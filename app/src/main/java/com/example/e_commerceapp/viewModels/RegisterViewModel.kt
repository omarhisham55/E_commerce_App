package com.example.e_commerceapp.viewModels

import androidx.lifecycle.ViewModel
import com.example.e_commerceapp.data.User
import com.example.e_commerceapp.firebase.authentication.FirebaseAuthentication
import com.example.e_commerceapp.firebase.database.FirestoreController
import com.example.e_commerceapp.util.Resource
import com.example.e_commerceapp.util.auth.FormValidation
import com.example.e_commerceapp.util.auth.RegisterFieldState
import com.example.e_commerceapp.util.auth.validateEmail
import com.example.e_commerceapp.util.auth.validateFirstName
import com.example.e_commerceapp.util.auth.validateLastName
import com.example.e_commerceapp.util.auth.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuthentication, private val firestore: FirestoreController
) : ViewModel() {
    private val _register = MutableStateFlow<Resource<User>>(Resource.Init())
    val register: Flow<Resource<User>> = _register

    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User, password: String) {
        val isFormValid = checkRegisterValidation(user, password)
        if (isFormValid) {
            runBlocking { _register.emit(Resource.Loading()) }
            firebaseAuth.createAccountWithEmailAndPassword(
                user, password
            ) { response ->
                _register.value = response
            }
        } else {
            val registerFieldState = RegisterFieldState(
                firstName = validateFirstName(user.firstName),
                lastName = validateLastName(user.lastName),
                email = validateEmail(user.email),
                password = validatePassword(password),
            )
            runBlocking {
                _validation.send(registerFieldState)
            }
        }
    }

    private fun checkRegisterValidation(user: User, password: String): Boolean {
        return validateFirstName(user.firstName) is FormValidation.Success && validateLastName(user.lastName) is FormValidation.Success && validateEmail(
            user.email
        ) is FormValidation.Success && validatePassword(password) is FormValidation.Success
    }
}