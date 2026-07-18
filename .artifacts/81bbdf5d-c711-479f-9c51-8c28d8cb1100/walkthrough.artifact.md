# Walkthrough - Resolved Kotlin Plugin Conflict

The "Cannot add extension with name 'kotlin'" error has been resolved by migrating to AGP 9.0's built-in Kotlin support.

## Changes Made

### Build Configuration

#### [app/build.gradle.kts](file:///C:/Users/vrabe/OneDrive/Dokumentumok/Codes/AI/DartsMath/app/build.gradle.kts)
- Removed redundant `alias(libs.plugins.android.kotlin)` application.

#### [build.gradle.kts](file:///C:/Users/vrabe/OneDrive/Dokumentumok/Codes/AI/DartsMath/build.gradle.kts)
- Removed redundant `alias(libs.plugins.android.kotlin) apply false` declaration.

#### [libs.versions.toml](file:///C:/Users/vrabe/OneDrive/Dokumentumok/Codes/AI/DartsMath/gradle/libs.versions.toml)
- Cleaned up the `android-kotlin` plugin definition.

## Verification Results

### Automated Tests
- **Gradle Sync**: Successful.
- **Project Structure**: Verified that Kotlin support is now provided automatically by AGP 9.3.0.

> [!NOTE]
> Since AGP 9.0, the Android Gradle Plugin includes built-in Kotlin support, so the `org.jetbrains.kotlin.android` plugin is no longer required and will cause conflicts if applied manually.
