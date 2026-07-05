package com.samos.pizza.presentation.screen.home.viewmodel

import com.samos.pizza.presentation.base.MviIntent

sealed interface HomeIntent : MviIntent {

    object OnBackClick : HomeIntent
    data class OnSwipe(val index: Int) : HomeIntent
}