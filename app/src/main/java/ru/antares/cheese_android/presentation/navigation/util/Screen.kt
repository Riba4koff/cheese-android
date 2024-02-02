package ru.antares.cheese_android.presentation.navigation.util

interface NavigationGraph {
    val route: String
}

sealed class Screen(
    vararg parameters: String,
    val route: String = buildString {
        append(this)
        parameters.forEach { parameter -> append("/{$parameter}") }
    },
) {

    data object SplashScreen : Screen(route = "SplashScreen")

    object AuthNavigationGraph : NavigationGraph {
        override val route: String = "AUTH_NAVIGATION_GRAPH"

        data object InputPhone : Screen(route = "SignInScreen")
        data object ConfirmCode : Screen("phone", route = "ConfirmCode")
    }

    object HomeNavigationGraph : NavigationGraph {
        override val route: String = "HOME_NAVIGATION_GRAPH"

        data object Home: Screen(route = "HomeScreen")
        // Screens ...
    }

    object CatalogNavigationGraph : NavigationGraph {
        override val route: String = "CATALOG_NAVIGATION_GRAPH"

        data object Catalog: Screen(route = "CatalogScreen")
        // Screens ...
    }

    object CommunityNavigationGraph : NavigationGraph {
        override val route: String = "COMMUNITY_NAVIGATION_GRAPH"

        data object Community: Screen(route = "CommunityScreen")
        // Screens ...
    }

    object CartNavigationGraph : NavigationGraph {
        override val route: String = "CART_NAVIGATION_GRAPH"

        data object Cart: Screen(route = "CartScreen")
        // Screens ...
    }

    object ProfileNavigationGraph : NavigationGraph {
        override val route: String = "PROFILE_NAVIGATION_GRAPH"

        data object Profile: Screen(route = "ProfileScreen")
        // Screens ...
    }
}