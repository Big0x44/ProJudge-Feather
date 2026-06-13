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

val SettingsIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Settings",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(19.14f, 12.94f)
            curveTo(19.18f, 12.63f, 19.2f, 12.32f, 19.2f, 12f)
            curveTo(19.2f, 11.68f, 19.18f, 11.37f, 19.14f, 11.06f)
            lineTo(21.27f, 9.4f)
            curveTo(21.46f, 9.25f, 21.51f, 8.98f, 21.39f, 8.76f)
            lineTo(19.39f, 5.3f)
            curveTo(19.27f, 5.08f, 19.01f, 4.99f, 18.79f, 5.08f)
            lineTo(16.28f, 6.09f)
            curveTo(15.76f, 5.69f, 15.19f, 5.36f, 14.57f, 5.11f)
            lineTo(14.19f, 2.44f)
            curveTo(14.15f, 2.19f, 13.94f, 2f, 13.69f, 2f)
            horizontalLineTo(9.69f)
            curveTo(9.44f, 2f, 9.23f, 2.19f, 9.19f, 2.44f)
            lineTo(8.81f, 5.11f)
            curveTo(8.19f, 5.36f, 7.62f, 5.69f, 7.1f, 6.09f)
            lineTo(4.59f, 5.08f)
            curveTo(4.37f, 4.99f, 4.11f, 5.08f, 3.99f, 5.3f)
            lineTo(1.99f, 8.76f)
            curveTo(1.87f, 8.98f, 1.92f, 9.25f, 2.11f, 9.4f)
            lineTo(4.24f, 11.06f)
            curveTo(4.2f, 11.37f, 4.18f, 11.68f, 4.18f, 12f)
            curveTo(4.18f, 12.32f, 4.2f, 12.63f, 4.24f, 12.94f)
            lineTo(2.11f, 14.6f)
            curveTo(1.92f, 14.75f, 1.87f, 15.02f, 1.99f, 15.24f)
            lineTo(3.99f, 18.7f)
            curveTo(4.11f, 18.92f, 4.37f, 19.01f, 4.59f, 18.92f)
            lineTo(7.1f, 17.91f)
            curveTo(7.62f, 18.31f, 8.19f, 18.64f, 8.81f, 18.89f)
            lineTo(9.19f, 21.56f)
            curveTo(9.23f, 21.81f, 9.44f, 22f, 9.69f, 22f)
            horizontalLineTo(13.69f)
            curveTo(13.94f, 22f, 14.15f, 21.81f, 14.19f, 21.56f)
            lineTo(14.57f, 18.89f)
            curveTo(15.19f, 18.64f, 15.76f, 18.31f, 16.28f, 17.91f)
            lineTo(18.79f, 18.92f)
            curveTo(19.01f, 19.01f, 19.27f, 18.92f, 19.39f, 18.7f)
            lineTo(21.39f, 15.24f)
            curveTo(21.51f, 15.02f, 21.46f, 14.75f, 21.27f, 14.6f)
            lineTo(19.14f, 12.94f)
            close()
            moveTo(12f, 15.5f)
            curveTo(10.07f, 15.5f, 8.5f, 13.93f, 8.5f, 12f)
            curveTo(8.5f, 10.07f, 10.07f, 8.5f, 12f, 8.5f)
            curveTo(13.93f, 8.5f, 15.5f, 10.07f, 15.5f, 12f)
            curveTo(15.5f, 13.93f, 13.93f, 15.5f, 12f, 15.5f)
            close()
        }
    }.build()

val ArrowBackIcon: ImageVector
    get() = ImageVector.Builder(
        name = "ArrowBack",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(20f, 11f)
            horizontalLineTo(7.83f)
            lineTo(13.42f, 5.42f)
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

val RemoveIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Remove",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(19f, 13f)
            horizontalLineTo(5f)
            verticalLineTo(11f)
            horizontalLineTo(19f)
            verticalLineTo(13f)
            close()
        }
    }.build()


