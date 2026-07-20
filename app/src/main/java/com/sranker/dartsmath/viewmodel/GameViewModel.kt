package com.sranker.dartsmath.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sranker.dartsmath.R
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
    val backgroundImageRes: Int? = null,
)

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var exerciseStartTimeMs: Long = System.currentTimeMillis()

    fun onAnswerChanged(text: String) {
        val filtered = text.filter { it.isDigit() }
        _uiState.value = uiState.value.copy(userAnswer = filtered)
    }

    companion object {
        private val backgroundImages = listOf(
            R.drawable.bg_1,
            R.drawable.bg_2,
            R.drawable.bg_3,
            R.drawable.bg_4,
            R.drawable.bg_5,
            R.drawable.bg_6,
            R.drawable.bg_7,
            R.drawable.bg_8,
            R.drawable.bg_9,
            R.drawable.bg_10,
        )
        private val usedBackgrounds = mutableSetOf<Int>()
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

            val newBackground = if (currentStreak % 5 == 0) {
                val available = backgroundImages.filter { it !in usedBackgrounds }
                if (available.isEmpty()) usedBackgrounds.clear()
                (if (available.isEmpty()) backgroundImages else available).random()
                    .also { usedBackgrounds.add(it) }
            } else {
                _uiState.value.backgroundImageRes
            }

            _uiState.value = _uiState.value.copy(
                feedbackState = FeedbackState.Correct,
                streak = currentStreak,
                totalSolved = currentTotal,
                averageResponseTimeMs = newAverage,
                backgroundImageRes = newBackground,
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
