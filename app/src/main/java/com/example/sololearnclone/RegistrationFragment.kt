package com.example.sololearnclone

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

//@AndroidEntryPoint
class RegistrationFragment : Fragment(), ClickedChange {
    private var binding: FragmentRegistrationBinding? = null

    lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private var isFailed: Boolean = false

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

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        binding?.run {
            ccp.registerCarrierNumberEditText(etPhone)

            etFullname.isClickedChange(layoutFullname)
            etEmail.isClickedChange(layoutEmail)
            etPhone.isClickedChange(layoutPhone)
            etPass.isClickedChange(layoutPass)

            etFullname.doOnTextChanged { text, _, _, _ ->
                if (text.toString().length < 5) {
                    layoutFullname.helperText = null
                    layoutFullname.error = "Minimum length must be at least 5"
                } else {
                    layoutFullname.helperText = "Strong username"
                    layoutFullname.error = null
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

            etPhone.doOnTextChanged { text, _, _, _ ->
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

            tvLogin.setOnClickListener {
                findNavController().navigate(R.id.action_RegistrationFragment_to_LoginFragment)
            }
            btnRegister.setOnClickListener {
                isFailed = false
                isFieldEmpty(layoutFullname)
                isFieldEmpty(layoutEmail)
                isFieldEmpty(layoutPhone)
                isFieldEmpty(layoutPass)

                if (!isFailed) {
                    progressBar.visibility = View.VISIBLE
                    val phone =
                        "${ccp.selectedCountryCodeWithPlus}${etPhone.text.toString()}"
                    val db = FirebaseDatabase.getInstance()
                    val ref = db.getReference("users")
                    ref.addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (!dataSnapshot.value.toString().contains(phone)) {
                                auth.fetchSignInMethodsForEmail(etEmail.text.toString())
                                    .addOnCompleteListener { task: Task<SignInMethodQueryResult> ->
                                        val isNewUser =
                                            task.result.signInMethods?.isEmpty()
                                        println(isNewUser)
                                        if (isNewUser == true) {
                                            //existEmail(etEmail.text.toString())
                                            auth.createUserWithEmailAndPassword(
                                                etEmail.text.toString(),
                                                etPass.text.toString()
                                            )
                                                .addOnCompleteListener { task: Task<AuthResult> ->
                                                    if (task.isSuccessful) {
                                                        val uid = auth.currentUser?.uid
                                                        databaseReference.child("users")
                                                            .child(uid.toString())
                                                            .child("username")
                                                            .setValue(etFullname.text.toString())
                                                        databaseReference.child("users")
                                                            .child(uid.toString())
                                                            .child("phone")
                                                            .setValue(phone)
                                                        val us =
                                                            FirebaseAuth.getInstance().currentUser
                                                        us?.sendEmailVerification()
                                                        etFullname.clearData(layoutFullname)
                                                        etEmail.clearData(layoutEmail)
                                                        etPhone.clearData(layoutPhone)
                                                        etPass.clearData(layoutPass)
                                                        auth.signOut()
                                                        findNavController().navigate(R.id.action_RegistrationFragment_to_LoginFragment)
                                                        Toast.makeText(
                                                            requireContext(),
                                                            "Successfully registered, please, check your email end verify to login",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else {
                                                        layoutEmail.requestFocus()
                                                        layoutEmail.error =
                                                            "Invalid Email"
                                                        layoutEmail.helperText = null
                                                    }
                                                }
                                        } else {
                                            layoutEmail.requestFocus()
                                            layoutEmail.error =
                                                "The email already exists"
                                            layoutEmail.helperText = null
                                        }
                                    }
                            } else {
                                layoutPhone.requestFocus()
                                layoutPhone.error =
                                    "The phone already exists"
                                layoutPhone.helperText = null
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
                progressBar.visibility = View.GONE
            }
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
            password.length < 8 -> "Min len is "
            password.firstOrNull { it.isDigit() } == null -> "It must have at least 1 digit"
            password.filter { it.isLetter() }
                .firstOrNull { it.isUpperCase() } == null -> "It must have at least 1 uppercase "
            password.filter { it.isLetter() }
                .firstOrNull { it.isLowerCase() } == null -> "It must have at least 1 lowercase"
            password.firstOrNull { !it.isLetterOrDigit() } == null -> "It must have one special character like"
            else -> "Ok"
        }
    }

    fun showKeyboard(et: TextInputEditText) {
        et.requestFocus()
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        //imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun existEmail(email: String) {
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task: Task<SignInMethodQueryResult> ->
                val isNewUser = task.result.signInMethods?.isNotEmpty()
                println(isNewUser)
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
