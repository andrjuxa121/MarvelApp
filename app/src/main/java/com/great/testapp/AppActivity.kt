package com.great.testapp


import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.great.testapp.fragment.EmptyFragment
import com.great.testapp.fragment.DetailsFragment
import com.great.testapp.fragment.ListFragment
import com.great.testapp.model.Character
import com.great.testapp.retrofit.RetroBuilder
import com.great.testapp.retrofit.RetroService
import com.great.testapp.view_model.SharedViewModel
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppActivity: AppCompatActivity() {
    private val BASE_URL = "https://www.simplifiedcoding.net/demos/"
    private lateinit var Service: RetroService
    private lateinit var Dialog: AlertDialog

    val SharedModel: SharedViewModel by viewModels()
    private lateinit var ListFrag: ListFragment
    private lateinit var DetailsFrag: DetailsFragment
    private lateinit var EmptyFrag: EmptyFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity)

        SharedModel.getPortPage().observe(this, { portPage ->
            updatePage(portPage)
        })
        initFragments()

        Service = RetroBuilder.getInstance(BASE_URL).create(RetroService::class.java)
        Dialog = SpotsDialog.Builder().
            setCancelable(true).
            setContext(this).
            build()

        val refreshButton: LinearLayout = findViewById(R.id.RefreshButton)
        refreshButton.setOnClickListener {
            loadCharacters()
        }
    }

    override fun onBackPressed() {
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if(SharedModel.getPortPage().value == SharedViewModel.Pages.DETAILS_PAGE) {
                updatePage(SharedViewModel.Pages.LIST_PAGE)
                return
            }
        }
        super.onBackPressed()
    }

    private fun initFragments() {
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ListFrag = ListFragment()
            DetailsFrag = DetailsFragment()
            updatePage(SharedViewModel.Pages.LIST_PAGE)
            return
        }
        ListFrag = supportFragmentManager.findFragmentById(R.id.ListFrag) as ListFragment
        DetailsFrag = supportFragmentManager.findFragmentById(R.id.DetailsFrag) as DetailsFragment
        EmptyFrag = EmptyFragment()
    }

    private fun updatePage(portPage: SharedViewModel.Pages) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.MainLay, getPageFragment(portPage))
            //.addToBackStack(null)
            .commit()
    }
    private fun getPageFragment(portPage: SharedViewModel.Pages): Fragment {
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            return when (portPage) {
                SharedViewModel.Pages.LIST_PAGE -> ListFrag
                SharedViewModel.Pages.DETAILS_PAGE -> DetailsFrag
            }
        }
        return EmptyFrag
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
                SharedModel.setCharacters(characters)
                Dialog.dismiss()
            }
        })
    }
}