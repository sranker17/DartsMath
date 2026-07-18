# Fix for "Could Not Resolve androidx.compose.material3:material3"

The project is failing to build because `androidx.compose.material3:material3` is declared without a version in `libs.versions.toml`, and the Compose BOM (Bill of Materials) is not correctly applied as a platform in `app/build.gradle.kts`. Without the BOM being applied via `platform()`, Gradle cannot resolve the versions for Compose libraries that don't have an explicit version defined.

## Proposed Changes

### [Component Name] app module

#### [MODIFY] [build.gradle.kts](file:///C:/Users/vrabe/OneDrive/Dokumentumok/Codes/AI/DartsMath/app/build.gradle.kts)

- Wrap `libs.androidx.compose.bom` with `platform()` in the `dependencies` block.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify that the project now builds successfully and all dependencies are resolved.

### Manual Verification
- Verify in Android Studio that the sync completes without errors.
