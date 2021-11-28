package com.great.app.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.great.app.R
import com.great.app.utils.Formater
import com.great.app.view_model.SharedViewModel
import com.squareup.picasso.Picasso

class DetailsFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View?{

        val layView = inflater.inflate(R.layout.fragment_details, container, false)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layView.layoutParams = LinearLayout.LayoutParams(
                resources.getDimension(R.dimen.dp0).toInt(),    // width
                FrameLayout.LayoutParams.MATCH_PARENT,          // height
                50f                                       // weight
            )
        }
        return layView
    }

    override fun onViewCreated(layView: View, savedInstanceState: Bundle?) {
        val characterImage: ImageView = layView.findViewById(R.id.image)
        val characterId: TextView = layView.findViewById(R.id.id)
        val characterName: TextView = layView.findViewById(R.id.name)
        val characterDescription: TextView = layView.findViewById(R.id.description)
        val characterComics: TextView = layView.findViewById(R.id.comics)

        val sharedModel: SharedViewModel by activityViewModels()
        sharedModel.getSelectedCharacter().observe(viewLifecycleOwner, { nullCharacter ->
            nullCharacter?.let { character ->
                Picasso.get().load(Formater.getImageUrl(character.thumbnail)).into(characterImage)
                characterId.setText(character.id.toString())
                characterName.setText(character.name)
                characterDescription.setText(character.description)
                var nameOfComics = ""
                character.comics?.items?.forEachIndexed { index, item ->
                    nameOfComics += "${index+1}. ${item.name}\n"
                }
                characterComics.setText(nameOfComics)
                return@observe
            }
            characterImage.setImageResource(R.drawable.image_not_found)
            characterId.setText(getString(R.string.NoInformation))
            characterName.setText(getString(R.string.NoInformation))
            characterDescription.setText(getString(R.string.NoInformation))
            characterComics.setText(getString(R.string.NoInformation))
        })
    }
}