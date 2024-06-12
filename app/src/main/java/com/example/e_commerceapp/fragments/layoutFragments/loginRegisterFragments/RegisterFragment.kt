package com.example.e_commerceapp.fragments.layoutFragments.loginRegisterFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerceapp.R
import com.example.e_commerceapp.data.User
import com.example.e_commerceapp.databinding.FragmentRegisterBinding
import com.example.e_commerceapp.util.Resource
import com.example.e_commerceapp.util.auth.FormValidation
import com.example.e_commerceapp.viewModels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var bind: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        bind = FragmentRegisterBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onLoginTextClick()
        onRegisterButtonClick()
        onGoogleButtonClick()
        onFacebookButtonClick()
    }

    private fun onLoginTextClick() {
        bind.registerToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun onFacebookButtonClick() {
        return
    }

    private fun onGoogleButtonClick() {
        return
    }

    private fun onRegisterButtonClick() {
        bind.apply {
            registerButton.setOnClickListener {
                val user = User(
                    firstName = edFirstName.text.toString().trim(),
                    lastName = edLastName.text.toString().trim(),
                    email = edEmailAddress.text.toString().trim()
                )
                val password = edPassword.text.toString()
                viewModel.createAccountWithEmailAndPassword(
                    user, password
                )
            }
        }
        observeButtonClick()
        observeValidation()
    }

    @Suppress("DEPRECATION")
    private fun observeValidation() {
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect {
                if (it.firstName is FormValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        bind.edFirstName.apply {
                            requestFocus()
                            error = it.firstName.message
                        }
                    }
                }
                if (it.lastName is FormValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        bind.edLastName.apply {
                            requestFocus()
                            error = it.lastName.message
                        }
                    }
                }
                if (it.email is FormValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        bind.edEmailAddress.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }
                if (it.password is FormValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        bind.edPassword.apply {
                            requestFocus()
                            error = it.password.message
                        }
                    }
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun observeButtonClick() {
        val button = bind.registerButton
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect {
                when (it) {
                    is Resource.Loading -> {
                        button.startAnimation()
                    }

                    is Resource.Success -> {
                        button.revertAnimation()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }

                    is Resource.Error -> {
                        button.revertAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }
}