package com.great.app.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.great.app.view_model.MainViewModel

open class BaseFragment : Fragment() {

    protected val mainViewModel: MainViewModel by activityViewModels()

    protected val responseListener: MainViewModel.IResponseListener

    init {
        responseListener = object : MainViewModel.IResponseListener {
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