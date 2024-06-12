package com.example.e_commerceapp.fragments.bottomsheetFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapp.activities.LoginRegisterActivity
import com.example.e_commerceapp.databinding.FragmentResetPasswordBottomSheetBinding
import com.example.e_commerceapp.util.auth.FormValidation
import com.example.e_commerceapp.viewModels.LoginViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ResetPasswordBottomSheet : BottomSheetDialogFragment() {
    private var _binding: FragmentResetPasswordBottomSheetBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as LoginRegisterActivity).loginViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBottomSheetBinding.inflate(inflater, container, false)
        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            recoveryButton.setOnClickListener {
                val email = edEmailAddressInResetPassword.text.toString().trim()
                viewModel.resetPassword(email)
                runBlocking { delay(1000) }
                dismissBottomsheet()
            }
        }
        observeResetPasswordEmailValidation()
    }

    @Suppress("DEPRECATION")
    private fun observeResetPasswordEmailValidation() {
        lifecycleScope.launchWhenStarted {
            viewModel.resetValidate.collect {
                if (it.email is FormValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding?.let { bind ->
                            bind.edEmailAddressInResetPassword.apply {
                                requestFocus()
                                error = it.email.message
                            }
                        }
                    }
                }
            }
        }
    }

    private fun dismissBottomsheet() {
        this.dismiss()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}