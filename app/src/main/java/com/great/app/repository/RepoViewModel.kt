package com.great.app.repository

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.great.app.R
import com.great.app.model.Character
import com.great.app.model.DataWrapper
import com.great.app.repository.retrofit.ApiService
import com.great.app.repository.retrofit.RetrofitBuilder
import com.great.app.utils.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepoViewModel(application: Application): AndroidViewModel(application) {

    private val apiService: ApiService =
        RetrofitBuilder.getRetrofit(Constant.BASE_URL).create(ApiService::class.java)

    private val _characters = MutableLiveData<List<Character>?>()
    val characters: LiveData<List<Character>?> = _characters

    private val _character = MutableLiveData<Character?>()
    val character: LiveData<Character?> = _character

    fun loadCharacters(responseListener: IResponseListener) {
        apiService.getCharacters().enqueue(object: Callback<DataWrapper> {
            override fun onFailure(call: Call<DataWrapper>, t: Throwable) {
                _characters.value = null
                responseListener.onFailure(R.string.no_response)
            }
            override fun onResponse(
                call: Call<DataWrapper?>,
                response: Response<DataWrapper?>
            ) {
                if(!response.isSuccessful) {
                    responseListener.onFailure(R.string.bad_request)
                    _character.value = null
                    return
                }
                response.body()?.let { dataWrapper ->
                    _characters.value = dataWrapper.data?.results
                    responseListener.onSuccess()
                    return
                }
                responseListener.onFailure(R.string.empty_response)
            }
        })
    }

    fun setCharacter(character: Character) {
        _character.value = character
    }

    fun loadCharacter(id: Int, responseListener: IResponseListener) {
        apiService.getCharacter(id).enqueue(object: Callback<DataWrapper> {
            override fun onFailure(call: Call<DataWrapper>, t: Throwable) {
                _character.value = null
                responseListener.onFailure(R.string.no_response)
            }
            override fun onResponse(
                call: Call<DataWrapper?>,
                response: Response<DataWrapper?>
            ) {
                response.body()?.let { dataWrapper ->
                    _character.value = dataWrapper.data?.results?.get(0)
                    responseListener.onSuccess()
                    return
                }
                responseListener.onFailure(R.string.empty_response)
            }
        })
    }

    interface IResponseListener {
        fun onSuccess()
        fun onFailure(messageResId: Int)
    }
}