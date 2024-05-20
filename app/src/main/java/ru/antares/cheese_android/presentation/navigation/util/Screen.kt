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

    /**
     * ROUTE: SplashScreen
     * URL: SplashScreen
     * */
    data object SplashScreen : Screen(route = "SplashScreen")

    object AuthNavigationGraph : NavigationGraph {
        override val route: String = "AUTH_NAVIGATION_GRAPH"

        /**
         *  ROUTE: SignInScreen
         *  URL: SignInScreen
         * */
        data object InputPhone : Screen(route = "SignInScreen")

        /**
         *  ROUTE: ConfirmCodeScreen/{phone}
         * */
        data object ConfirmCode : Screen(route = "ConfirmCodeScreen", "phone")
    }

    object HomeNavigationGraph : NavigationGraph {
        override val route: String = "HOME_NAVIGATION_GRAPH"

        /**
         * ROUTE: HomeScreen
         * URL: HomeScreen
         * */
        data object Home : Screen(route = "HomeScreen")

        /**
         * ROUTE: HomePostScreen
         * URL: HomePostScreen/id
         * */
        data object Post : Screen(route = "HomePostScreen", "id")

        /**
         * ROUTE: HomeActivityScreen
         * URL: HomeActivityScreen/id
         * */
        data object Activity: Screen(route = "HomeActivityScreen", "id")

        /**
         * ROUTE: HomeRecommendationScreen
         * URL: HomeRecommendationScreen/id
         * */
        data object ProductDetailScreen: Screen(route = "HomeProductDetailScreen", "id")
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
        data object CatalogParentCategory :
            Screen(route = "CatalogParentCategoryScreen", "parentID", "name")

        /**
         * ROUTE: ProductsScreen
         * URL: ProductsScreen/id/name
         *
         * @param id category id
         * @param name name of category
         *
         * */
        data object Products : Screen(route = "ProductsScreen", "id", "name")

        /**
         * ROUTE: ProductDetailScreen
         * URL: ProductDetailScreen/id
         *
         * @param id product id
         * */
        data object ProductDetail : Screen("route = ProductDetailScreen", "id")
        // Screens ...
    }

    object CommunityNavigationGraph : NavigationGraph {
        override val route: String = "COMMUNITY_NAVIGATION_GRAPH"

        /**
         * ROUTE: CommunityScreen
         * URL: CommunityScreen
         * */
        data object Community : Screen(route = "CommunityScreen")

        /**
         * ROUTE: CommunityDetailScreen
         * URL: CommunityDetailScreen/id
         *
         * @param id post id
         * */
        data object CommunityActivityScreen : Screen(route = "CommunityActivityScreen", "id")

        /**
         * ROUTE: CommunityPostScreen
         * URL: CommunityPostScreen/id
         *
         * @param id post id
         * */
        data object CommunityPostScreen : Screen(route = "CommunityPostScreen", "id")
        data object ProductDetailScreen : Screen(route = "ProductDetailScreen", "id")
        // Screens ...
    }

    object CartNavigationGraph : NavigationGraph {
        override val route: String = "CART_NAVIGATION_GRAPH"

        /**
         * ROUTE: CartScreen
         * URL: CartScreen
         * */
        data object Cart : Screen(route = "CartScreen")

        /**
         * ROUTE: CheckoutOrderScreen
         * URL: CheckoutOrderScreen/total_cost
         * */
        data object CheckoutOrder : Screen(route = "CheckoutOrderScreen", "total_cost")

        /**
         * Route: ConfirmOrderScreen
         * URL: ConfirmOrderScreen/address_id/receiver/payment_type/comment/total_cost
         * */
        data object ConfirmOrder : Screen(
            route = "ConfirmOrderScreen",
            "address_id",
            "receiver",
            "payment_type",
            "comment",
            "total_cost"
        )

        /**
         * Route: SelectAddressScreen
         * URL: SelectAddressScreen
         * */
        data object SelectAddress: Screen(
            route = "SelectAddressScreen"
        )
        // Screens ...
    }

    object ProfileNavigationGraph : NavigationGraph {
        override val route: String = "PROFILE_NAVIGATION_GRAPH"

        data object Profile : Screen(route = "ProfileScreen")
        data object PersonalData : Screen(route = "PersonalDataScreen")
        data object Addresses : Screen(route = "AddressesScreen")
        data object CreateAddress : Screen(route = "CreateAddressScreen")
        data object Orders : Screen(route = "OrdersScreen")
        data object AboutApp : Screen(route = "PaymentMethodsScreen")
        data object Tickets : Screen(route = "TicketsScreen")
        // Screens ...
    }
}