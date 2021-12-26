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
import com.great.app.AppActivity
import com.great.app.R
import com.great.app.adapter.ListAdapter
import com.great.app.databinding.FragmentListBinding
import com.great.app.model.Character


class ListFragment : BaseFragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
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
        initViews()
        subscribeOnCharactersUpdate()

        if(!repoViewModel.areCharactersAvailable()) {
            repoViewModel.loadCharacters(responseListener)
            binding.refreshLayout.isRefreshing = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {
        val layManager = LinearLayoutManager(
            requireActivity(), RecyclerView.VERTICAL, false
        )
        binding.recyclerView.apply {
            layoutManager = layManager
            setHasFixedSize(true)
        }
        binding.refreshLayout.setOnRefreshListener {
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
        binding.recyclerView.adapter = listAdapter
        binding.refreshLayout.isRefreshing = false
    }

    private fun openDetailsFragment() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val detailsFragment = DetailsFragment()
            (requireActivity() as AppActivity).openFragment(detailsFragment)
        }
    }
}