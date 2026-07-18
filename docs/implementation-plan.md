# DartsMath — Implementation Plan

## Overview
Build a native Android app (Kotlin + Jetpack Compose + Material 3) for dart score mental arithmetic training. Dark-themed, MVVM architecture, no XML layouts.

## Key Decisions
- **Bull probability:** ~10% per throw (each throw has 10% chance of being 25 or 50 instead of standard 1–20 × 1–3)
- **Nice-to-haves:** All included (streak counter, total solved, avg response time, vibration, success animation)

---

## Task 1: Project Setup (`project-setup`)
**Goal:** Add Compose, Material 3, and ViewModel dependencies to the Gradle build.

**Files to modify:**
- `gradle/libs.versions.toml` — add Compose BOM, compose-ui, compose-material3, activity-compose, lifecycle-viewmodel-compose, lifecycle-runtime-compose version entries and library declarations
- `app/build.gradle.kts` — add `kotlin("plugin.compose")` plugin, `buildFeatures { compose = true }`, and all new `implementation()` dependencies

**Expected outcome:** Project syncs successfully with Compose + Material 3 available.

---

## Task 2: Data Model & Generator (`data-model`)
**Goal:** Define the dart throw types and random exercise generator.

**Files to create:**
- `app/src/main/java/com/sranker/dartsmath/model/DartThrow.kt` — sealed interface with data classes: `Single(n)`, `Double(n)`, `Triple(n)`, `OuterBull`, `Bullseye`. Each has a `score` property.
- `app/src/main/java/com/sranker/dartsmath/model/Exercise.kt` — data class holding `List<DartThrow>` (3 items) and total score.
- `app/src/main/java/com/sranker/dartsmath/model/ExerciseGenerator.kt` — `generate(): Exercise` function:
  - For each of 3 throws: roll random. 90% → random 1–20 + random 1–3 multiplier. 10% → 50% OuterBull / 50% Bullseye.
  - Display: e.g. `12 + 20×3 + 25`, computed total.

**Expected outcome:** Call `ExerciseGenerator.generate()` and get a valid 3-throw exercise.

---

## Task 3: GameViewModel (`viewmodel`)
**Goal:** MVVM state management for the game.

**Files to create:**
- `app/src/main/java/com/sranker/dartsmath/viewmodel/GameViewModel.kt`

**State:** `GameUiState` data class:
- `currentExercise: Exercise`
- `userAnswer: String`
- `feedbackState: FeedbackState` (None / Correct / Incorrect)
- `streak: Int`, `totalSolved: Int`, `averageResponseTimeMs: Long`
- Private tracking: `exerciseStartTime`, `totalTimeMs`, `totalAnswers`

**Logic:**
- `onAnswerChanged(text: String)` — filter to digits only
- `checkAnswer()` — compare user input to exercise total
  - Correct → FeedbackState.Correct, schedule delayed `nextExercise()` (800–1200ms), update streak++, totalSolved++, record response time
  - Incorrect → FeedbackState.Incorrect (persists until user retries)
- `nextExercise()` — generate new exercise, clear input, reset feedback

**Expected outcome:** ViewModel exposes reactive state for the UI to observe.

---

## Task 4: Game UI (`game-ui`)
**Goal:** Compose screen with dark theme, exercise display, input, button.

**Files to create:**
- `app/src/main/java/com/sranker/dartsmath/ui/GameScreen.kt`
- `app/src/main/java/com/sranker/dartsmath/ui/theme/Theme.kt` (if needed inline, or minimal)

**Layout (top to bottom):**
1. Stats row (streak 🔥, solved ✅, avg time ⏱) — initially hidden/empty
2. Large exercise expression text (e.g. "12 + 20×3 + 25 = ?")
3. OutlinedTextField for numeric answer, large font, border color reflects feedback (green/red/default)
4. "Check" button
5. Success checkmark (✓) overlay on correct

**Behavior:**
- On correct: input border turns green, ✓ appears, 800–1200ms delay then next exercise
- On incorrect: input border turns red, vibration (if available), input stays editable
- IME action sends "Check"

**Expected outcome:** Fully functional game screen matching the spec layout.

---

## Task 5: App Integration (`app-integration`)
**Goal:** Wire everything into MainActivity with proper manifest.

**Files to modify/create:**
- `app/src/main/java/com/sranker/dartsmath/MainActivity.kt` — `setContent { DartsMathTheme { GameScreen() } }`
- `app/src/main/AndroidManifest.xml` — add `<activity>` with `SingleInstance` launch mode or `windowSoftInputMode="adjustResize"`
- `app/src/main/res/values/themes.xml` — remove old theme or adjust for Material 3 dark

**Expected outcome:** App launches to the game screen, builds and runs on device/emulator.

---

## Task 6: Nice-to-Haves (`nice-to-haves`)
**Goal:** Polish features on top of core game.

**Changes across ViewModel + UI:**
- **Streak counter:** Display "🔥 5" in top stats bar, reset to 0 on incorrect
- **Total solved:** Display "✅ 42" in top stats bar
- **Average response time:** Track per-exercise timing, compute rolling average, display "⏱ 2.3s"
- **Vibration on incorrect:** Use `HapticFeedback` or `Vibrator` service with light 50ms vibration
- **Success animation:** Simple scale + fade-in ✓ icon using `AnimatedVisibility` / `animateFloatAsState`

---

## Task 7: Tests (`tests`)
**Goal:** Verify generator and ViewModel correctness.

**Files to create:**
- `app/src/test/java/com/sranker/dartsmath/ExerciseGeneratorTest.kt` — tests:
  - Generated exercise always has exactly 3 throws
  - All throw scores are within valid range (1–60 for standard, 25 or 50 for bulls)
  - Score calculation is correct (sum of individual scores)
  - Bull probability roughly ~10% over many iterations
- `app/src/test/java/com/sranker/dartsmath/GameViewModelTest.kt` — tests:
  - Correct answer → FeedbackState.Correct → streak increments → auto-advances
  - Incorrect answer → FeedbackState.Incorrect → streak resets → exercise stays
  - Non-numeric input filtered
  - Stats (totalSolved, averageTime) update correctly

**Expected outcome:** `./gradlew test` passes all tests.

---

## Execution Order (Dependencies)
```
project-setup
    ↓
data-model
    ↓
viewmodel ──→ tests
    ↓
game-ui
    ↓
app-integration
    ↓
nice-to-haves
```

Tasks are fully separable agent tasks. Each agent receives full context from this plan and the original spec. The dependency chain ensures agents working on downstream tasks have the interfaces/APIs defined by upstream tasks.
