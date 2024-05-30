package com.example.quikcartadmin.models.firbase

import com.example.quikcartadmin.models.entities.user.User

interface IAuthenticationRepository {
    suspend fun signInWithEmailAndPassword(user: User): Boolean
}