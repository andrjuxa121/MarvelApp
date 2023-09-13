package com.great.app.repository

import com.great.app.R
import com.great.app.model.Character
import retrofit2.HttpException

class Repository(private val marvelApi: MarvelApi) {

    private var offsetOfApiCall = BASE_OFFSET_OF_API_CALL
    private var maxOffsetOfApiCall = BASE_OFFSET_OF_API_CALL

    fun resetFetchOffset() {
        offsetOfApiCall = BASE_OFFSET_OF_API_CALL
        maxOffsetOfApiCall = BASE_OFFSET_OF_API_CALL
    }

    fun increaseFetchOffset(): Boolean {
        val oldOffset = offsetOfApiCall
        offsetOfApiCall += PAGE_LIMIT
        if (offsetOfApiCall > maxOffsetOfApiCall) {
            offsetOfApiCall = maxOffsetOfApiCall
        }
        return (offsetOfApiCall > oldOffset)
    }

    suspend fun loadCharacters(): List<Character> {
        return withErrorCheck {
            val data = marvelApi.getCharacters(
                offsetOfApiCall, PAGE_LIMIT
            ).data
            maxOffsetOfApiCall = getMaxOffset(data.total)
            data.results
        }
    }

    suspend fun loadCharacter(id: Int): Character {
        return withErrorCheck {
            marvelApi.getCharacter(id).data.results[0]
        }
    }

    private fun getMaxOffset(totalCount: Int): Int {
        return (totalCount / PAGE_LIMIT) * PAGE_LIMIT
    }

    private suspend fun <T> withErrorCheck(
        block: suspend () -> T
    ): T {
        try {
            return block()
        } catch (e: HttpException) {
            if (e.code() == ErrorCode.UNAUTHORIZED) {
                throw RepositoryError(R.string.invalid_credentials)
            } else throw RepositoryError(R.string.data_not_found)
        } catch (cause: Throwable) {
            throw RepositoryError(R.string.something_went_wrong, cause)
        }
    }

    companion object {
        private const val BASE_OFFSET_OF_API_CALL = 0
        private const val PAGE_LIMIT = 20
    }
}

class RepositoryError(
    val messageResId: Int,
    cause: Throwable? = null
) : Exception(cause)