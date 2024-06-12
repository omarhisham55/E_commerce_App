package com.example.e_commerceapp.util.auth

import android.util.Patterns

fun validateFirstName(name: String): FormValidation {
    if (name.isEmpty()) return FormValidation.Failed("First name is required")
    return FormValidation.Success
}

fun validateLastName(name: String): FormValidation {
    if (name.isEmpty()) return FormValidation.Failed("Last name is required")
    return FormValidation.Success
}

fun validateEmail(email: String): FormValidation {
    if (email.isEmpty()) return FormValidation.Failed("Email is required")
    if (!Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    ) return FormValidation.Failed("wrong email format")
    return FormValidation.Success
}

fun validatePassword(password: String): FormValidation {
    if (password.isEmpty()) {
        return FormValidation.Failed("Password is required")
    }
    if (password.length < 6) {
        return FormValidation.Failed("Min length 6")
    }
    return FormValidation.Success
}