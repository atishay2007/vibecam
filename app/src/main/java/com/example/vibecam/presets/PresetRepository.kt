package com.example.vibecam.presets

object PresetRepository {

    val presets = listOf(

        Preset(
            id = "tokyo_neon",
            name = "Tokyo Neon Rain",
            creator = "LUMYX",
            category = "Night",

            iso = 800,
            shutterSpeed = "1/8s",
            whiteBalance = 5500,

            look = "Cyberpunk neon streets",
            description = "Bright neon signs and rainy streets.",

            whyItWorks = listOf(
                "High ISO brightens dark scenes",
                "Slow shutter captures neon glow",
                "Cool color tones enhance cyberpunk vibes"
            )
        ),

        Preset(
            id = "motion_blur",
            name = "Motion Blur",

            creator = "LUMYX",
            category = "Street",

            iso = 400,
            shutterSpeed = "1/15s",

            whiteBalance = 5000,
            look = "Dreamy movement and light trails",

            description = "Captures movement and light trails.",

            whyItWorks = listOf(
                "Slow shutter records movement",
                "Creates natural motion blur"
            )
        ),

        Preset(
            id = "street_grain",
            name = "Street Grain",

            creator = "LUMYX",
            category = "Street",

            iso = 1600,
            shutterSpeed = "1/60s",

            whiteBalance = 6000,
            look = "Gritty documentary street photography",

            description = "Noisy documentary street photography.",

            whyItWorks = listOf(
                "High ISO introduces grain",
                "Creates a raw documentary feel"
            )
        )




    )
}
