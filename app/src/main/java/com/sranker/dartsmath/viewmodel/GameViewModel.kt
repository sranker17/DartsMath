package com.sranker.dartsmath.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sranker.dartsmath.model.Exercise
import com.sranker.dartsmath.model.ExerciseGenerator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface FeedbackState {
    data object None : FeedbackState
    data object Correct : FeedbackState
    data object Incorrect : FeedbackState
}

data class GameUiState(
    val currentExercise: Exercise = ExerciseGenerator.generate(),
    val userAnswer: String = "",
    val feedbackState: FeedbackState = FeedbackState.None,
    val streak: Int = 0,
    val totalSolved: Int = 0,
    val averageResponseTimeMs: Long = 0L,
)

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var exerciseStartTimeMs: Long = System.currentTimeMillis()

    fun onAnswerChanged(text: String) {
        val filtered = text.filter { it.isDigit() }
        _uiState.value = uiState.value.copy(userAnswer = filtered)
    }

    fun checkAnswer() {
        val answer = _uiState.value.userAnswer.toIntOrNull() ?: return
        val currentExercise = _uiState.value.currentExercise

        if (answer == currentExercise.totalScore) {
            val currentStreak = _uiState.value.streak + 1
            val currentTotal = _uiState.value.totalSolved + 1
            val responseTime = System.currentTimeMillis() - exerciseStartTimeMs
            val currentAverage = _uiState.value.averageResponseTimeMs
            val newAverage = if (currentTotal == 1) {
                responseTime
            } else {
                ((currentTotal - 1) * currentAverage + responseTime) / currentTotal
            }

            _uiState.value = _uiState.value.copy(
                feedbackState = FeedbackState.Correct,
                streak = currentStreak,
                totalSolved = currentTotal,
                averageResponseTimeMs = newAverage,
            )

            viewModelScope.launch {
                delay(1000L)
                nextExercise()
            }
        } else {
            _uiState.value = _uiState.value.copy(
                feedbackState = FeedbackState.Incorrect,
                streak = 0,
            )
        }
    }

    fun nextExercise() {
        _uiState.value = _uiState.value.copy(
            currentExercise = ExerciseGenerator.generate(),
            userAnswer = "",
            feedbackState = FeedbackState.None,
        )
        exerciseStartTimeMs = System.currentTimeMillis()
    }
}
