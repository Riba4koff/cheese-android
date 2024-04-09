package ru.antares.cheese_android.presentation.navigation.bottomBar

import ru.antares.cheese_android.R
import ru.antares.cheese_android.presentation.navigation.util.Screen

enum class BottomBarDestination(
    val icon: Int,
    val title: String,
    val route: String,
) {
    Home(icon = R.drawable.bottom_bar_home_icon, title = "Главная", route = Screen.HomeNavigationGraph.route),
    Catalog(icon = R.drawable.bottom_bar_catalog_icon, title = "Каталог", route = Screen.CatalogNavigationGraph.route),
    Community(icon = R.drawable.bottom_bar_community_icon, title = "Сообщество", route = Screen.CommunityNavigationGraph.route),
    Cart(icon = R.drawable.bottom_bar_cart_icon, title = "Корзина", route = Screen.CartNavigationGraph.route),
    Profile(icon = R.drawable.bottom_bar_profile_icon, title = "Профиль", route = Screen.ProfileNavigationGraph.route),
}