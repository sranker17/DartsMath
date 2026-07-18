package com.sranker.dartsmath.model

data class Exercise(
    val throws: List<DartThrow>,
    val totalScore: Int
) {
    val displayExpression: String
        get() = throws.joinToString(" + ") { it.displayText }
}
