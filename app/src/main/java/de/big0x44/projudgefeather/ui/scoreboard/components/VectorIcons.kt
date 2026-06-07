package de.big0x44.projudgefeather.ui.scoreboard.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val UndoIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Undo",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(12.5f, 8f)
            curveTo(9.85f, 8f, 7.45f, 9f, 5.6f, 10.6f)
            lineTo(2f, 7f)
            verticalLineTo(16f)
            horizontalLineTo(11f)
            lineTo(7.38f, 12.38f)
            curveTo(8.77f, 11.22f, 10.54f, 10.5f, 12.5f, 10.5f)
            curveTo(16.5f, 10.5f, 19.73f, 13.3f, 20.35f, 17.09f)
            lineTo(22.8f, 16.29f)
            curveTo(21.95f, 11.07f, 17.7f, 8f, 12.5f, 8f)
            close()
        }
    }.build()

val EditIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Edit",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(3f, 17.25f)
            verticalLineTo(21f)
            horizontalLineTo(6.75f)
            lineTo(17.81f, 9.94f)
            lineTo(14.06f, 6.19f)
            lineTo(3f, 17.25f)
            close()
            moveTo(20.71f, 7.04f)
            curveTo(21.1f, 6.65f, 21.1f, 6.02f, 20.71f, 5.63f)
            lineTo(18.37f, 3.29f)
            curveTo(17.98f, 2.9f, 17.35f, 2.9f, 16.96f, 3.29f)
            lineTo(15.13f, 5.12f)
            lineTo(18.88f, 8.87f)
            lineTo(20.71f, 7.04f)
            close()
        }
    }.build()

val RefreshIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Refresh",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(17.65f, 6.35f)
            curveTo(16.2f, 4.9f, 14.21f, 4f, 12f, 4f)
            curveTo(7.58f, 4f, 4.01f, 7.58f, 4.01f, 12f)
            curveTo(4.01f, 16.42f, 7.58f, 20f, 12f, 20f)
            curveTo(15.73f, 20f, 18.84f, 17.45f, 19.73f, 14f)
            horizontalLineTo(17.65f)
            curveTo(16.83f, 16.33f, 14.61f, 18f, 12f, 18f)
            curveTo(8.69f, 18f, 6f, 15.31f, 6f, 12f)
            curveTo(6f, 8.69f, 8.69f, 6f, 12f, 6f)
            curveTo(13.66f, 6f, 15.14f, 6.69f, 16.22f, 7.78f)
            lineTo(13f, 11f)
            horizontalLineTo(20f)
            verticalLineTo(4f)
            lineTo(17.65f, 6.35f)
            close()
        }
    }.build()

val HistoryIcon: ImageVector
    get() = ImageVector.Builder(
        name = "History",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(13f, 3f)
            curveTo(8.03f, 3f, 4f, 7.03f, 4f, 12f)
            horizontalLineTo(1f)
            lineTo(4.89f, 15.89f)
            curveTo(4.96f, 15.96f, 5.04f, 16f, 5.14f, 16f)
            curveTo(5.24f, 16f, 5.32f, 15.96f, 5.39f, 15.89f)
            lineTo(9f, 12f)
            horizontalLineTo(6f)
            curveTo(6f, 8.13f, 9.13f, 5f, 13f, 5f)
            curveTo(16.87f, 5f, 20f, 8.13f, 20f, 12f)
            curveTo(20f, 15.87f, 16.87f, 19f, 13f, 19f)
            curveTo(11.07f, 19f, 9.32f, 18.21f, 8.06f, 16.94f)
            lineTo(6.64f, 18.36f)
            curveTo(8.27f, 19.99f, 10.51f, 21f, 13f, 21f)
            curveTo(17.97f, 21f, 22f, 16.97f, 22f, 12f)
            curveTo(22f, 7.03f, 17.97f, 3f, 13f, 3f)
            close()
            moveTo(12.5f, 7f)
            verticalLineTo(13f)
            lineTo(17.5f, 16f)
            lineTo(18.25f, 14.75f)
            lineTo(14f, 12.25f)
            verticalLineTo(7f)
            horizontalLineTo(12.5f)
            close()
        }
    }.build()

val PeopleIcon: ImageVector
    get() = ImageVector.Builder(
        name = "People",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(16f, 11f)
            curveTo(17.66f, 11f, 18.99f, 9.66f, 18.99f, 8f)
            curveTo(18.99f, 6.34f, 17.66f, 5f, 16f, 5f)
            curveTo(14.34f, 5f, 13f, 6.34f, 13f, 8f)
            curveTo(13f, 9.66f, 14.34f, 11f, 16f, 11f)
            close()
            moveTo(8f, 11f)
            curveTo(9.66f, 11f, 11f, 9.66f, 11f, 8f)
            curveTo(11f, 6.34f, 9.66f, 5f, 8f, 5f)
            curveTo(6.34f, 5f, 5f, 6.34f, 5f, 8f)
            curveTo(5f, 9.66f, 6.34f, 11f, 8f, 11f)
            close()
            moveTo(8f, 13f)
            curveTo(5.33f, 13f, 0f, 14.33f, 0f, 17f)
            verticalLineTo(19f)
            horizontalLineTo(16f)
            verticalLineTo(17f)
            curveTo(16f, 14.33f, 10.67f, 13f, 8f, 13f)
            close()
            moveTo(16f, 13f)
            curveTo(15.71f, 13f, 15.38f, 13.02f, 15.03f, 13.05f)
            curveTo(16.19f, 14.07f, 16.97f, 15.39f, 16.97f, 17f)
            verticalLineTo(19f)
            horizontalLineTo(24f)
            verticalLineTo(17f)
            curveTo(24f, 14.33f, 18.67f, 13f, 16f, 13f)
            close()
        }
    }.build()

val DeleteIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Delete",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(6f, 19f)
            curveTo(6f, 20.1f, 6.9f, 21f, 8f, 21f)
            horizontalLineTo(16f)
            curveTo(16.9f, 21f, 17.8f, 20.1f, 17.8f, 19f)
            verticalLineTo(7f)
            horizontalLineTo(6f)
            verticalLineTo(19f)
            close()
            moveTo(19f, 4f)
            horizontalLineTo(15.5f)
            lineTo(14.5f, 3f)
            horizontalLineTo(9.5f)
            lineTo(8.5f, 4f)
            horizontalLineTo(5f)
            verticalLineTo(6f)
            horizontalLineTo(19f)
            verticalLineTo(4f)
            close()
        }
    }.build()

val AddIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Add",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(19f, 13f)
            horizontalLineTo(13f)
            verticalLineTo(19f)
            horizontalLineTo(11f)
            verticalLineTo(13f)
            horizontalLineTo(5f)
            verticalLineTo(11f)
            horizontalLineTo(11f)
            verticalLineTo(5f)
            horizontalLineTo(13f)
            verticalLineTo(11f)
            horizontalLineTo(19f)
            verticalLineTo(13f)
            close()
        }
    }.build()

val BackIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Back",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(20f, 11f)
            horizontalLineTo(7.83f)
            lineTo(13.42f, 5.41f)
            lineTo(12f, 4f)
            lineTo(4f, 12f)
            lineTo(12f, 20f)
            lineTo(13.41f, 18.59f)
            lineTo(7.83f, 13f)
            horizontalLineTo(20f)
            verticalLineTo(11f)
            close()
        }
    }.build()

val ShareIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Share",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(18f, 16.08f)
            curveTo(17.24f, 16.08f, 16.56f, 16.38f, 16.04f, 16.85f)
            lineTo(8.91f, 12.7f)
            curveTo(8.96f, 12.47f, 9f, 12.24f, 9f, 12f)
            curveTo(9f, 11.76f, 8.96f, 11.53f, 8.91f, 11.3f)
            lineTo(15.96f, 7.19f)
            curveTo(16.5f, 7.69f, 17.21f, 8f, 18f, 8f)
            curveTo(19.66f, 8f, 21f, 6.66f, 21f, 5f)
            curveTo(21f, 3.34f, 19.66f, 2f, 18f, 2f)
            curveTo(16.34f, 2f, 15f, 3.34f, 15f, 5f)
            curveTo(15f, 5.24f, 15.04f, 5.47f, 15.09f, 5.7f)
            lineTo(8.04f, 9.81f)
            curveTo(7.5f, 9.31f, 6.79f, 9f, 6f, 9f)
            curveTo(4.34f, 9f, 3f, 10.34f, 3f, 12f)
            curveTo(3f, 13.66f, 4.34f, 15f, 6f, 15f)
            curveTo(6.79f, 15f, 7.5f, 14.69f, 8.04f, 14.19f)
            lineTo(15.16f, 18.35f)
            curveTo(15.11f, 18.56f, 15.08f, 18.78f, 15.08f, 19f)
            curveTo(15.08f, 20.61f, 16.39f, 21.92f, 18f, 21.92f)
            curveTo(19.61f, 21.92f, 20.92f, 20.61f, 20.92f, 19f)
            curveTo(20.92f, 17.39f, 19.61f, 16.08f, 18f, 16.08f)
            close()
        }
    }.build()

val FileOpenIcon: ImageVector
    get() = ImageVector.Builder(
        name = "FileOpen",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(20f, 6f)
            horizontalLineTo(12f)
            lineTo(10f, 4f)
            horizontalLineTo(4f)
            curveTo(2.9f, 4f, 2.01f, 4.9f, 2.01f, 6f)
            lineTo(2f, 18f)
            curveTo(2f, 19.1f, 2.9f, 20f, 4f, 20f)
            horizontalLineTo(20f)
            curveTo(21.1f, 20f, 22f, 19.1f, 22f, 18f)
            verticalLineTo(8f)
            curveTo(22f, 6.9f, 21.1f, 6f, 20f, 6f)
            close()
            moveTo(16f, 17f)
            horizontalLineTo(8f)
            verticalLineTo(15f)
            horizontalLineTo(16f)
            verticalLineTo(17f)
            close()
            moveTo(16f, 13f)
            horizontalLineTo(8f)
            verticalLineTo(11f)
            horizontalLineTo(16f)
            verticalLineTo(13f)
            close()
        }
    }.build()
