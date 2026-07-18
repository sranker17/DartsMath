# Android Darts Score Training App – AI Development Specification

## Project Overview

Build a native Android application using the **latest stable versions of Kotlin, Gradle, Android Studio, and Jetpack Compose**. The application is designed to help users practice mental arithmetic for calculating dart scores.

The UI should be **minimalistic, modern, and dark-themed** (Material 3 with Dark Mode).

---

# Core Functionality

The application continuously generates random dart scoring exercises.

Each exercise consists of **exactly three dart throws**.

Each throw is randomly generated as follows:

* Multiplier: 1×, 2×, or 3×
* Number: 1–20

This means every generated throw represents one of:

* Single (1×)
* Double (2×)
* Triple (3×)

Example throws:

* 12 (which is 12*1 but here we don't show the multiplier)
* 18×2
* 20×3

---

## Bull Scores

In addition to the standard throws, the generator should also occasionally include:

* Outer Bull = 25
* Bullseye = 50

These should be treated as valid dart scores.

---

# Exercise Generation

Generate exactly three random throws.

Example exercises:

* 12 + 20×3 + 25 = ?
* 18×2 + 17 + 19×3 = ?
* 50 + 20 + 7×2 = ?

The displayed expression should clearly show multipliers where applicable.

---

# User Input

Display a numeric input field where the user enters the total score.

Only integer input is allowed.

Include a button labeled:

**Check**

Alternatively, automatically validate the answer when the user submits via the keyboard.

---

# Validation Logic

If the answer is correct:

* Highlight the answer area in green.
* Optionally display a small success indicator (✓).
* After a short delay (approximately 800–1200 ms), automatically generate the next exercise.
* Clear the input field.

If the answer is incorrect:

* Highlight the answer area in red.
* Keep the current exercise visible.
* Allow the user to continue guessing without generating a new exercise.
* The exercise should only change after the correct answer is entered.

---

# User Experience

The interaction should be very fast:

1. New exercise appears.
2. User enters a number.
3. Immediate validation.
4. Green for correct.
5. Red for incorrect.
6. Automatically continue after a correct answer.

No confirmation dialogs or unnecessary animations.

---

# UI Requirements

The design should be clean and uncluttered.

Use:

* Material Design 3
* Jetpack Compose
* Dark theme by default
* Large typography for the exercise
* Large numeric input
* Comfortable spacing
* High contrast
* Rounded corners
* Modern Android appearance

The exercise should be the primary visual element.

Example layout:

```
----------------------------

12 + 20×3 + 25 = ?

[      97      ]

[ Check ]

----------------------------
```

When correct:

* Green input border/background.

When incorrect:

* Red input border/background.

---

# Architecture

Use modern Android development best practices:

* Kotlin
* Jetpack Compose
* Material 3
* MVVM architecture
* StateFlow or Compose State
* ViewModel
* No XML layouts
* No deprecated APIs
* Follow official Android architecture recommendations.

---

# Random Generation Rules

For each of the three throws:

* Number: random integer from 1 to 20
* Multiplier: random integer from 1 to 3

Occasionally replace one generated throw with:

* 25
* 50

The probability for bull scores should be low enough that standard dart throws remain the most common.

---

# Nice-to-Have Enhancements

If time permits, implement:

* Current streak counter (consecutive correct answers)
* Total number of solved exercises
* Average response time
* Light vibration on incorrect answers
* Small success animation when the answer is correct

These features are optional and should not complicate the core user experience.

---

# Goal

The application should be extremely lightweight, responsive, and focused solely on helping users improve mental arithmetic for dart scoring through rapid, repetitive practice.
