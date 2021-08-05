package com.great.testapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.great.testapp.R
import com.great.testapp.view_model.SharedViewModel
import com.squareup.picasso.Picasso

class DetailsFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View?{

        val layView = inflater.inflate(R.layout.fragment_details, container, false)
        val image: ImageView = layView.findViewById(R.id.Image)
        val name: TextView = layView.findViewById(R.id.Name)
        val realname: TextView = layView.findViewById(R.id.Realname)
        val team: TextView = layView.findViewById(R.id.Team)
        val firstAppearance: TextView = layView.findViewById(R.id.FirstAppearance)
        val createdBy: TextView = layView.findViewById(R.id.CreatedBy)
        val bio: TextView = layView.findViewById(R.id.Bio)

        val sharedModel: SharedViewModel by activityViewModels()
        sharedModel.getCharacter().observe(requireActivity(), { character ->
            Picasso.get().load(character.imageurl).into(image)
            name.setText(character.name)
            realname.setText(character.realname)
            team.setText(character.team)
            firstAppearance.setText(character.firstappearance)
            createdBy.setText(character.createdby)
            bio.setText(character.bio)
        })
        return layView
    }
}