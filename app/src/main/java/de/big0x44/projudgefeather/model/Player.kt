package de.big0x44.projudgefeather.model

data class Player(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String
)
