package com.great.testapp.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.great.testapp.model.Character

class SharedViewModel: ViewModel() {
    // Active page in portrait mode
    enum class Pages {
        LIST_PAGE, DETAILS_PAGE
    }
    private var PortPage = MutableLiveData<Pages>()
    private val Characters = MutableLiveData<List<Character>>()
    private var SelectedCharacter = MutableLiveData<Character>()
    init {
        PortPage.value = Pages.LIST_PAGE
    }

    fun setPortPage(page: Pages) {
        PortPage.value = page
    }
    fun getPortPage(): MutableLiveData<Pages> {
        return PortPage
    }
    fun setCharacters(characters: List<Character>) {
        Characters.value = characters
    }
    fun getCharacters(): MutableLiveData<List<Character>> {
        return Characters
    }
    fun selectCharacter(character: Character) {
        SelectedCharacter.value = character
    }
    fun getCharacter(): MutableLiveData<Character> {
        return SelectedCharacter
    }
}