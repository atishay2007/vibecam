package com.example.vibecam.presets

object PresetRepository {

    val presets = listOf(

        Preset(
            id = "tokyo_neon",
            name = "Tokyo Neon Rain",
            iso = 800,
            shutterSpeed = "1/8s",
            description = "Bright neon signs, cool colors, rainy streets."
        ),

        Preset(
            id = "motion_blur",
            name = "Motion Blur",
            iso = 400,
            shutterSpeed = "1/15s",
            description = "Captures movement and light trails."
        ),

        Preset(
            id = "street_grain",
            name = "Street Grain",
            iso = 1600,
            shutterSpeed = "1/60s",
            description = "Noisy documentary street photography."
        )
    )
}
