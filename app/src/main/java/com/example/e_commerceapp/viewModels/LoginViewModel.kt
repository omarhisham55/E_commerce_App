package com.example.e_commerceapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.data.User
import com.example.e_commerceapp.firebase.authentication.FirebaseAuthentication
import com.example.e_commerceapp.firebase.database.UserDB
import com.example.e_commerceapp.util.Resource
import com.example.e_commerceapp.util.auth.ForgotPasswordFieldState
import com.example.e_commerceapp.util.auth.FormValidation
import com.example.e_commerceapp.util.auth.LoginFieldState
import com.example.e_commerceapp.util.auth.validateEmail
import com.example.e_commerceapp.util.auth.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuthentication: FirebaseAuthentication, private val userDB: UserDB
) : ViewModel() {
    private val _login = MutableSharedFlow<Resource<User>>()
    val login = _login.asSharedFlow()

    private val _loginValidate = Channel<LoginFieldState>()
    val loginValidate = _loginValidate.receiveAsFlow()

//    private val _allUsers = MutableSharedFlow<Resource<List<DocumentSnapshot>>>()
//    val allUsers = _allUsers.asSharedFlow()
//    var users: List<User> = mutableListOf()


//    init {
//        userDB.getAllUsers {
//            runBlocking {
//                _allUsers.emit(it)
//            }
//        }
//    }

    fun loginWithEmailAndPassword(email: String, password: String) {
        val isFormValid = checkLoginValidation(email, password)
        if (isFormValid) {
            runBlocking { _login.emit(Resource.Loading()) }
            firebaseAuthentication.loginWithEmailAndPassword(email, password) { response ->
                viewModelScope.launch {
                    _login.emit(response)
                }
            }
        } else {
            val loginFieldState = LoginFieldState(
                validateEmail(email), validatePassword(password)
            )
            runBlocking {
                _loginValidate.send(loginFieldState)
            }
        }
    }

    private fun checkLoginValidation(email: String, password: String): Boolean {
        return validateEmail(email) is FormValidation.Success && validatePassword(password) is FormValidation.Success
    }

    private val _resetPasswordMsg = MutableStateFlow<Resource<String>>(Resource.Init())
    val resetPasswordMsg = _resetPasswordMsg.asStateFlow()

    private val _resetValidate = Channel<ForgotPasswordFieldState>()
    val resetValidate = _resetValidate.receiveAsFlow()

    fun resetPassword(email: String) {
        if (checkEmailValidation(email)) {
            runBlocking { _resetPasswordMsg.emit(Resource.Loading()) }
            firebaseAuthentication.forgotPassword(email) { response ->
                viewModelScope.launch {
                    _resetPasswordMsg.emit(response)
                }
            }
        } else {
            val forgetPasswordFieldState = ForgotPasswordFieldState(validateEmail(email))
            runBlocking {
                _resetValidate.send(forgetPasswordFieldState)
            }
        }
    }

    private fun checkEmailValidation(email: String): Boolean {
        return validateEmail(email) is FormValidation.Success
    }

}