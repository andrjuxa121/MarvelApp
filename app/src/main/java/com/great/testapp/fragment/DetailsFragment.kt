package com.great.testapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.great.testapp.R
import com.great.testapp.utils.Formater
import com.great.testapp.view_model.SharedViewModel
import com.squareup.picasso.Picasso

class DetailsFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View?{

        val layView = inflater.inflate(R.layout.fragment_details, container, false)
        val image: ImageView = layView.findViewById(R.id.Image)
        val id: TextView = layView.findViewById(R.id.Id)
        val name: TextView = layView.findViewById(R.id.Name)
        val description: TextView = layView.findViewById(R.id.Description)
        val comics: TextView = layView.findViewById(R.id.Comics)

        val sharedModel: SharedViewModel by activityViewModels()
        sharedModel.getCharacter().observe(requireActivity(), { character ->
            Picasso.get().load(Formater.getImageUrl(character.thumbnail)).into(image)
            id.setText(character.id.toString())
            name.setText(character.name)
            description.setText(character.description)
            var nameOfComics = ""
            character.comics?.items?.forEachIndexed { index, item ->
                nameOfComics += "${index+1}. ${item.name}\n"
            }
            comics.setText(nameOfComics)
        })
        return layView
    }
}