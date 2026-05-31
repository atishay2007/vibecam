package com.example.vibecam.presets

data class Preset(
    val id: String,
    val name: String,
    val creator: String,
    val category: String,

    val iso: Int,
    val shutterSpeed: String,
    val whiteBalance: Int,
    val look: String,

    val description: String,
    val whyItWorks: List<String>
)