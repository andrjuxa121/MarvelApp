package com.great.app.repository

import com.great.app.R
import com.great.app.model.Character

class Repository(private val marvelApi: MarvelApi) {

    suspend fun loadCharacters(): List<Character> {
        val characters = try {
            marvelApi.getCharacters().data?.results
        } catch (cause: Throwable) {
            throw RepositoryError(R.string.something_went_wrong, cause)
        }
        characters?.apply { return this }
        throw RepositoryError(R.string.data_not_found)
    }

    suspend fun loadCharacter(id: Int): Character {
        val character = try {
            marvelApi.getCharacter(id).data?.results?.get(0)
        } catch (cause: Throwable) {
            throw RepositoryError(R.string.something_went_wrong, cause)
        }
        character?.apply { return this }
        throw RepositoryError(R.string.data_not_found)
    }
}

class RepositoryError(
    val messageResId: Int,
    cause: Throwable? = null
) : Exception(cause)