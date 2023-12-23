package com.example.utxo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET ("character")
    fun getRick(): Call<ResponseRick>

    @GET("character/{id}")
    fun getCharacterDetail(@Path("id") characterId: String): Call<ResultsItem>
}