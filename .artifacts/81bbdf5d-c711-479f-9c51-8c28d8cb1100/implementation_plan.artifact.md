# Implementation Plan - Fix Gradle Sync Error (Built-in Kotlin conflict)

The project is using Android Gradle Plugin (AGP) 9.3.0, which introduces **built-in Kotlin support** enabled by default. This means the `org.jetbrains.kotlin.android` plugin is no longer required and its manual application causes a conflict because AGP already registers the `kotlin` extension.

## Proposed Changes

### Build Configuration

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/vrabe/OneDrive/Dokumentumok/Codes/AI/DartsMath/app/build.gradle.kts)
- Remove `alias(libs.plugins.android.kotlin)` from the `plugins` block.

#### [MODIFY] [build.gradle.kts](file:///C:/Users/vrabe/OneDrive/Dokumentumok/Codes/AI/DartsMath/build.gradle.kts)
- Remove `alias(libs.plugins.android.kotlin) apply false` from the `plugins` block.

#### [MODIFY] [libs.versions.toml](file:///C:/Users/vrabe/OneDrive/Dokumentumok/Codes/AI/DartsMath/gradle/libs.versions.toml)
- Remove the `android-kotlin` plugin definition from the `[plugins]` section.

## Verification Plan

### Automated Tests
- Run Gradle Sync to ensure the "Cannot add extension with name 'kotlin'" error is resolved.
- Run `./gradlew assembleDebug` to verify that Kotlin compilation still works as expected with the built-in support.
