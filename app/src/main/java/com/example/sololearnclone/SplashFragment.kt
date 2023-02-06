package com.example.sololearnclone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class SplashFragment() : Fragment() {
    lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            if (auth.currentUser != null)
                findNavController().navigate(R.id.action_SplashFragment_to_HomeFragment)
            else
                findNavController().navigate(R.id.action_SplashFragment_to_LoginFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}