package ru.antares.cheese_android.data.remote.services.community

import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.services.community.dto.PostDTO
import ru.antares.cheese_android.domain.result.CheeseResult
import java.net.UnknownHostException

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 15:25
 * Android Studio
 */

class CommunityServiceHandler(
    private val service: CommunityService
) {
    suspend fun get(
        size: Int?,
        page: Int?,
        sort: String?
    ): CheeseResult<GetCommunityError, Pagination<PostDTO>> = try {
        val response = service.get(size, page, sort)
        
        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data
                
                if (data != null) {
                    CheeseResult.Success(data)
                } else {
                    CheeseResult.Error(GetCommunityError.LOAD_ERROR)
                }
            }
            in 500..599 -> {
                CheeseResult.Error(GetCommunityError.SERVER_ERROR)
            }
            else -> {
                CheeseResult.Error(GetCommunityError.UNKNOWN_ERROR)
            }
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(GetCommunityError.NO_INTERNET_ERROR)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(GetCommunityError.UNKNOWN_ERROR)
    }

    suspend fun get(
        id: String
    ): CheeseResult<GetCommunityError, PostDTO> = try {
        val response = service.get(id)

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data

                if (data != null) {
                    CheeseResult.Success(data)
                } else {
                    CheeseResult.Error(GetCommunityError.LOAD_ERROR)
                }
            }
            in 500..599 -> {
                CheeseResult.Error(GetCommunityError.SERVER_ERROR)
            }
            else -> {
                CheeseResult.Error(GetCommunityError.UNKNOWN_ERROR)
            }
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(GetCommunityError.NO_INTERNET_ERROR)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(GetCommunityError.UNKNOWN_ERROR)
    }
}