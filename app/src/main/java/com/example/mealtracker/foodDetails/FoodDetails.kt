package com.example.mealtracker.foodDetails

data class FoodDetails(
    val _links: Links,
    val hints: List<Hint>,
    val parsed: List<Parsed>,
    val text: String
)