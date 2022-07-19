package com.great.app.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.great.app.view_model.RepoViewModel

open class BaseFragment : Fragment() {

    protected val repoViewModel: RepoViewModel by activityViewModels()

    protected val responseListener: RepoViewModel.IResponseListener

    init {
        responseListener = object : RepoViewModel.IResponseListener {
            override fun onSuccess() {}

            override fun onFailure(messageResId: Int) {
                showToast(messageResId)
            }
        }
    }

    protected fun showToast(textResId: Int) {
        Toast.makeText(
            requireActivity(),
            textResId, Toast.LENGTH_SHORT
        ).show()
    }
}