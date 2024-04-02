package ru.antares.cheese_android.presentation.navigation.util

interface NavigationGraph {
    val route: String
}

sealed class Screen(
    val route: String,
    vararg parameters: String
) {
    val url: String = buildString {
        append(route)
        parameters.forEach { parameter -> append("/{$parameter}") }
    }

    data object SplashScreen : Screen(route = "SplashScreen")

    object AuthNavigationGraph : NavigationGraph {
        override val route: String = "AUTH_NAVIGATION_GRAPH"

        data object InputPhone : Screen(route = "SignInScreen")

        /**
         *  route: ConfirmCodeScreen/{phone}
         * */
        data object ConfirmCode : Screen(route = "ConfirmCodeScreen", "phone")
    }

    object HomeNavigationGraph : NavigationGraph {
        override val route: String = "HOME_NAVIGATION_GRAPH"

        data object Home : Screen(route = "HomeScreen")
        // Screens ...
    }

    object CatalogNavigationGraph : NavigationGraph {
        override val route: String = "CATALOG_NAVIGATION_GRAPH"

        /**
         * ROUTE: CatalogScreen
         * URL: CatalogScreen
         * */
        data object Catalog : Screen(route = "CatalogScreen")

        /**
         * ROUTE: CatalogParentCategoryScreen
         * URL: CatalogParentCategoryScreen/parentID/name
         *
         * @param parentID parent category id
         * @param name name of category
         * */
        data object CatalogParentCategory: Screen(route = "CatalogParentCategoryScreen", "parentID", "name")

        /**
         * ROUTE: ProductsScreen
         * URL: ProductsScreen/id/name
         *
         * @param id category id
         * @param name name of category
         *
         * */
        data object Products: Screen(route = "ProductsScreen", "id", "name")

        /**
         * ROUTE: ProductDetailScreen
         * URL: ProductDetailScreen/id
         *
         * @param id product id
         * */
        data object ProductDetail: Screen("route = ProductDetailScreen", "id")
        // Screens ...
    }

    object CommunityNavigationGraph : NavigationGraph {
        override val route: String = "COMMUNITY_NAVIGATION_GRAPH"

        data object Community : Screen(route = "CommunityScreen")
        // Screens ...
    }

    object CartNavigationGraph : NavigationGraph {
        override val route: String = "CART_NAVIGATION_GRAPH"

        data object Cart : Screen(route = "CartScreen")
        // Screens ...
    }

    object ProfileNavigationGraph : NavigationGraph {
        override val route: String = "PROFILE_NAVIGATION_GRAPH"

        data object Profile : Screen(route = "ProfileScreen")
        data object PersonalData: Screen(route = "PersonalDataScreen")
        data object Addresses: Screen(route = "AddressesScreen")
        data object Orders: Screen(route = "OrdersScreen")
        data object AboutApp: Screen(route = "PaymentMethodsScreen")
        data object Tickets: Screen(route = "TicketsScreen")
        // Screens ...
    }
}