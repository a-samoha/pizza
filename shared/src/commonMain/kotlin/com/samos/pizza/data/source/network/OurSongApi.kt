package com.samos.pizza.data.source.network

import com.samos.pizza.data.source.network.model.PizzasResponse
import de.jensklingenberg.ktorfit.http.GET

interface OurSongApi {

    @GET("pizzas")
    suspend fun getPizzas(): PizzasResponse
}