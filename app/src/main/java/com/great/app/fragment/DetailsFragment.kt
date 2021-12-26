package com.great.app.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.great.app.R
import com.great.app.databinding.FragmentDetailsBinding
import com.great.app.model.Character
import com.great.app.utils.Formater
import com.squareup.picasso.Picasso

class DetailsFragment : BaseFragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val rootView = binding.root

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rootView.layoutParams = LinearLayout.LayoutParams(
                resources.getDimension(R.dimen.dp0).toInt(),    // width
                FrameLayout.LayoutParams.MATCH_PARENT,          // height
                50f                                       // weight
            )
        }
        return rootView
    }

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        repoViewModel.character.observe(viewLifecycleOwner, { nullableCharacter ->
            updateUi(nullableCharacter)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(nullableCharacter: Character?) {
        nullableCharacter?.let { character ->
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
            return
        }
        binding.imgCharacterProfile.setImageResource(R.drawable.img_not_found)
        binding.tvCharacterId.setText(R.string.no_information)
        binding.tvCharacterName.setText(R.string.no_information)
        binding.tvCharacterDescription.setText(R.string.no_information)
        binding.tvCharacterInComics.setText(R.string.no_information)
    }
}