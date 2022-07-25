package com.great.app.repository

import com.great.app.R
import com.great.app.model.Character
import retrofit2.HttpException

class Repository(private val marvelApi: MarvelApi) {

    suspend fun loadCharacters(): List<Character> {
        return withErrorCheck {
            marvelApi.getCharacters().data!!.results!!
        }
    }

    suspend fun loadCharacter(id: Int): Character {
        return withErrorCheck {
            marvelApi.getCharacter(id).data!!.results!!.get(0)
        }
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
}

class RepositoryError(
    val messageResId: Int,
    cause: Throwable? = null
) : Exception(cause)