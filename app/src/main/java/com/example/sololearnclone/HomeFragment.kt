package com.example.sololearnclone

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.sololearnclone.databinding.FragmentHomeBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class HomeFragment : Fragment() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var binding: FragmentHomeBinding? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth
    private lateinit var viewData:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        viewData = inflater.inflate(R.layout.activity_main, null)
        val navigationView: NavigationView = viewData.findViewById(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val tvFullName = headerView.findViewById<View>(R.id.tv_fullname) as TextView
        tvFullName.text = "Your Text Here"
//        coroutineScope.launch {
//            getUserInfo(inflater)
//        }

        return FragmentHomeBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private suspend fun getUserInfo(inflater: LayoutInflater) = suspendCoroutine { continuation ->
        val navigationView: NavigationView = viewData.findViewById(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val tvFullName = headerView.findViewById<View>(R.id.tv_fullname) as TextView
        tvFullName.text = "Your Text Here"
//        val view = inflater.inflate(R.layout.nav_header_main, null)

        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("users")
        ref.addValueEventListener(object : ValueEventListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                val data:HashMap<String, String> = snapshot.value as HashMap<String, String>
                val a = data[auth.uid] as HashMap<String, String>
                println(a["username"])
                val username = a["username"]
                tvFullName.text = "Username: $username"
                continuation.resume(username)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}



















//val view = inflater.inflate(R.layout.activity_main, null)
//        val drawerLayout: DrawerLayout = view.findViewById(R.id.drawer_layout)
//        val navView: NavigationView = view.findViewById(R.id.nav_view)
//        val navHostFragment =
//            activity?.supportFragmentManager?.findFragmentById(R.id.fragmnet_container) as NavHostFragment
//        val navController = navHostFragment.navController
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
//            ), drawerLayout
//        )
//        setupActionBarWithNavController(
//            (activity as AppCompatActivity?)!!,
//            navController,
//            appBarConfiguration
//        )
//        navView.setupWithNavController(navController)