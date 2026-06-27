package com.samos.pizza.di.module

import com.samos.pizza.data.repository.PizzasRepository
import com.samos.pizza.data.source.network.di.networkModule
import com.samos.pizza.data.usecase.GetPizzasUseCaseImpl
import com.samos.pizza.domain.usecase.GetPizzasUseCase
import com.samos.pizza.presentation.navigation.router.ComposeRouter
import com.samos.pizza.presentation.navigation.router.ComposeRouterImpl
import com.samos.pizza.presentation.screen.splash.SplashViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module {

    includes(networkModule)

    singleOf(::ComposeRouterImpl) bind ComposeRouter::class

    factoryOf(::PizzasRepository)
    factoryOf(::GetPizzasUseCaseImpl) bind GetPizzasUseCase::class

    viewModelOf(::SplashViewModel)
}
