package ru.antares.cheese_android.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.antares.cheese_android.domain.TimestampParser

/**
 * parser_module.kt
 * @author Павел
 * Created by on 06.04.2024 at 22:53
 * Android studio
 */

val parser_module = module {
    factoryOf(::TimestampParser)
}