package com.example.pokemon

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

data class TypeListResponse(
    val results: List<TypeItem>
)

data class TypeItem(
    val name: String,
    val url: String
)

interface PokeApiService {

        @GET("pokemon/{id}/")
        fun getPokemonDetails(@Path("id") id: Int): Call<PokemonDetails>



    @GET("type")
    fun getTypeList(): Call<TypeListResponse>

    @GET("pokemon")
    fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<PokemonListResponse>

    @GET("type/{typeName}")
    fun getPokemonByType(@Path("typeName") typeName: String): Call<TypeDetailResponse>
}

data class TypeDetailResponse(
    val pokemon: List<PokemonSlot>
)

data class PokemonSlot(
    val pokemon: PokemonListItem
)
