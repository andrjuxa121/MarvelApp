package com.great.app.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.great.app.model.Character

class SharedViewModel: ViewModel() {
    // Active page in portrait mode
    enum class Pages {
        LIST_PAGE, DETAILS_PAGE
    }
    private val characters = MutableLiveData<List<Character>>()
    private var selectedCharacter = MutableLiveData<Character?>()
    private var portPage = MutableLiveData<Pages>()

    init {
        portPage.value = Pages.LIST_PAGE
    }

    fun setPortPage(page: Pages) {
        portPage.value = page
    }
    fun getPortPage(): MutableLiveData<Pages> {
        return portPage
    }
    fun setCharacters(characters: List<Character>) {
        this.characters.value = characters
    }
    fun getCharacters(): MutableLiveData<List<Character>> {
        return characters
    }
    fun selectCharacter(character: Character?) {
        selectedCharacter.value = character
    }
    fun getSelectedCharacter(): MutableLiveData<Character?> {
        return selectedCharacter
    }
}