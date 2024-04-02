package ru.antares.cheese_android.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.antares.cheese_android.presentation.view.authorization.confirm_code.ConfirmCodeViewModel
import ru.antares.cheese_android.presentation.view.authorization.input_phone.InputPhoneViewModel
import ru.antares.cheese_android.presentation.view.main.MainViewModel
import ru.antares.cheese_android.presentation.view.main.cart_graph.cart.CartViewModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogViewModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_parent_category.CatalogParentCategoryViewModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail.ProductDetailViewModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductsViewModel
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
        cartViewModelsModule
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

private val cartViewModelsModule = module {
    viewModelOf(::CartViewModel)
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