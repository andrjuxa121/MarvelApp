package com.great.app.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.great.app.model.Character
import com.great.app.repository.Repository
import com.great.app.repository.RepositoryError
import com.great.app.utils.singleArgViewModelFactory
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _state = MutableLiveData<ViewModelState>()
    val state: LiveData<ViewModelState> = _state

    private val _characters = MutableLiveData<List<Character>?>()
    val characters: LiveData<List<Character>?> = _characters

    private val _character = MutableLiveData<Character?>()
    val character: LiveData<Character?> = _character

    fun areCharactersAvailable(): Boolean {
        return (characters.value != null)
    }

    fun loadCharacters() {
        withLoading {
            _characters.value = repository.loadCharacters()
        }
    }

    fun setCharacter(character: Character?) {
        _character.value = character
    }

    fun loadCharacter(id: Int) {
        withLoading {
            _character.value = repository.loadCharacter(id)
        }
    }

    private fun withLoading(
        block: suspend () -> Unit
    ) {
        viewModelScope.launch {
            try {
                _state.value = ViewModelState.Loading
                block()
            } catch (e: RepositoryError) {
                e.printStackTrace()
                _state.value = ViewModelState.Error(e.messageResId)
            } finally {
                _state.value = ViewModelState.Completed
            }
        }
    }

    companion object {
        val FACTORY = singleArgViewModelFactory(::MainViewModel)
    }
}