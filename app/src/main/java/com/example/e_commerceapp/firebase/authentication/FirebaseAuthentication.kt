package com.example.e_commerceapp.firebase.authentication

import com.example.e_commerceapp.data.User
import com.example.e_commerceapp.firebase.database.UserDB
import com.example.e_commerceapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseAuthentication @Inject constructor(
    private val firebaseAuth: FirebaseAuth, private val userDB: UserDB
) {
    fun createAccountWithEmailAndPassword(
        user: User, password: String, onResult: (Resource<User>) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, password).addOnSuccessListener {
            it.user?.let { firebaseUser ->
                userDB.saveUser(firebaseUser.uid, user) { response ->
                    onResult(response)
                }
            }
        }.addOnFailureListener {
            onResult(Resource.Error(it.message.toString()))
        }
    }

    fun loginWithEmailAndPassword(
        email: String, password: String, onResult: (Resource<User>) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            it.user?.let { firebaseUser ->
                userDB.getUser(firebaseUser.uid) { response ->
                    onResult(response)
                }
            }
        }.addOnFailureListener {
            onResult(Resource.Error(it.message.toString()))
        }
    }

    fun forgotPassword(email: String, onResult: (Resource<String>) -> Unit) {
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
            onResult(
                Resource.Success("Password recovery mail sent")
            )
        }.addOnFailureListener { onResult(Resource.Error("Password recovery error")) }
    }
}