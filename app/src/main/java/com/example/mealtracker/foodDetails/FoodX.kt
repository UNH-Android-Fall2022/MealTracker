package com.example.mealtracker.foodDetails

data class FoodX(
    val category: String,
    val categoryLabel: String,
    val foodId: String,
    val image: String,
    val knownAs: String,
    val label: String,
    val nutrients: NutrientsX
)