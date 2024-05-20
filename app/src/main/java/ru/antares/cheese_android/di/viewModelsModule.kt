package ru.antares.cheese_android.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.antares.cheese_android.presentation.view.authorization.confirm_code.ConfirmCodeViewModel
import ru.antares.cheese_android.presentation.view.authorization.input_phone.InputPhoneViewModel
import ru.antares.cheese_android.presentation.view.main.MainViewModel
import ru.antares.cheese_android.presentation.view.main.cart_graph.cart.CartViewModel
import ru.antares.cheese_android.presentation.view.main.cart_graph.order.checkout.CheckoutOrderViewModel
import ru.antares.cheese_android.presentation.view.main.cart_graph.order.confirm.ConfirmOrderViewModel
import ru.antares.cheese_android.presentation.view.main.cart_graph.order.select_address.SelectAddressViewModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogViewModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_parent_category.CatalogParentCategoryViewModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail.ProductDetailViewModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductsViewModel
import ru.antares.cheese_android.presentation.view.main.community_graph.community.CommunityViewModel
import ru.antares.cheese_android.presentation.view.main.community_graph.activity.ActivityViewModel
import ru.antares.cheese_android.presentation.view.main.community_graph.post.PostViewModel
import ru.antares.cheese_android.presentation.view.main.home_graph.home.HomeScreenViewModel
import ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.AddressesViewModel
import ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.create.CreateAddressViewModel
import ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data.PersonalDataViewModel
import ru.antares.cheese_android.presentation.view.main.profile_graph.profile.ProfileViewModel
import ru.antares.cheese_android.presentation.view.splash.SplashScreenViewModel

val viewModelsModule: List<Module>
    get() = listOf(
        mainViewModel,
        splashScreenViewModelModule,
        authViewModelsModule,
        profileViewModelsModule,
        catalogViewModelsModule,
        cartViewModelsModule,
        communityViewModelsModule,
        addressesViewModelModule,
        homeViewModel,

    )

private val splashScreenViewModelModule = module {
    viewModelOf(::SplashScreenViewModel)
}

private val mainViewModel = module {
    viewModelOf(::MainViewModel)
}

private val authViewModelsModule = module {
    viewModelOf(::InputPhoneViewModel)
    viewModelOf(::ConfirmCodeViewModel)
}

private val homeViewModel = module {
    viewModelOf(::HomeScreenViewModel)
}

private val cartViewModelsModule = module {
    viewModelOf(::CartViewModel)
    viewModelOf(::CheckoutOrderViewModel)
    viewModelOf(::ConfirmOrderViewModel)
    viewModelOf(::SelectAddressViewModel)
}

private val profileViewModelsModule = module {
    viewModelOf(::ProfileViewModel)
    viewModelOf(::PersonalDataViewModel)
}

private val catalogViewModelsModule = module {
    viewModelOf(::CatalogViewModel)
    viewModelOf(::CatalogParentCategoryViewModel)
    viewModelOf(::ProductsViewModel)
    viewModelOf(::ProductDetailViewModel)
}

private val communityViewModelsModule = module {
    viewModelOf(::CommunityViewModel)
    viewModelOf(::ActivityViewModel)
    viewModelOf(::PostViewModel)
}

private val addressesViewModelModule = module {
    viewModelOf(::AddressesViewModel)
    viewModelOf(::CreateAddressViewModel)
}