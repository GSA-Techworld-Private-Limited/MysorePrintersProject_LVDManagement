package com.example.mysoreprintersproject_lvdmanagement.profilefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.mysoreprintersproject_lvdmanagement.network.APIManager
import com.example.mysoreprintersproject_lvdmanagement.R
import com.example.mysoreprintersproject_lvdmanagement.network.SessionManager
import com.example.mysoreprintersproject_lvdmanagement.responses.ProfileResponses
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Response


class Profile_Fragment :Fragment(R.layout.fragment_profile_) {




    private lateinit var drawerLayout: DrawerLayout

    private lateinit var navigationView: NavigationView

    private lateinit var sessionManager: SessionManager

    private lateinit var profileName: TextView
    private lateinit var profileImage: ImageView
    private lateinit var nameEdit: EditText
    private lateinit var editPhoneNumber: EditText
    private lateinit var editEmail: EditText
    private lateinit var editLocation: EditText

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sessionManager=SessionManager(requireActivity())

        drawerLayout = requireView().findViewById(R.id.drawer_layout)
        navigationView = requireView().findViewById(R.id.navigationView)

        profileName=requireView().findViewById(R.id.profile_name)
        profileImage=requireView().findViewById(R.id.profile_image)
        nameEdit=requireView().findViewById(R.id.nameEdit)
        editPhoneNumber=requireView().findViewById(R.id.editPhoneNumber)
        editEmail=requireView().findViewById(R.id.editEmail)
        editLocation=requireView().findViewById(R.id.editLocation)


        setupNavigationView()

        getLVDProfile()
    }

    private fun setupNavigationView() {
        val navigationViewIcon: ImageView = requireView().findViewById(R.id.imageSettings)
        navigationViewIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
//                R.id.nav_dashboard -> startActivity(Intent(requireActivity(), HomeContainerActivity::class.java))
//                R.id.nav_attendance -> startActivity(Intent(requireActivity(), AttendanceActivity::class.java))
//                R.id.nav_work_summary -> startActivity(Intent(requireActivity(), DailyWorkingSummaryActivity::class.java))
//                R.id.nav_collections_performance -> startActivity(Intent(requireActivity(), CollectionPerformanceActivity::class.java))
//                R.id.nav_collection_summary -> startActivity(
//                    Intent(requireActivity(),
//                    CollectionSummaryReportActivity::class.java)
//                )
//                R.id.nav_collections_report -> startActivity(Intent(requireActivity(), DailyCollectionActivity::class.java))
//                R.id.nav_supply_reports -> startActivity(Intent(requireActivity(), SupplyReportActivity::class.java))
//                R.id.nav_net_sales_report -> startActivity(Intent(requireActivity(), NetSaleActivity::class.java))
//                R.id.nav_logout ->{
//                    sessionManager.logout()
//                    sessionManager.clearSession()
//                    startActivity(Intent(requireActivity(), SplashScreenActivity::class.java))
//                    requireActivity().finishAffinity()
//                }
//                else -> Log.d("NavigationDrawer", "Unhandled item clicked: ${item.itemId}")
            }
            drawerLayout.closeDrawers()
            true
        }
    }



    private fun getLVDProfile() {
        val serviceGenerator = APIManager.apiInterface
        val accessToken = sessionManager.fetchAuthToken()
        val authorization = "Bearer $accessToken"
        val id = sessionManager.fetchUserId()!!

        serviceGenerator.getProfileOfLVD(authorization, id.toInt())
            .enqueue(object : retrofit2.Callback<ProfileResponses> {
                override fun onResponse(call: Call<ProfileResponses>, response: Response<ProfileResponses>) {
                    val profileResponses = response.body()

                    if(profileResponses!=null){
                        profileName.text=profileResponses.name
                        val image=profileResponses.profileImage
                        val file= APIManager.getImageUrl(image!!)
                        Glide.with(requireActivity()).load(file).into(profileImage)

                        nameEdit.setText(profileResponses.name)
                        editEmail.setText(profileResponses.email)
                        editPhoneNumber.setText(profileResponses.phonenumber)
                        editLocation.setText(profileResponses.userLocation)
                    }
                }

                override fun onFailure(call: Call<ProfileResponses>, t: Throwable) {
                    Toast.makeText(requireActivity(), "Error fetching data", Toast.LENGTH_SHORT).show()
                }
            })
    }


}