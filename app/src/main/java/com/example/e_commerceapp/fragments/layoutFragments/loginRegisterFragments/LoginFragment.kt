package com.example.e_commerceapp.fragments.layoutFragments.loginRegisterFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerceapp.R
import com.example.e_commerceapp.activities.LoginRegisterActivity
import com.example.e_commerceapp.activities.ShoppingActivity
import com.example.e_commerceapp.databinding.FragmentLoginBinding
import com.example.e_commerceapp.fragments.bottomsheetFragments.ResetPasswordBottomSheet
import com.example.e_commerceapp.util.Resource
import com.example.e_commerceapp.util.auth.FormValidation
import com.example.e_commerceapp.viewModels.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _bind: FragmentLoginBinding? = null
    private val bind get() = _bind
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as LoginRegisterActivity).loginViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _bind = FragmentLoginBinding.inflate(inflater, container, false)
        return bind!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRegisterTextClick()
        onGoogleButtonClick()
        onFacebookButtonClick()
        onLoginButtonClick()
        onForgotPasswordClick()
        observeRecoverMessage()
    }

    private fun onForgotPasswordClick() {
        bind?.tvForgotPasswordLogin?.setOnClickListener {
            ResetPasswordBottomSheet().show(childFragmentManager, "reset password")
        }
    }

    @Suppress("DEPRECATION")
    private fun observeRecoverMessage() {
        lifecycleScope.launchWhenStarted {
            withContext(Dispatchers.Main) {
                viewModel.resetPasswordMsg.collect {
                    when (it) {
                        is Resource.Loading -> {}

                        is Resource.Success -> {
                            Snackbar.make(
                                requireView(), it.data!!, Snackbar.LENGTH_LONG
                            ).show()
                        }

                        is Resource.Error -> {
                            Snackbar.make(
                                requireView(), it.message!!, Snackbar.LENGTH_LONG
                            ).show()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }


    private fun onRegisterTextClick() {
        bind?.loginToRegister?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun onLoginButtonClick() {
        bind?.apply {
            loginButton.setOnClickListener {
                val email = edEmailAddress.text.toString().trim()
                val password = edPassword.text.toString()
                viewModel.loginWithEmailAndPassword(email, password)
            }
        }
        observeLogin()
        observeLoginValidation()
    }

    @Suppress("DEPRECATION")
    private fun observeLoginValidation() {
        lifecycleScope.launchWhenStarted {
            viewModel.loginValidate.collect {
                bind?.let { bind ->
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
    }

    @Suppress("DEPRECATION")
    private fun observeLogin() {
        val button = bind?.loginButton
        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when (it) {
                    is Resource.Loading -> {
                        button?.startAnimation()
                    }

                    is Resource.Success -> {
                        button?.revertAnimation()
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    is Resource.Error -> {
                        button?.revertAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun onFacebookButtonClick() {
        return
    }

    private fun onGoogleButtonClick() {
        return
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }
}