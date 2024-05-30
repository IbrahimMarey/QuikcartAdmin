package com.example.quikcartadmin.ui.auth.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
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
            Toast.makeText(this, "Some thing wrong in signIn, ensure about your mail", Toast.LENGTH_SHORT).show()
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
                                AuthHelper.setUserSignedIn(this@AuthenticationActivity, true)
                                Toast.makeText(this@AuthenticationActivity, "Sign in successful", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@AuthenticationActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@AuthenticationActivity, "Sign in failed. Please try again.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        is UiState.Failed -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@AuthenticationActivity, "Error: ${state.msg}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, "Invalid email or password. Please check your input.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }
}
