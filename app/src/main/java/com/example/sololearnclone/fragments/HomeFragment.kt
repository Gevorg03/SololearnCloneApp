package com.example.sololearnclone.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sololearnclone.R
import com.example.sololearnclone.data.Course
import com.example.sololearnclone.adapters.HomeAdapter
import com.example.sololearnclone.databinding.FragmentHomeBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: HomeAdapter
    private var courseList: MutableList<Course> = mutableListOf()
    private lateinit var recyclerViewer: RecyclerView

    companion object {
        lateinit var phone_: String
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding?.run {
            recyclerViewer = recyclerView
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigationView: NavigationView? = activity?.findViewById(R.id.nav_view)
        val headerView = navigationView?.getHeaderView(0)
        val tvFullName = headerView?.findViewById(R.id.tv_fullname) as TextView
        val tvPhone = headerView.findViewById(R.id.tv_phone) as TextView

        val sharedPref = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val username = sharedPref?.getString("username", "")
        val phone = sharedPref?.getString("phone", "")

        tvFullName.text = username
        tvPhone.text = phone
        phone_ = phone.toString()

        val drawerLayout: DrawerLayout? = activity?.findViewById(R.id.drawer_layout)
        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.fragmnet_container) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_sign_out
            ), drawerLayout
        )
        setupActionBarWithNavController(
            (activity as AppCompatActivity),
            navController,
            appBarConfiguration
        )

        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

        recyclerViewer.layoutManager = LinearLayoutManager(context)
        courseList = mutableListOf()
        courseList.add(Course(R.drawable.kotlin_logo))
        adapter = HomeAdapter(courseList, findNavController())
        recyclerViewer.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
