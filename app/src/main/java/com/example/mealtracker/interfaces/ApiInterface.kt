package com.example.mealtracker.interfaces

import retrofit2.Call
import retrofit2.http.GET


interface ApiInterface {

    @GET("auto-complete?app_key=f007851959429c18855f87f03b4a50b7&app_id=7c8a7243&q=chi")
    fun getData(): Call<List<String>>
}