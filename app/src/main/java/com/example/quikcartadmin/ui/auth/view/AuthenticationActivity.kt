package com.example.quikcartadmin.ui.auth.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.quikcartadmin.databinding.ActivityAuthenticationBinding
import com.example.quikcartadmin.helpers.AuthHelper
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.user.User
import com.example.quikcartadmin.ui.MainActivity
import com.example.quikcartadmin.ui.auth.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.SignInButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {

        val email = binding.UserNameTextField.editText?.text.toString()
        val password = binding.PasswordTextField.editText?.text.toString()

        if (!email.endsWith("@admin.com")) {
            errorAlert("Maybe your email and password is incorrect")
            return
        }

        if (isValidEmail(email) && isValidPassword(password)) {
            val user = User(email, password)
            lifecycleScope.launch {
                authViewModel.login(user)
                authViewModel.authState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val success = state.data
                            if (success) {
                                AuthHelper.setUserSignedIn(this@AuthenticationActivity, false)
                                val intent = Intent(this@AuthenticationActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                errorAlert("Sign in failed. Please try again.")
                            }
                        }
                        is UiState.Failed -> {
                            binding.progressBar.visibility = View.GONE
                            errorAlert("Failed SignIn")
                        }
                    }
                }
            }
        } else {
            errorAlert("Invalid email or password. Please check your input.")
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }



    private fun errorAlert(msg: String) {
        AlertDialog.Builder(this)
            .setTitle("Alert")
            .setMessage(msg)
            .setPositiveButton(
                "OK"
            ) { _, _ ->

            }
            .setNegativeButton("Cancel", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }
}

