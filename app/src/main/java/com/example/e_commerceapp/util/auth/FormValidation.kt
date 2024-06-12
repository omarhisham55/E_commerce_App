package com.example.e_commerceapp.util.auth

sealed class FormValidation {
    data object Success : FormValidation()
    data class Failed(val message: String) : FormValidation()
}

data class RegisterFieldState(
    val firstName: FormValidation,
    val lastName: FormValidation,
    val email: FormValidation,
    val password: FormValidation,
)

data class LoginFieldState(
    val email: FormValidation,
    val password: FormValidation,
)

data class ForgotPasswordFieldState(val email: FormValidation)
