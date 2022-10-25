package com.example.mealtracker.interfaces

import com.example.mealtracker.foodDetails.FoodDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET("auto-complete?app_key=f007851959429c18855f87f03b4a50b7&app_id=7c8a7243")
    fun getData(@Query("q") query: String): Call<List<String>>

    @GET("api/food-database/v2/parser?app_id=ed2239b0&app_key=3155c8e121e5e5e3818560eb51fce674")
    fun getFoodDetails(@Query("ingr") query: String): Call<FoodDetails>
}