package com.great.app.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.great.app.R

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        val rootView = binding.root

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rootView.layoutParams = LinearLayout.LayoutParams(
                resources.getDimension(R.dimen.dp0).toInt(),                                    // width
                LinearLayout.LayoutParams.MATCH_PARENT,         // height
                50f                                      // weight
            )
        }
        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    protected fun showError(textResId: Int) {
        Toast.makeText(
            requireActivity(),
            textResId, Toast.LENGTH_SHORT
        ).show()
    }

    protected abstract fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB
    public abstract fun onBackPressed();
}