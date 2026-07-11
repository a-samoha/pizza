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
        val size: PizzaSize,
        val price: Double,
    )

    enum class PizzaSize {
        S, M, L;

        companion object {
            fun from(raw: String) = entries.find { it.name.equals(raw, ignoreCase = true) } ?: M
        }
    }
}
