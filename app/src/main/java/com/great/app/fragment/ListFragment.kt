package com.great.app.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.great.app.R
import com.great.app.model.Character
import com.great.app.adapter.ListAdapter
import com.great.app.repository.RepoViewModel
import com.great.app.view_model.SharedViewModel


class ListFragment : Fragment() {
    private val repoViewModel: RepoViewModel by activityViewModels()

    private lateinit var refreshLayout: SwipeRefreshLayout;
    private lateinit var recyclerView: RecyclerView;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val layView = inflater.inflate(R.layout.fragment_list, container, false);

        // todo if error put in onViewCreated
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
        initRecycleView(layView);
        initRefreshLayout(layView);
    }

    private fun initRecycleView(layView: View) {
        val layManager = LinearLayoutManager(
            requireActivity(), RecyclerView.VERTICAL, false
        )
        recyclerView = layView.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = layManager
        recyclerView.setHasFixedSize(true)
    }

    private fun initRefreshLayout(layView: View) {
        refreshLayout = layView.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener {
            repoViewModel.loadCharacters()
        }
    }

    private fun subscribeOnViewModel() {
        repoViewModel.getCharacters().observe(viewLifecycleOwner, { nullableCharacters ->

            if (nullableCharacters == null) // todo
            val listAdapter = ListAdapter(requireActivity(), nullableCharacters)
            listAdapter.notifyDataSetChanged()
            listAdapter.initCharacterClickListener(object : ListAdapter.ICharacterClickListener {
                override fun onClick(character: Character) {
                    repoViewModel.apply {
                        selectCharacter(character)
                        setPortPage(SharedViewModel.Pages.DETAILS_PAGE)
                    }
                }
            })
            recyclerView.adapter = listAdapter
        })
    }
}