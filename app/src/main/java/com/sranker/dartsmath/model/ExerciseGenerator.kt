package com.sranker.dartsmath.model

import kotlin.random.Random

object ExerciseGenerator {

    fun generate(): Exercise {
        val throws = List(3) { generateDartThrow() }
        val totalScore = throws.sumOf { it.score }
        return Exercise(throws = throws, totalScore = totalScore)
    }

    private fun generateDartThrow(): DartThrow {
        return if (Random.nextFloat() < 0.10f) {
            if (Random.nextBoolean()) DartThrow.OuterBull else DartThrow.Bullseye
        } else {
            val number = Random.nextInt(1, 21)
            when (Random.nextInt(1, 4)) {
                1 -> DartThrow.Single(number)
                2 -> DartThrow.Double(number)
                else -> DartThrow.Triple(number)
            }
        }
    }
}
