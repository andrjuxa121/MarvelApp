package com.great.app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.great.app.model.Character
import com.great.app.model.DataWrapper
import com.great.app.repository.retrofit.ApiService
import com.great.app.repository.retrofit.RetrofitBuilder
import com.great.app.utils.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepoViewModel: ViewModel() {
    private val apiService: ApiService =
        RetrofitBuilder.getRetrofit(Constant.BASE_URL).create(ApiService::class.java)

    private val _characters: MutableLiveData<List<Character>?> by lazy {
        MutableLiveData<List<Character>?>().also {
            loadCharacters()
        }
    }
    val characters: LiveData<List<Character>?> = _characters

    private val _character = MutableLiveData<Character?>()
    val character: LiveData<Character?> = _character

    fun loadCharacters() {
        apiService.getCharacters().enqueue(object: Callback<DataWrapper> {
            override fun onFailure(call: Call<DataWrapper>, t: Throwable) {
                _characters.value = null;
            }
            override fun onResponse(
                call: Call<DataWrapper?>,
                response: Response<DataWrapper?>
            ) {
                response.body()?.let { dataWrapper ->
                    _characters.value = dataWrapper.data?.results
                }
            }
        })
    }

    fun setCharacter(character: Character?) {
        _character.value = character
    }

    fun loadCharacter(id: Int) {
        apiService.getCharacter(id).enqueue(object: Callback<DataWrapper> {
            override fun onFailure(call: Call<DataWrapper>, t: Throwable) {
                _character.value = null;
            }
            override fun onResponse(
                call: Call<DataWrapper?>,
                response: Response<DataWrapper?>
            ) {
                response.body()?.let { dataWrapper ->
                    _character.value = dataWrapper.data?.results?.get(0)
                }
            }
        })
    }
}