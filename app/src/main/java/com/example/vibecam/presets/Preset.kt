package com.example.vibecam.presets

data class Preset(
    val id: String,
    val name: String,
    val iso: Int,
    val shutterSpeed: String,
    val description: String
)