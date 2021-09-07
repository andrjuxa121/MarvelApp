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
import com.great.testapp.retrofit.RetroBuilder
import com.great.testapp.retrofit.RetroService
import com.great.testapp.utils.Constant
import com.great.testapp.view_model.SharedViewModel
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AppActivity: AppCompatActivity() {
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

        Service = RetroBuilder.getInstance(Constant.BASE_URL).create(RetroService::class.java)
        Dialog = SpotsDialog.Builder()
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
        lockOrientation() // no rotation on loading
        Dialog.show()
        Service.getCharacters().enqueue(object: Callback<DataWrapper> {
            override fun onFailure(call: Call<DataWrapper>, t: Throwable) {
                Toast.makeText(this@AppActivity,
                    getString(R.string.NoResponse), Toast.LENGTH_LONG).show()
                Dialog.dismiss()
                unlockOrientation()
            }
            override fun onResponse(
                call: Call<DataWrapper?>,
                response: Response<DataWrapper?>) {

                response.body()?.let { dataWrapper ->
                    val characters = dataWrapper.data!!.results!!
                    SharedModel.setCharacters(characters)
                    Dialog.dismiss()
                    unlockOrientation()
                }
            }
        })
    }
    private fun loadCharacter(id: Int) {
        lockOrientation() // no rotation on loading
        Dialog.show()
        Service.getCharacter(id).enqueue(object: Callback<DataWrapper> {
            override fun onFailure(call: Call<DataWrapper>, t: Throwable) {
                Toast.makeText(this@AppActivity,
                    getString(R.string.NoResponse), Toast.LENGTH_LONG).show()
                Dialog.dismiss()
                unlockOrientation()
            }
            override fun onResponse(
                call: Call<DataWrapper>,
                response: Response<DataWrapper>) {

                val dataWrapper = response.body()
                val character = dataWrapper?.data?.results?.get(0)
                SharedModel.setCharacter(character)

                Dialog.dismiss()
                unlockOrientation()
                SharedModel.setPortPage(SharedViewModel.Pages.DETAILS_PAGE)
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