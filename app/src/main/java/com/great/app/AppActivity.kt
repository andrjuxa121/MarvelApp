package com.great.app


import android.app.AlertDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.Surface
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.great.app.fragment.DetailsFragment
import com.great.app.fragment.ListFragment
import com.great.app.repository.RepoViewModel


class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity)
        setMainPage(savedInstanceState)

//        repoViewModel.getPortPage().observe(this, { portPage ->
//            updatePage(portPage)
//        })
//        initFragments()
//
//        apiService = RetrofitBuilder.getRetrofit(Constant.BASE_URL).create(ApiService::class.java)
//        dialog = SpotsDialog.Builder()
//            .setCancelable(true)
//            .setContext(this)
//            .build()
//
//        val refreshButton: LinearLayout = findViewById(R.id.RefreshButton)
//        refreshButton.setOnClickListener {
//            loadCharacters()
//        }
//        val input: EditText = findViewById(R.id.edt_search)
//        val searchButton: ImageView = findViewById(R.id.btn_search)
//        searchButton.setOnClickListener {
//            loadCharacter(input.text.toString().toInt())
//        }
    }

//    override fun onBackPressed() {
//        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            if (repoViewModel.getPortPage().value == SharedViewModel.Pages.DETAILS_PAGE) {
//                updatePage(SharedViewModel.Pages.LIST_PAGE)
//                return
//            }
//        }
//        super.onBackPressed()
//    }

    fun openFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        supportFragmentManager.fragments.forEach { oldFragment ->
            fragmentTransaction.remove(oldFragment)
        }
        fragmentTransaction.add(R.id.lay_main, fragment).commit()
    }

    private fun setMainPage(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                val listFragment = ListFragment()
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.lay_main, listFragment)
                    .commit()
            } else {
                val listFragment = ListFragment()
                val detailsFragment = DetailsFragment()
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.lay_main, listFragment)
                    .add(R.id.lay_main, detailsFragment)
                    .commit()
            }
        }
    }

//    private fun loadCharacters() {
//        lockOrientation() // no rotation on loading
//        dialog.show()
//        apiService.getCharacters().enqueue(object : Callback<DataWrapper> {
//            override fun onFailure(call: Call<DataWrapper>, t: Throwable) {
//                Toast.makeText(
//                    this@AppActivity,
//                    getString(R.string.NoResponse), Toast.LENGTH_LONG
//                ).show()
//                dialog.dismiss()
//                unlockOrientation()
//            }
//
//            override fun onResponse(
//                call: Call<DataWrapper?>,
//                response: Response<DataWrapper?>
//            ) {
//
//                response.body()?.let { dataWrapper ->
//                    val characters = dataWrapper.data!!.results!!
//                    repoViewModel.setCharacters(characters)
//                    dialog.dismiss()
//                    unlockOrientation()
//                }
//            }
//        })
//    }
//
//    private fun loadCharacter(id: Int) {
//        lockOrientation() // no rotation on loading
//        dialog.show()
//        apiService.getCharacter(id).enqueue(object : Callback<DataWrapper> {
//            override fun onFailure(call: Call<DataWrapper>, t: Throwable) {
//                Toast.makeText(
//                    this@AppActivity,
//                    getString(R.string.NoResponse), Toast.LENGTH_LONG
//                ).show()
//                dialog.dismiss()
//                unlockOrientation()
//            }
//
//            override fun onResponse(
//                call: Call<DataWrapper>,
//                response: Response<DataWrapper>
//            ) {
//
//                val dataWrapper = response.body()
//                val character = dataWrapper?.data?.results?.get(0)
//                repoViewModel.selectCharacter(character)
//
//                dialog.dismiss()
//                unlockOrientation()
//                repoViewModel.setPortPage(SharedViewModel.Pages.DETAILS_PAGE)
//            }
//        })
//    }

    private fun lockOrientation() {
        val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val rotation: Int = display.rotation
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
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