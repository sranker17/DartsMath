package com.sranker.dartsmath.model

sealed interface DartThrow {
    val score: Int
    val displayText: String

    data class Single(val number: Int) : DartThrow {
        override val score: Int get() = number
        override val displayText: String get() = number.toString()
    }

    data class Double(val number: Int) : DartThrow {
        override val score: Int get() = number * 2
        override val displayText: String get() = "${number}×2"
    }

    data class Triple(val number: Int) : DartThrow {
        override val score: Int get() = number * 3
        override val displayText: String get() = "${number}×3"
    }

    data object OuterBull : DartThrow {
        override val score: Int get() = 25
        override val displayText: String get() = "25"
    }

    data object Bullseye : DartThrow {
        override val score: Int get() = 50
        override val displayText: String get() = "50"
    }
}
