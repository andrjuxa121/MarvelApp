package com.great.app.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.great.app.R
import com.great.app.databinding.FragmentDetailsBinding
import com.great.app.model.Character
import com.great.app.utils.Formater
import com.great.app.view_model.MainViewModel
import com.squareup.picasso.Picasso

class DetailsFragment : BaseFragment<FragmentDetailsBinding>() {

    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailsBinding {
        return FragmentDetailsBinding.inflate(
            inflater, container, false
        )
    }

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        mainViewModel.character.observe(viewLifecycleOwner) { character ->
            character?.apply { updateUi(this) }
        }
    }

    private fun updateUi(character: Character) {
        Picasso.get()
            .load(Formater.getImageUrl(character.thumbnail))
            .into(binding.imgCharacterProfile)
        binding.tvCharacterId.text = character.id.toString()
        binding.tvCharacterName.text = character.name
        binding.tvCharacterDescription.text = character.description
        var nameOfComics = ""
        character.comics?.items?.forEachIndexed { index, item ->
            nameOfComics += "${index + 1}. ${item.name}\n"
        }
        binding.tvCharacterInComics.text = nameOfComics
    }

    override fun onBackPressed() {
        Log.e("111", "OnBackPressed: $this")
        mainViewModel.setCharacter(null)
    }
}