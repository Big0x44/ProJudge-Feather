# ProJudge Feather Agent Guide

Welcome to **ProJudge Feather**, a mobile app designed for tracking and managing badminton match scores. This document serves as a developer and agent onboarding guide covering the project structure, design decisions, execution commands, and development practices.

---

## 1. Project Overview

ProJudge Feather is built as a modern Android application using **Jetpack Compose** and official **Android Architecture Guidelines**.

### Tech Stack & Libraries
- **Language**: Kotlin 2.2+
- **UI Framework**: Jetpack Compose (using Material 3 UI widgets)
- **Architecture**: Model-View-ViewModel (MVVM) with Unidirectional Data Flow (UDF)
- **Dependency Injection**: Dagger Hilt 2.59.2+
- **Build System**: Gradle (Kotlin DSL, `build.gradle.kts`)
- **Testing**: JUnit 4

### Directory & Package Structure
- **[`MainActivity.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/MainActivity.kt)**: Entry point loading the main UI, annotated with `@AndroidEntryPoint`.
- **[`di/`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/di/)**: Dependency injection modules configuration.
  - **[`AppModule.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/di/AppModule.kt)**: Provides singleton instances of database, DAOs, customized dispatchers and coroutine scopes.
  - **[`RepositoryModule.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/di/RepositoryModule.kt)**: Binds repository implementations to domain repository interfaces.
- **[`model/`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/model/)**: Core domain logic and repository interfaces (framework-independent).
  - **[`ScoreboardLogic.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/model/ScoreboardLogic.kt)**: Badminton rule checker (Match Point / Winner check).
  - **[`Player.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/model/Player.kt)**: Player domain model.
  - **[`MatchResult.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/model/MatchResult.kt)**: Match result domain model.
  - **[`Repositories.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/model/Repositories.kt)**: Repository interfaces (`PlayerRepository`, `MatchResultRepository`).
  - **[`SettingsRepository.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/model/SettingsRepository.kt)**: Repository defining setting persistence interface and SharedPreferences implementation.
- **[`data/`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/data/)**: Data persistence and file I/O layer.
  - **`database/`**: Room database config (`AppDatabase`), entity tables (`PlayerEntity`, `MatchResultEntity`), and DAOs.
  - **`repository/`**: Concretions of repository interfaces (`PlayerRepositoryImpl`, `MatchResultRepositoryImpl`).
  - **[`JsonMatchExporter.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/data/JsonMatchExporter.kt)**: JSON exporter utilizing `MatchResultRepository`.
  - **[`JsonMatchImporter.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/data/JsonMatchImporter.kt)**: JSON importer utilizing `MatchResultRepository`.
- **[`ui/`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/ui/)**: Presentation layer.
  - **`theme/`**: Color palettes, Typography definition, and App Theme.
  - **`scoreboard/`**: Scoreboard screens and modularized view components.
    - **[`ScoreboardUiState.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/ui/scoreboard/ScoreboardUiState.kt)**: Unified immutable state definition.
    - **[`ScoreboardViewModel.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/ui/scoreboard/ScoreboardViewModel.kt)**: Emits UI state, consumes click actions, and interacts with repositories.
    - **[`ScoreboardScreen.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/ui/scoreboard/ScoreboardScreen.kt)**: Stateful screen host and stateless content container.
    - **[`HistoryScreen.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/ui/scoreboard/HistoryScreen.kt)**: Match history view.
    - **[`PlayersScreen.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/ui/scoreboard/PlayersScreen.kt)**: Player definition and management view.
    - **`components/`**: Modularized view components (`PlayerHalf.kt`, `ControlPanel.kt`, `RenameDialog.kt`, `StartMatchDialog.kt`, `VectorIcons.kt`).
  - **`settings/`**: Settings-specific components.
    - **[`SettingsUiState.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/ui/settings/SettingsUiState.kt)**: Unified immutable settings state definition.
    - **[`SettingsViewModel.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/ui/settings/SettingsViewModel.kt)**: Manages and validates settings state.
    - **[`SettingsScreen.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/ui/settings/SettingsScreen.kt)**: Composable view allowing settings modification.

---

## 2. Build and Test Commands

### Environment Requirements
The project requires JDK 11 or higher and the Android SDK to build. If they are not configured in your environment, set the following variables:
```bash
# Path to Android Studio JBR and Android SDK:
export JAVA_HOME=/opt/android-studio/jbr
export ANDROID_HOME=/home/daniel/Android/Sdk
```

### Gradle Commands
Run these commands from the project root directory:

- **Build / Compile Debug APK**:
  ```bash
  JAVA_HOME=/opt/android-studio/jbr ./gradlew assembleDebug
  ```
- **Execute Unit Tests**:
  ```bash
  JAVA_HOME=/opt/android-studio/jbr ./gradlew test
  ```
- **Clean Build Cache**:
  ```bash
  JAVA_HOME=/opt/android-studio/jbr ./gradlew clean
  ```

---

## 3. Code Style Guidelines

1. **Unidirectional Data Flow (UDF)**:
   - Composables should rely on hoisted states rather than modifying data directly.
   - Screen layouts should be split into a stateful host (observing Flow/ViewModel) and a stateless visual content container (which only consumes data structures and events).
2. **Icons and Resources**:
   - To keep dependencies lightweight and fully compatible across different Compose platforms, define icons programmatically using custom vector paths (see [`VectorIcons.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/ui/scoreboard/components/VectorIcons.kt)) instead of relying on heavy XML layouts or extra Google Icon dependencies.
3. **Immutability**:
   - The UI State must always be declared as an immutable `data class` (`ScoreboardUiState`). State transitions must use the `.copy()` function inside `StateFlow.update {}` blocks.
4. **Localization & Resource Decoupling**:
   - Do not hardcode user-facing strings inside Compose UI files. Use `stringResource(R.string.id)` inside Composable functions.
   - Keep the `ViewModel` agnostic of Android resource strings and `Context` objects. Instead, store type-safe structures like Enums (`MatchStatus`) or nullable attributes (e.g., `name1: String? = null` where `null` signifies fallback to default) inside the UI state, and resolve them to localized resource strings within the stateless visual content container.
5. **Dependency Injection (Hilt)**:
   - All classes requiring dependencies (ViewModels, Repositories, DAOs) should use constructor injection using `@Inject constructor`.
   - Implementations of interfaces should be bound using `@Binds` in Dagger modules (e.g., [`RepositoryModule.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/di/RepositoryModule.kt)).
   - Do not instantiate or hold references to global singletons manually. Use Dagger Hilt to manage the scopes and lifetimes.
   - For injecting custom qualifiers on constructor parameters that are also properties, prepend the parameter annotations with the `@param:` target filter (e.g., `@param:DatabaseScope` or `@param:IoDispatcher`) to avoid Kotlin compiler property/parameter mismatch warnings and ensure correct Hilt code generation.
6. **Screen Activity & Display State**:
   - To ensure the device display remains active during scoring/match tracking, the main scoreboard/count screen utilizes `DisposableEffect` with `LocalView.current` to set `keepScreenOn = true` during composition, and resets it to `false` when disposed.

---

## 4. Testing Instructions

All business rules, JSON serializers, settings updates, and view transitions are fully verified via local unit tests:
- **Rule Verification**: [`ExampleUnitTest.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/test/java/de/big0x44/projudgefeather/ExampleUnitTest.kt) tests badminton scoring rules (deuces, match-point detection, winner caps).
- **JSON Import / Export Verification**: [`JsonMatchExporterTest.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/test/java/de/big0x44/projudgefeather/data/JsonMatchExporterTest.kt) and [`JsonMatchImporterTest.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/test/java/de/big0x44/projudgefeather/data/JsonMatchImporterTest.kt) test exporting and importing match results.
- **ViewModel Interactions**:
  - [`ScoreboardViewModelTest.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/test/java/de/big0x44/projudgefeather/ui/scoreboard/ScoreboardViewModelTest.kt) tests state mutation histories, player renaming constraints, navigation routing, player addition/deletion, automatic match result persistence using fake test repositories, undo states, reset behaviors, and dynamic setting adjustments.
  - [`SettingsViewModelTest.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/test/java/de/big0x44/projudgefeather/ui/settings/SettingsViewModelTest.kt) tests settings initialization, validation constraints, and persistence updates.
- **Test Coroutine Dispatchers**: The ViewModel constructor supports coroutine scope and dispatcher injection, allowing JVM unit tests to override standard asynchronous operation handlers with `Dispatchers.Unconfined` for synchronous testing.

To add new tests, place them under `app/src/test/java/` aligning packages to the tested file. Ensure tests run with the `./gradlew test` task.

---

## 5. Security Considerations

1. **Input Sanitization**:
   - Player names entered in the rename or add player dialog must be trimmed of leading/trailing whitespace.
   - Empty or blank inputs are ignored and fallback to default names to prevent blank fields or rendering corruption.
2. **Database Integrity**:
   - The players table contains a unique database index on the player name field to prevent duplicate players.
   - In-memory undo history list (`history` inside the ViewModel) is capped to a max length of 50 actions to prevent heap leaks.
3. **JSON Escaping**:
   - When exporting match records, string fields (such as player names and IDs) are securely escaped using backslashes for quotes, newlines, and other special characters to prevent JSON injection or format corruption.
4. **No External Libraries**:
   - To prevent supply-chain vulnerabilities, do not add external UI or icon packs without explicit permission. Use standard Android Jetpack and Kotlin libraries.

---

## 6. Living Documentation Guidelines

This file is a **living document**. Developers and AI coding agents working on this repository must treat this guide as active documentation and automatically update it whenever:
- Core business rules, packages, or architectural structures change.
- New scripts, build tasks, or gradle environment configurations are introduced.
- Additional testing conventions or security parameters are defined.

Ensure changes to codebase systems are reflected here immediately to prevent documentation drift.

