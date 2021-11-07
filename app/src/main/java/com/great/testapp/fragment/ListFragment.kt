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

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(layView: View, savedInstanceState: Bundle?) {

        val layManager = LinearLayoutManager(
            requireActivity(), RecyclerView.VERTICAL, false)

        val recyclerView: RecyclerView = layView.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = layManager
        recyclerView.setHasFixedSize(true)

        val sharedModel: SharedViewModel by activityViewModels()
        sharedModel.getCharacters().observe(viewLifecycleOwner, { characters ->

            val listAdapter = ListAdapter(requireActivity(), characters)
            listAdapter.notifyDataSetChanged()
            listAdapter.initCharacterClickListener(object: ListAdapter.ICharacterClickListener {
                override fun onClick(character: Character) {
                    sharedModel.apply {
                        selectCharacter(character)
                        setPortPage(SharedViewModel.Pages.DETAILS_PAGE)
                    }
                }
            })
            recyclerView.adapter = listAdapter
        })
    }
}