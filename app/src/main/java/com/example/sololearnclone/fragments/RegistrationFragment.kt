package com.example.sololearnclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sololearnclone.ClickedChange
import com.example.sololearnclone.R
import com.example.sololearnclone.databinding.FragmentRegistrationBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegistrationFragment : Fragment(), ClickedChange {
    private var binding: FragmentRegistrationBinding? = null
    lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private var isFailed: Boolean = false //if there is any field that empty

    private val databaseReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://sololearnclone-default-rtdb.firebaseio.com/")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        return FragmentRegistrationBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.run {
            ccp.registerCarrierNumberEditText(etPhone)

            etFullName.isClickedChange(layoutFullName)
            etEmail.isClickedChange(layoutEmail)
            etPhone.isClickedChange(layoutPhone)
            etPass.isClickedChange(layoutPass)

            etFullName.doOnTextChanged { text, _, _, _ ->
                if (text.toString().length < 5) {
                    layoutFullName.helperText = null
                    layoutFullName.error = "Minimum length must be at least 5"
                } else if (text.toString().isBlank()) {
                    layoutFullName.helperText = null
                    layoutFullName.error = "The username can not be empty"
                } else {
                    layoutFullName.helperText = "Strong username"
                    layoutFullName.error = null
                }
            }

            etEmail.doAfterTextChanged { text ->
                if (text.toString().trim().matches(emailPattern.toRegex()) && text.toString()
                        .isNotEmpty()
                ) {
                    layoutEmail.helperText = "Valid Email"
                    layoutEmail.error = null
                } else {
                    layoutEmail.helperText = null
                    layoutEmail.error = "Invalid email"
                }
            }

            etPhone.doOnTextChanged { _, _, _, _ ->
                if (!ccp.isValidFullNumber) {
                    layoutPhone.helperText = null
                    layoutPhone.error = "Invalid phone number"
                } else {
                    layoutPhone.helperText = "Valid phone number"
                    layoutPhone.error = null
                }
            }

            etPass.doOnTextChanged { text, _, _, _ ->
                if (isValidPassword(text.toString()) != "Ok") {
                    layoutPass.helperText = null
                    layoutPass.error = isValidPassword(text.toString())
                } else {
                    layoutPass.helperText = "Strong password"
                    layoutPass.error = null
                }
            }

            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_RegistrationFragment_to_LoginFragment)
            }

            btnRegister.setOnClickListener {
                isFailed = false
                isFieldEmpty(layoutFullName)
                isFieldEmpty(layoutEmail)
                isFieldEmpty(layoutPhone)
                isFieldEmpty(layoutPass)

                if (!isFailed) {
                    progressBar.visibility = View.VISIBLE
                    val phone =
                        "${ccp.selectedCountryCodeWithPlus}${etPhone.text.toString()}"
                    val db = FirebaseDatabase.getInstance()
                    val ref = db.getReference("users")

                    ref.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (!dataSnapshot.value.toString().contains(phone)) {
                                if (etEmail.text.toString().isNotEmpty()) {
                                    auth.fetchSignInMethodsForEmail(etEmail.text.toString())
                                        .addOnCompleteListener { task: Task<SignInMethodQueryResult> ->
                                            if (task.isSuccessful) {
                                                val result = task.result

                                                if ((result?.signInMethods?.size ?: 0) > 0) {
                                                    progressBar.visibility = View.GONE
                                                    layoutEmail.requestFocus()
                                                    layoutEmail.error =
                                                        "The email already exists"
                                                    layoutEmail.helperText = null
                                                } else {
                                                    auth.createUserWithEmailAndPassword(
                                                        etEmail.text.toString(),
                                                        etPass.text.toString()
                                                    )
                                                        .addOnCompleteListener { taskRes: Task<AuthResult> ->
                                                            if (taskRes.isSuccessful) {
                                                                val uid = auth.currentUser?.uid
                                                                //add data in firebase database
                                                                databaseReference.child("users")
                                                                    .child(uid.toString())
                                                                    .child("username")
                                                                    .setValue(etFullName.text.toString())
                                                                databaseReference.child("users")
                                                                    .child(uid.toString())
                                                                    .child("phone")
                                                                    .setValue(phone)
                                                                val us =
                                                                    FirebaseAuth.getInstance().currentUser
                                                                us?.sendEmailVerification()

                                                                etFullName.clearData(layoutFullName)
                                                                etEmail.clearData(layoutEmail)
                                                                etPhone.clearData(layoutPhone)
                                                                etPass.clearData(layoutPass)

                                                                auth.signOut()
                                                                findNavController().navigate(R.id.action_RegistrationFragment_to_LoginFragment)
                                                                Toast.makeText(
                                                                    requireContext(),
                                                                    "Successfully registered, please, check your email and verify to login",
                                                                    Toast.LENGTH_LONG
                                                                ).show()
                                                            } else {
                                                                progressBar.visibility = View.GONE
                                                                layoutEmail.requestFocus()
                                                                layoutEmail.error =
                                                                    "Invalid Email"
                                                                layoutEmail.helperText = null
                                                            }
                                                        }
                                                }
                                            } else {
                                                progressBar.visibility = View.GONE
                                            }
                                        }
                                        .addOnFailureListener {
                                            progressBar.visibility = View.GONE
                                            Toast.makeText(
                                                requireContext(),
                                                "The registration failed. Please, try again",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                            } else {
                                progressBar.visibility = View.GONE
                                layoutPhone.requestFocus()
                                layoutPhone.error =
                                    "The phone already exists"
                                layoutPhone.helperText = null
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "The registration failed. Please, try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            }
            progressBar.visibility = View.GONE
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun TextInputEditText.clearData(layout: TextInputLayout) {
        this.text = null
        layout.helperText = null
        layout.error = null
    }

    private fun isValidPassword(password: String): String {
        return when {
            password.length < 8 -> "The minimum length must be at least 8"
            password.firstOrNull { it.isDigit() } == null -> "It must have at least 1 digit"
            password.filter { it.isLetter() }
                .firstOrNull { it.isUpperCase() } == null -> "It must have at least 1 uppercase "
            password.filter { it.isLetter() }
                .firstOrNull { it.isLowerCase() } == null -> "It must have at least 1 lowercase"
            password.firstOrNull { !it.isLetterOrDigit() } == null -> "It must have one special character"
            else -> "Ok"
        }
    }

    private fun isFieldEmpty(textInputLayout: TextInputLayout) {
        if (textInputLayout.error == null && textInputLayout.helperText == null) {
            textInputLayout.error = "The field can not be empty"
            textInputLayout.helperText = null
            isFailed = true
        }
    }
}