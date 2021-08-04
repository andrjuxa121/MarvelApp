package com.great.testapp.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.great.testapp.model.Character

class SharedViewModel: ViewModel() {
    private val Characters = MutableLiveData<List<Character>>()
    private var SelectedNumber = 0 // номер вибраного елемента списку

    fun saveCharacters(characters: List<Character>) {
        Characters.value = characters
    }
    fun getCharacters(): List<Character>? {
        return Characters.value
    }
    fun selectCharacter(number: Int) {
        SelectedNumber = number
    }
    fun getSelectedNumber(): Int {
        return SelectedNumber
    }
    fun getSelectedCharacter(): Character? {
        return getCharacters()?.get(SelectedNumber)
    }
}