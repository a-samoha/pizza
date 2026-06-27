package com.samos.pizza.domain.model

data class PizzaModel(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val variants: List<VariantModel>,
    val defaultSize: String,
) {

    data class VariantModel(
        val size: String,
        val price: Double,
    )
}
