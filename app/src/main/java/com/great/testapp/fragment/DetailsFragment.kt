package com.great.testapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.great.testapp.R
import com.great.testapp.view_model.SharedViewModel

class DetailsFragment: Fragment() {
    val SharedModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View?{

        return inflater.inflate(R.layout.fragment_details, container, false)
    }
}