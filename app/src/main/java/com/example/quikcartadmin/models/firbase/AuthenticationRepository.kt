package com.example.quikcartadmin.models.firbase

import com.example.quikcartadmin.models.entities.user.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepository @Inject constructor() :IAuthenticationRepository {

    private val auth: FirebaseAuth = Firebase.auth
    override suspend fun signInWithEmailAndPassword(user: User): Boolean {
        return try {
            auth.signInWithEmailAndPassword(user.email, user.password).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}