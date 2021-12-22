package com.great.app.fragment

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.great.app.AppActivity
import com.great.app.R
import com.great.app.adapter.ListAdapter
import com.great.app.databinding.FragmentListBinding
import com.great.app.model.Character


class ListFragment : BaseFragment() {

    private lateinit var binding: FragmentListBinding

    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        val rootView = binding.root

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rootView.layoutParams = LinearLayout.LayoutParams(
                resources.getDimension(R.dimen.dp0).toInt(),    // width
                LinearLayout.LayoutParams.MATCH_PARENT,         // height
                50f                                       // weight
            )
        }
        return rootView
    }

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        initRecycleView(rootView)
        initRefreshLayout(rootView)
        subscribeOnCharactersUpdate()

        repoViewModel.loadCharacters(responseListener)
        refreshLayout.isRefreshing = true;
    }

    private fun initRecycleView(rootView: View) {
        val layManager = LinearLayoutManager(
            requireActivity(), RecyclerView.VERTICAL, false
        )
        recyclerView = rootView.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = layManager
        recyclerView.setHasFixedSize(true)
    }

    private fun initRefreshLayout(rootView: View) {
        refreshLayout = rootView.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener {
            repoViewModel.loadCharacters(responseListener)
        }
    }

    private fun subscribeOnCharactersUpdate() {
        repoViewModel.characters.observe(viewLifecycleOwner, { nullableCharacters ->
            nullableCharacters?.let { characters ->
                updateUi(characters)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUi(characters: List<Character>) {
        val listAdapter = ListAdapter(requireActivity(), characters)
        listAdapter.notifyDataSetChanged()
        listAdapter.initCharacterClickListener(
            object : ListAdapter.ICharacterClickListener {
                override fun onClick(character: Character) {
                    repoViewModel.setCharacter(character)
                    openDetailsFragment()
                }
            })
        recyclerView.adapter = listAdapter
        refreshLayout.isRefreshing = false
    }

    private fun openDetailsFragment() {
        val detailsFragment = DetailsFragment()
        (requireActivity() as AppActivity).openFragment(detailsFragment)
    }
}