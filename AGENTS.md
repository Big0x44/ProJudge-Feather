# ProJudge Feather Agent Guide

Welcome to **ProJudge Feather**, a mobile app designed for tracking and managing badminton match scores. This document serves as a developer and agent onboarding guide covering the project structure, design decisions, execution commands, and development practices.

---

## 1. Project Overview

ProJudge Feather is built as a modern Android application using **Jetpack Compose** and official **Android Architecture Guidelines**.

### Tech Stack & Libraries
- **Language**: Kotlin 2.2+
- **UI Framework**: Jetpack Compose (using Material 3 UI widgets)
- **Architecture**: Model-View-ViewModel (MVVM) with Unidirectional Data Flow (UDF)
- **Build System**: Gradle (Kotlin DSL, `build.gradle.kts`)
- **Testing**: JUnit 4

### Directory & Package Structure
- **[`MainActivity.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/MainActivity.kt)**: Entry point loading the main UI.
- **[`model/`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/model/)**: Core domain logic.
  - **[`ScoreboardLogic.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/model/ScoreboardLogic.kt)**: Badminton rule checker (Match Point / Winner check).
- **[`ui/`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/ui/)**: Presentation layer.
  - **`theme/`**: Color palettes, Typography definition, and App Theme.
  - **`scoreboard/`**: Scoreboard-specific components.
    - **[`ScoreboardUiState.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/ui/scoreboard/ScoreboardUiState.kt)**: Unified immutable state definition.
    - **[`ScoreboardViewModel.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/ui/scoreboard/ScoreboardViewModel.kt)**: Emits UI state and consumes click actions.
    - **[`ScoreboardScreen.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/main/java/de/big0x44/projudgefeather/ui/scoreboard/ScoreboardScreen.kt)**: Stateful screen host and stateless content container.
    - **`components/`**: Modularized view components (`PlayerHalf.kt`, `ControlPanel.kt`, `RenameDialog.kt`, `VectorIcons.kt`).

---

## 2. Build and Test Commands

### Java Environment Requirements
The project requires JDK 11 or higher to build. If java is not set up on your path or you run into environment issues, use the JDK packaged inside Android Studio:
```bash
# Path to Android Studio JBR:
export JAVA_HOME=/opt/android-studio/jbr
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

---

## 4. Testing Instructions

All business rules and view transitions are fully verified via local unit tests:
- **Rule Verification**: [`ExampleUnitTest.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/test/java/de/big0x44/projudgefeather/ExampleUnitTest.kt) tests badminton scoring rules (deuces, match-point detection, winner caps).
- **ViewModel Interactions**: [`ScoreboardViewModelTest.kt`](file:///home/daniel/src/ProJudge-Feather/app/src/test/java/de/big0x44/projudgefeather/ui/scoreboard/ScoreboardViewModelTest.kt) tests state mutation histories, player renaming constraints, undo states, and reset behaviors.

To add new tests, place them under `app/src/test/java/` aligning packages to the tested file. Ensure tests run with the `./gradlew test` task.

---

## 5. Security Considerations

1. **Input Sanitization**:
   - Player names entered in the rename dialog must be trimmed of leading/trailing whitespace.
   - Empty or blank inputs are ignored and fallback to default names ("Player 1", "Player 2") to prevent blank fields or rendering corruption.
2. **State Protection**:
   - Scoreboard history lists (`history` inside the ViewModel) are capped to a max length of 50 actions to prevent infinite memory growth or heap leaks during extremely long matches.
3. **No External Libraries**:
   - To prevent supply-chain vulnerabilities, do not add external UI or icon packs without explicit permission. Use standard Android Jetpack and Kotlin libraries.

---

## 6. Living Documentation Guidelines

This file is a **living document**. Developers and AI coding agents working on this repository must treat this guide as active documentation and automatically update it whenever:
- Core business rules, packages, or architectural structures change.
- New scripts, build tasks, or gradle environment configurations are introduced.
- Additional testing conventions or security parameters are defined.

Ensure changes to codebase systems are reflected here immediately to prevent documentation drift.

