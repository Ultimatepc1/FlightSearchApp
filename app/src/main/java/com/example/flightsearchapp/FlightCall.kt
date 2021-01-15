package com.example.flightsearchapp

import retrofit2.Call
import retrofit2.http.GET

interface FlightCall {
    @GET("5979c6731100001e039edcb3")
    fun getAllFlights(): Call<FlightResponse>
}