package com.example.sololearnclone

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Nullable
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sololearnclone.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment(), ClickedChange {
    private var binding: FragmentLoginBinding? = null
    private lateinit var auth: FirebaseAuth
    private var isFailed: Boolean = false
    private var isDialogOpen: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()

        return FragmentLoginBinding.inflate(inflater, container, false)
            .also { binding = it }.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
                if (!isFailed) {
                    auth.signInWithEmailAndPassword(
                        etEmail.text.toString(),
                        etPass.text.toString()
                    )
                        .addOnCompleteListener { p0 ->
                            progressBar.visibility = View.VISIBLE
                            if (p0.isSuccessful) {
                                val user = FirebaseAuth.getInstance().currentUser
                                if (user?.isEmailVerified == true) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Successfully logged",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    findNavController().navigate(R.id.action_LoginFragment_to_HomeFragment)

                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Please, check your email",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    auth.signOut()
                                    user?.sendEmailVerification()
                                }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Wrong email or password",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                            progressBar.visibility = View.GONE
                        }
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)

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
        isDialogOpen = true
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
            isDialogOpen = false
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
                    isDialogOpen = false
                    Toast.makeText(requireContext(), "Done sent", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                   layout.error = "There is no email"
                }
            }
    }

}
