package com.great.app.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.great.app.AppActivity
import com.great.app.R
import com.great.app.adapter.ListAdapter
import com.great.app.databinding.FragmentListBinding
import com.great.app.model.Character
import com.great.app.utils.Keyboard
import com.great.app.view_model.MainViewModel
import com.great.app.view_model.ViewModelState


class ListFragment : BaseFragment<FragmentListBinding>() {

    private val mainViewModel by activityViewModels<MainViewModel>()

    private var isSearchFieldOpen = false
    private val animationDuration: Long = 300 // ms

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListBinding {
        return FragmentListBinding.inflate(
            inflater, container, false
        )
    }

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        initViews()
        initListeners()
        observeData()

        if (!mainViewModel.areCharactersAvailable()) {
            mainViewModel.loadCharacters()
        }
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
            mainViewModel.loadCharacters()
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
            } else showError(R.string.empty_search_query)
            false // to hide keyboard
        }
    }

    private fun observeData() {
        mainViewModel.apply {
            state.observe(viewLifecycleOwner) { state ->
                when (state) {
                    ViewModelState.Loading -> showProgress(true)
                    is ViewModelState.Error -> showError(state.messageResId)
                    is ViewModelState.Completed -> showProgress(false)
                }
            }
            characters.observe(viewLifecycleOwner) { characters ->
                characters?.apply {
                    (requireActivity() as AppActivity).clearBackground()
                    updateUi(this)
                }
            }
            character.observe(viewLifecycleOwner) { character ->
                character?.apply { openDetailsFragment() }
            }
        }
    }

    private fun updateUi(characters: List<Character>) {
        val listAdapter = ListAdapter(requireActivity(), characters)
        listAdapter.notifyDataSetChanged()
        listAdapter.initCharacterClickListener(
            object : ListAdapter.ICharacterClickListener {
                override fun onClick(character: Character) {
                    mainViewModel.setCharacter(character)
                }
            })
        binding.recyclerView.adapter = listAdapter
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
        mainViewModel.loadCharacter(characterId)
    }

    private fun openDetailsFragment() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val detailsFragment = DetailsFragment()
            (requireActivity() as AppActivity).openFragment(detailsFragment)
        }
    }

    fun showProgress(value: Boolean) {
        binding.refreshLayout.isRefreshing = value
    }

    override fun onBackPressed() {
        Log.e("111", "OnBackPressed: $this")
    }
}