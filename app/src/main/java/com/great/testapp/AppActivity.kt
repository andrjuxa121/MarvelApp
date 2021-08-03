package com.great.testapp

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.great.testapp.model.Character
import com.great.testapp.retrofit.RetroBuilder
import com.great.testapp.retrofit.RetroService
import com.great.testapp.view.ListAdapter
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppActivity: Activity() {
    private val BaseUrl = "https://www.simplifiedcoding.net/demos/"

    private lateinit var Service: RetroService
    private lateinit var RecView: RecyclerView
    private lateinit var LayManager: LinearLayoutManager
    private lateinit var Adapter: ListAdapter
    private lateinit var Dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity)

        Service = RetroBuilder.getInstance(BaseUrl).create(RetroService::class.java)

        RecView = findViewById(R.id.RecView)
        RecView.setHasFixedSize(true)

        LayManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        RecView.layoutManager = LayManager

        Dialog = SpotsDialog.Builder().
            setCancelable(true).
            setContext(this).
            build()

        val refreshButton: LinearLayout = findViewById(R.id.RefreshButton)
        refreshButton.setOnClickListener {
            loadCharacters()
        }
    }

    private fun loadCharacters() {
        Dialog.show()
        Service.getCharacters().enqueue(object: Callback<MutableList<Character>> {
            override fun onFailure(call: Call<MutableList<Character>>, t: Throwable) {
                Toast.makeText(this@AppActivity,
                    "Internet connection lost", Toast.LENGTH_LONG).show()
                Dialog.dismiss()
            }
            override fun onResponse(
                call: Call<MutableList<Character>>,
                response: Response<MutableList<Character>>) {

                val characters = response.body() as MutableList<Character>
                Adapter = ListAdapter(this@AppActivity, characters)
                Adapter.notifyDataSetChanged()
                RecView.adapter = Adapter

                Dialog.dismiss()
            }
        })
    }
}