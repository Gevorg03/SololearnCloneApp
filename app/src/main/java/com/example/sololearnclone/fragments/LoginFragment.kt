package com.example.sololearnclone.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sololearnclone.ClickedChange
import com.example.sololearnclone.R
import com.example.sololearnclone.databinding.FragmentLoginBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginFragment : Fragment(), ClickedChange {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var binding: FragmentLoginBinding? = null
    private lateinit var auth: FirebaseAuth
    private var isFailed: Boolean = false //if there is any field that empty
    private var username = ""
    private var phone = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()

        return FragmentLoginBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val drawerLayout: DrawerLayout? = activity?.findViewById(R.id.drawer_layout)
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setHomeButtonEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(false)

        binding?.run {
            etEmail.isClickedChange(layoutEmail)
            etPass.isClickedChange(layoutPass)

            etEmail.doOnTextChanged { text, _, _, _ ->
                if (text?.isNotEmpty() == true)
                    layoutEmail.error = null
            }

            etPass.doOnTextChanged { text, _, _, _ ->
                if (text?.isNotEmpty() == true)
                    layoutPass.error = null
            }

            tvForgotPassword.setOnClickListener {
                showRecoverPasswordDialog()
            }

            signUp.setOnClickListener {
                findNavController().navigate(R.id.action_LoginFragment_to_RegistrationFragment)
            }

            signIn.setOnClickListener {
                isFailed = false
                etEmail.isFieldEmpty(layoutEmail)
                etPass.isFieldEmpty(layoutPass)
                if (!isFailed && etEmail.text.toString().isNotEmpty() && etPass.text.toString()
                        .isNotEmpty()
                ) {
                    progressBar.visibility = View.VISIBLE
                    auth.signInWithEmailAndPassword(
                        etEmail.text.toString(),
                        etPass.text.toString()
                    )
                        .addOnCompleteListener { p0 ->
                            if (p0.isSuccessful) {
                                val user = FirebaseAuth.getInstance().currentUser
                                if (user?.isEmailVerified == true) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Successfully logged",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val sharedPref = activity?.getSharedPreferences(
                                        "userInfo",
                                        Context.MODE_PRIVATE
                                    )
                                    val editor = sharedPref?.edit()
                                    coroutineScope.launch {
                                        getUserInfo()
                                        editor?.putString("username", username)
                                        editor?.putString("phone", phone)
                                        editor?.apply()
                                    }

                                    findNavController().navigate(R.id.action_LoginFragment_to_HomeFragment)
                                } else {
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(
                                        requireContext(),
                                        "Please, check your email",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    auth.signOut()
                                    user?.sendEmailVerification()
                                }
                            } else {
                                progressBar.visibility = View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    "Wrong email or password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        .addOnFailureListener {
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Please, check your internet connection",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private suspend fun getUserInfo() = suspendCoroutine { continuation ->
        val navigationView: NavigationView? = activity?.findViewById(R.id.nav_view)
        val headerView = navigationView?.getHeaderView(0)
        val tvFullName = headerView?.findViewById<View>(R.id.tv_fullname) as TextView
        val tvPhone = headerView.findViewById<View>(R.id.tv_phone) as TextView

        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (auth.uid != null) {
                    var userInfo = snapshot.child(auth.uid.toString()).value.toString()
                    userInfo = userInfo.substring(1, userInfo.length - 1)

                    val map: Map<String, String> = userInfo.split(",").associate {
                        val (left, right) = it.split("=")
                        left to right
                    }
                    val data = map.values.toList()
                    phone = data[0]
                    username = data[1]

                    tvFullName.text = username
                    tvPhone.text = phone
                    continuation.resume(username)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Please, check your internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun TextInputEditText.isFieldEmpty(layout: TextInputLayout) {
        if (this.text.toString().isEmpty()) {
            layout.error = "The field can not be empty"
            isFailed = true
        }
    }

    private fun showRecoverPasswordDialog() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.forget_pass_dialog, null)
        val etEmail = view.findViewById<TextInputEditText>(R.id.et_email)
        val layoutEmail = view.findViewById<TextInputLayout>(R.id.layout_email)
        val btnReset = view.findViewById<Button>(R.id.btn_reset)
        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)

        val dialog = builder.create()
        dialog.setCancelable(false)

        btnReset.setOnClickListener {
            if (etEmail.text.toString().isEmpty()) {
                layoutEmail.error = "The field can not be empty"
            } else {
                val email = etEmail.text.toString()
                beginRecovery(email, dialog, layoutEmail)
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun beginRecovery(
        email: String,
        dialog: AlertDialog,
        layout: TextInputLayout
    ) {
        auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Password reset code is send",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                } else {
                    layout.error = "There is no email"
                }
            }
    }
}