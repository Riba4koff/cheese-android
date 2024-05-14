package ru.antares.cheese_android.data.remote.api.main.community

import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.api.main.community.dto.PostDTO
import ru.antares.cheese_android.domain.result.CheeseResult
import java.net.UnknownHostException

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 15:25
 * Android Studio
 */

class CommunityApiHandler(
    private val service: CommunityApi
) {
    suspend fun get(
        size: Int?,
        page: Int?,
        sort: String?,
        hasActivity: Boolean?
    ): CheeseResult<CommunityError, Pagination<PostDTO>> = try {
        val response = service.get(size, page, sort, hasActivity)
        
        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data
                
                if (data != null) {
                    CheeseResult.Success(data)
                } else {
                    CheeseResult.Error(CommunityError.LOAD_ERROR)
                }
            }
            in 500..599 -> {
                CheeseResult.Error(CommunityError.SERVER_ERROR)
            }
            else -> {
                CheeseResult.Error(CommunityError.UNKNOWN_ERROR)
            }
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(CommunityError.NO_INTERNET_ERROR)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(CommunityError.UNKNOWN_ERROR)
    }

    suspend fun get(
        id: String
    ): CheeseResult<CommunityError, PostDTO> = try {
        val response = service.get(id)

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data

                if (data != null) {
                    CheeseResult.Success(data)
                } else {
                    CheeseResult.Error(CommunityError.LOAD_ERROR)
                }
            }
            in 500..599 -> {
                CheeseResult.Error(CommunityError.SERVER_ERROR)
            }
            else -> {
                CheeseResult.Error(CommunityError.UNKNOWN_ERROR)
            }
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(CommunityError.NO_INTERNET_ERROR)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(CommunityError.UNKNOWN_ERROR)
    }
}