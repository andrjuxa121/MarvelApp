package com.great.testapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.great.testapp.R
import com.great.testapp.model.Character
import com.great.testapp.adapter.ListAdapter
import com.great.testapp.view_model.SharedViewModel


class ListFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View?{

        val layView = inflater.inflate(R.layout.fragment_list, container, false)

        val layManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        val recView: RecyclerView = layView.findViewById(R.id.RecView)
        recView.layoutManager = layManager
        recView.setHasFixedSize(true)

        val sharedModel: SharedViewModel by activityViewModels()
        sharedModel.getCharacters().observe(requireActivity(), { characters ->
            if(!isAdded) return@observe // if fragment no added to an activity

            val listAdapter = ListAdapter(requireActivity(), characters)
            listAdapter.notifyDataSetChanged()
            listAdapter.setListener(object: ListAdapter.iListener {
                override fun onClick(character: Character) {
                    sharedModel.apply {
                        setCharacter(character)
                        setPortPage(SharedViewModel.Pages.DETAILS_PAGE)
                    }
                }
            })
            recView.adapter = listAdapter
        })
        return layView
    }


}