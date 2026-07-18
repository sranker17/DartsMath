package com.sranker.dartsmath

import com.sranker.dartsmath.model.DartThrow
import com.sranker.dartsmath.model.ExerciseGenerator
import org.junit.Assert.*
import org.junit.Test

class ExerciseGeneratorTest {

    @Test
    fun generatedExercise_hasExactlyThreeThrows() {
        val exercise = ExerciseGenerator.generate()
        assertEquals(3, exercise.throws.size)
    }

    @Test
    fun allThrowScores_areWithinValidRange() {
        repeat(1000) {
            val exercise = ExerciseGenerator.generate()
            for (dartThrow in exercise.throws) {
                when (dartThrow) {
                    is DartThrow.Single -> {
                        assertTrue("Single number ${dartThrow.number} out of range",
                            dartThrow.number in 1..20)
                        assertEquals(dartThrow.number, dartThrow.score)
                    }
                    is DartThrow.Double -> {
                        assertTrue("Double number ${dartThrow.number} out of range",
                            dartThrow.number in 1..20)
                        assertEquals(dartThrow.number * 2, dartThrow.score)
                    }
                    is DartThrow.Triple -> {
                        assertTrue("Triple number ${dartThrow.number} out of range",
                            dartThrow.number in 1..20)
                        assertEquals(dartThrow.number * 3, dartThrow.score)
                    }
                    is DartThrow.OuterBull -> {
                        assertEquals(25, dartThrow.score)
                    }
                    is DartThrow.Bullseye -> {
                        assertEquals(50, dartThrow.score)
                    }
                }
            }
        }
    }

    @Test
    fun exerciseTotalScore_matchesSumOfThrowScores() {
        repeat(1000) {
            val exercise = ExerciseGenerator.generate()
            val expectedTotal = exercise.throws.sumOf { it.score }
            assertEquals(expectedTotal, exercise.totalScore)
        }
    }

    @Test
    fun displayExpression_containsAllThrowTexts() {
        repeat(1000) {
            val exercise = ExerciseGenerator.generate()
            val expectedExpression = exercise.throws.joinToString(" + ") { it.displayText }
            assertEquals(expectedExpression, exercise.displayExpression)
        }
    }

    @Test
    fun bullProbability_isRoughlyTenPercent() {
        val desiredThrowCount = 10_000
        val exerciseCount = (desiredThrowCount + 2) / 3  // ceil(desiredThrowCount / 3)
        var bullCount = 0
        var totalThrows = 0

        repeat(exerciseCount) {
            val exercise = ExerciseGenerator.generate()
            for (dartThrow in exercise.throws) {
                totalThrows++
                if (dartThrow is DartThrow.OuterBull || dartThrow is DartThrow.Bullseye) {
                    bullCount++
                }
            }
        }

        val bullRatio = bullCount.toDouble() / totalThrows
        assertTrue("Bull ratio $bullRatio was not between 0.05 and 0.15",
            bullRatio in 0.05..0.15)
    }
}
