package com.great.app.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    protected fun showError(textResId: Int) {
        Toast.makeText(
            requireActivity(),
            textResId, Toast.LENGTH_SHORT
        ).show()
    }
}