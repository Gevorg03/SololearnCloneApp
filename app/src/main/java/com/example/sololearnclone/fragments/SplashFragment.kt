package com.example.sololearnclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.sololearnclone.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class SplashFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            if (auth.currentUser != null)
                lifecycleScope.launchWhenCreated {
                    findNavController().navigate(R.id.action_SplashFragment_to_HomeFragment)
                }
            else {
                lifecycleScope.launchWhenCreated {
                    findNavController().navigate(R.id.action_SplashFragment_to_LoginFragment)
                }
            }
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