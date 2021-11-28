package com.great.app.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.great.app.AppActivity
import com.great.app.R
import com.great.app.adapter.ListAdapter
import com.great.app.model.Character
import com.great.app.repository.RepoViewModel


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
        initRecycleView(layView)
        initRefreshLayout(layView)
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
            refreshLayout.isRefreshing = true;
        }
    }

    private fun subscribeOnViewModel() {
        repoViewModel.characters.observe(viewLifecycleOwner, { nullableCharacters ->
            if (nullableCharacters == null) {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.no_response), Toast.LENGTH_SHORT
                ).show()
                return@observe
            }
            val listAdapter = ListAdapter(requireActivity(), nullableCharacters)
            listAdapter.notifyDataSetChanged()
            listAdapter.initCharacterClickListener(
                object : ListAdapter.ICharacterClickListener {
                    override fun onClick(character: Character) {
                        repoViewModel.setCharacter(character)
                        openDetailsFragment()
                    }
                })
            recyclerView.adapter = listAdapter
            refreshLayout.isRefreshing = false;
        })
    }

    private fun openDetailsFragment() {
        val detailsFragment = DetailsFragment()
        (requireActivity() as AppActivity).openFragment(detailsFragment)
    }
}