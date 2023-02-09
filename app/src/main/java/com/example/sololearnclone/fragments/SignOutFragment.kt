package com.example.sololearnclone.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sololearnclone.R
import com.example.sololearnclone.databinding.FragmentSignOutBinding
import com.google.firebase.auth.FirebaseAuth

class SignOutFragment : Fragment() {
    private var binding: FragmentSignOutBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        auth.signOut()

        val sharedPref = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.clear()
        editor?.apply()

        findNavController().navigate(R.id.action_GalleryFragment_to_LoginFragment)

        return FragmentSignOutBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}