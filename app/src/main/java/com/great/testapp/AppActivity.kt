package com.great.testapp


import android.app.AlertDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.Surface
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.great.testapp.fragment.DetailsFragment
import com.great.testapp.fragment.EmptyFragment
import com.great.testapp.fragment.ListFragment
import com.great.testapp.model.DataWrapper
import com.great.testapp.retrofit.RetrofitBuilder
import com.great.testapp.retrofit.InterfaceAPI
import com.great.testapp.utils.Constant
import com.great.testapp.view_model.SharedViewModel
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AppActivity: AppCompatActivity() {
    private lateinit var apiService: InterfaceAPI
    private lateinit var dialog: AlertDialog

    val sharedModel: SharedViewModel by viewModels()
    private lateinit var listFragment: ListFragment
    private lateinit var detailsFragment: DetailsFragment
    private lateinit var emptyFragment: EmptyFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity)

        sharedModel.getPortPage().observe(this, { portPage ->
            updatePage(portPage)
        })
        initFragments()

        apiService = RetrofitBuilder.getRetrofit(Constant.BASE_URL).create(InterfaceAPI::class.java)
        dialog = SpotsDialog.Builder()
            .setCancelable(true)
            .setContext(this)
            .build()

        val refreshButton: LinearLayout = findViewById(R.id.RefreshButton)
        refreshButton.setOnClickListener {
            loadCharacters()
        }
        val input: EditText = findViewById(R.id.Input)
        val searchButton: ImageView = findViewById(R.id.SearchButton)
        searchButton.setOnClickListener {
            loadCharacter(input.text.toString().toInt())
        }
    }

    override fun onBackPressed() {
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if(sharedModel.getPortPage().value == SharedViewModel.Pages.DETAILS_PAGE) {
                updatePage(SharedViewModel.Pages.LIST_PAGE)
                return
            }
        }
        super.onBackPressed()
    }

    private fun initFragments() {
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            listFragment = ListFragment()
            detailsFragment = DetailsFragment()
            updatePage(SharedViewModel.Pages.LIST_PAGE)
            return
        }
        listFragment = supportFragmentManager.findFragmentById(R.id.ListFrag) as ListFragment
        detailsFragment = supportFragmentManager.findFragmentById(R.id.DetailsFrag) as DetailsFragment
        emptyFragment = EmptyFragment()
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
                SharedViewModel.Pages.LIST_PAGE -> listFragment
                SharedViewModel.Pages.DETAILS_PAGE -> detailsFragment
            }
        }
        return emptyFragment
    }

    private fun loadCharacters() {
        lockOrientation() // no rotation on loading
        dialog.show()
        apiService.getCharacters().enqueue(object: Callback<DataWrapper> {
            override fun onFailure(call: Call<DataWrapper>, t: Throwable) {
                Toast.makeText(this@AppActivity,
                    getString(R.string.NoResponse), Toast.LENGTH_LONG).show()
                dialog.dismiss()
                unlockOrientation()
            }
            override fun onResponse(
                call: Call<DataWrapper?>,
                response: Response<DataWrapper?>) {

                response.body()?.let { dataWrapper ->
                    val characters = dataWrapper.data!!.results!!
                    sharedModel.setCharacters(characters)
                    dialog.dismiss()
                    unlockOrientation()
                }
            }
        })
    }
    private fun loadCharacter(id: Int) {
        lockOrientation() // no rotation on loading
        dialog.show()
        apiService.getCharacter(id).enqueue(object: Callback<DataWrapper> {
            override fun onFailure(call: Call<DataWrapper>, t: Throwable) {
                Toast.makeText(this@AppActivity,
                    getString(R.string.NoResponse), Toast.LENGTH_LONG).show()
                dialog.dismiss()
                unlockOrientation()
            }
            override fun onResponse(
                call: Call<DataWrapper>,
                response: Response<DataWrapper>) {

                val dataWrapper = response.body()
                val character = dataWrapper?.data?.results?.get(0)
                sharedModel.selectCharacter(character)

                dialog.dismiss()
                unlockOrientation()
                sharedModel.setPortPage(SharedViewModel.Pages.DETAILS_PAGE)
            }
        })
    }

    private fun lockOrientation() {
        val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val rotation: Int = display.rotation
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            requestedOrientation =
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_270)
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                else ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            return
        }
        requestedOrientation =
            if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90)
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            else ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
    }
    private fun unlockOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}