package com.samos.pizza.di

import com.samos.pizza.di.module.platformModule
import com.samos.pizza.di.module.sharedModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    config: KoinAppDeclaration? = null
) = startKoin {
    config?.invoke(this)
    modules(platformModule, sharedModule)
}
