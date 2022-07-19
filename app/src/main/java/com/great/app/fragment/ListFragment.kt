package com.great.app.fragment

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import AppActivity
import com.great.app.R
import com.great.app.adapter.ListAdapter
import com.great.app.databinding.FragmentListBinding
import com.great.app.model.Character
import com.great.app.utils.Keyboard


class ListFragment : BaseFragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private var isSearchFieldOpen = false
    private val animationDuration: Long = 300 // ms

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
        initListeners()
        subscribeOnCharactersUpdate()

        if (!repoViewModel.areCharactersAvailable()) {
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
        setEnabled(false, binding.edtSearch)
    }

    private fun initListeners() {
        binding.refreshLayout.setOnRefreshListener {
            repoViewModel.loadCharacters(responseListener)
        }
        binding.btnSearch.setOnClickListener {
            if (!isSearchFieldOpen) {
                showSearchFiled()
            } else hideSearchFiled()
            isSearchFieldOpen = !isSearchFieldOpen
        }
        binding.edtSearch.setOnEditorActionListener { v, enterType, event ->
            val characterId = binding.edtSearch.text
            if (characterId.isNotEmpty()) {
                findCharacter(characterId.toString().toInt())
            } else showToast(R.string.empty_search_query)
            false // to hide keyboard
        }
    }

    private fun subscribeOnCharactersUpdate() {
        repoViewModel.characters.observe(viewLifecycleOwner) { nullableCharacters ->
            nullableCharacters?.let { characters ->
                (requireActivity() as AppActivity).clearBackground()
                updateUi(characters)
            }
        }
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

    private fun updateSearchFiledPivot() {
        // For measuring be sure the view was drawn
        binding.edtSearch.pivotX =
            binding.edtSearch.width - binding.btnSearch.width / 2f
    }

    private fun showSearchFiled() {
        // Set EditText enabled before show keyboard
        setEnabled(true, binding.edtSearch)
        Keyboard.show(binding.edtSearch)
        updateSearchFiledPivot()

        binding.btnSearch.backgroundTintList =
            requireContext().getColorStateList(R.color.c_white_dark)
        binding.btnSearch.setImageResource(R.drawable.ic_cancel)
        binding.btnSearch.animate()
            .rotation(90f)
            .duration = animationDuration

        binding.edtSearch.setText("")
        binding.edtSearch.animate()
            .scaleX(1f)
            .setInterpolator(AccelerateInterpolator())
            .duration = animationDuration
    }

    private fun hideSearchFiled() {
        Keyboard.hide(requireActivity())
        setEnabled(false, binding.edtSearch)

        binding.btnSearch.backgroundTintList =
            requireContext().getColorStateList(R.color.c_white_light)
        binding.btnSearch.setImageResource(R.drawable.ic_search)
        binding.btnSearch.animate()
            .rotation(0f)
            .duration = animationDuration

        binding.edtSearch.animate()
            .scaleX(0f)
            .setInterpolator(AccelerateInterpolator())
            .duration = animationDuration
    }

    private fun setEnabled(enabled: Boolean, edit: EditText) {
        edit.apply {
            isFocusable = enabled
            isFocusableInTouchMode = enabled
            isCursorVisible = enabled
            isLongClickable = enabled
        }
    }

    private fun findCharacter(characterId: Int) {
        Keyboard.hide(requireActivity())
        repoViewModel.loadCharacter(
            characterId,
            responseListener
        )
        openDetailsFragment()
    }

    private fun openDetailsFragment() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val detailsFragment = DetailsFragment()
            (requireActivity() as AppActivity).openFragment(detailsFragment)
        }
    }
}