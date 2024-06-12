package com.example.e_commerceapp.firebase.database

import com.example.e_commerceapp.data.User
import com.example.e_commerceapp.util.Constants.Companion.USER_COLLECTION
import com.example.e_commerceapp.util.Resource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserDB @Inject constructor(private val firestoreClient: FirebaseFirestore) {
    fun saveUser(userUid: String, user: User, onResult: (Resource<User>) -> Unit) {
        firestoreClient.collection(USER_COLLECTION).document(userUid).set(user)
            .addOnSuccessListener {
                onResult(Resource.Success(user))
            }.addOnFailureListener {
                onResult(Resource.Error(it.message.toString()))
            }
    }

    fun getUser(userUid: String, onResult: (Resource<User>) -> Unit) {
        firestoreClient.collection(USER_COLLECTION).document(userUid).get()
            .addOnSuccessListener {
                it.data?.let { json ->
                    onResult(Resource.Success(User().fromJson(json)))
                }
            }.addOnFailureListener {
                onResult(Resource.Error(it.message.toString()))
            }
    }

    fun getAllUsers(onResult: (Resource<List<DocumentSnapshot>>) -> Unit) {
        firestoreClient.collection(USER_COLLECTION).get()
            .addOnSuccessListener {
                onResult(Resource.Success(it.documents))
            }
            .addOnFailureListener {
                onResult(Resource.Error(it.message.toString()))
            }
    }
}