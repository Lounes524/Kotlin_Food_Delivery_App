package com.example.tdm.retrofit

import com.example.tdm.data.Dish
import com.example.tdm.data.Restaurant
import com.example.tdm.url
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface Endpoint {

    @GET("restaurants")
    suspend fun getAllRestaurants(): Response<List<Restaurant>>

    @GET("dishes/{id}")
    suspend fun getDishesById(@Path("id") dishId: Int?): Response<List<Dish>>



    companion object {
        @Volatile
        var endpoint: Endpoint? = null
        fun createEndpoint(): Endpoint {
            if(endpoint ==null) {
                synchronized(this) {
                    endpoint = Retrofit.Builder().baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create()).build()
                        .create(Endpoint::class.java)
                }
            }
            return endpoint!!

        }


    }
}