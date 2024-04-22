package ru.antares.cheese_android.data.remote.api.main.catalog

import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.domain.result.CheeseResult
import java.net.UnknownHostException

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 16:20
 * Android Studio
 */

class CatalogApiHandler(
    private val service: CatalogApi
) {
    suspend fun get(
        name: String? = null,
        position: Int? = null,
        parentID: String? = null,
        hasParent: Boolean? = null,
        page: Int? = 0,
        pageSize: Int? = 10,
        sortDirection: String? = null,
        sortByColumn: String? = null
    ): CheeseResult<CatalogError, Pagination<CategoryDTO>> = try {
        val response = service.get(
            name,
            position,
            parentID,
            hasParent,
            page,
            pageSize,
            sortDirection,
            sortByColumn
        )

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data

                if (data != null) {
                    CheeseResult.Success(data)
                } else {
                    CheeseResult.Error(CatalogError.ReceiveError)
                }
            }
            in 500..599 -> {
                CheeseResult.Error(CatalogError.ServerError)
            }
            else -> {
                CheeseResult.Error(CatalogError.UnknownError)
            }
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(CatalogError.NoInternetError)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(CatalogError.UnknownError)
    }
}