package com.example.navigationdrawer

import retrofit2.Call
import retrofit2.http.GET

interface JsonPlaceholderAPI {
    @GET("messages")
    fun getPosts(): Call<List<ExamplePost>>
}