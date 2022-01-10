package com.example.navigationdrawer

import retrofit2.Call
import retrofit2.http.*

interface JsonPlaceholderAPI {
    @GET("messages")
    fun getPosts(): Call<ArrayList<ExamplePost>>

    @PUT("message/{id}")
    fun putPost(@Path("id") id: String, @Body body: Post) :Call<ExamplePost>

    @POST("message")
    fun postData(@Body post: Post) :Call<ExamplePost>

    @DELETE("message/{id}")
    fun deletePost(@Path("id") id: String) :Call<Void>
}