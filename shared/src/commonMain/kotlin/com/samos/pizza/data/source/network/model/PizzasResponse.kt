package com.samos.pizza.data.source.network.model

import com.samos.pizza.domain.model.PizzaModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PizzasResponse(
    val pizzas: List<PizzaResponse>,
) {

    @Serializable
    class PizzaResponse(
        val id: String,
        val name: String,
        val description: String,
        @SerialName("image_url")
        val imageUrl: String,
        val variants: List<VariantResponse>,
        @SerialName("default_size")
        val defaultSize: String,
    ) {

        fun toDomain(): PizzaModel =
            PizzaModel(
                id = id,
                name = name,
                description = description,
                imageUrl = imageUrl,
                variants = variants.map { it.toDomain() },
                defaultSize = defaultSize,
            )
    }

    @Serializable
    class VariantResponse(
        val size: String,
        val price: Double,
    ) {
        fun toDomain() = PizzaModel.VariantModel(
            size = size,
            price = price,
        )
    }
}
