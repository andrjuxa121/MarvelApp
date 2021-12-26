package com.great.app.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class Keyboard {
    companion object{
        fun show(edit: EditText) {
            edit.requestFocus()
            val imManager = edit.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imManager.showSoftInput(edit, InputMethodManager.SHOW_FORCED) // SHOW_IMPLICIT
        }

        fun hide(activity: Activity) {
            activity.currentFocus?.let { view ->
                val imManager = activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imManager.hideSoftInputFromWindow(view.windowToken, 0);
            }
        }
    }
}