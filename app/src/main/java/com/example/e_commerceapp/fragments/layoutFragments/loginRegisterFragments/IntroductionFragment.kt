package com.example.e_commerceapp.fragments.layoutFragments.loginRegisterFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerceapp.R
import com.example.e_commerceapp.activities.ShoppingActivity
import com.example.e_commerceapp.databinding.FragmentIntroductionBinding
import com.example.e_commerceapp.viewModels.IntroductionViewModel
import com.example.e_commerceapp.viewModels.IntroductionViewModel.Companion.ACCOUNT_OPTIONS_FRAGMENT
import com.example.e_commerceapp.viewModels.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroductionFragment : Fragment() {
    private var _bind: FragmentIntroductionBinding? = null
    private val bind get() = _bind
    private val viewModel: IntroductionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _bind = FragmentIntroductionBinding.inflate(inflater, container, false)
        return bind!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNavState()
        onStartClick()
    }

    @Suppress("DEPRECATION")
    private fun observeNavState() {
        lifecycleScope.launchWhenStarted {
            viewModel.navigate.collect {
                when (it) {
                    SHOPPING_ACTIVITY -> {
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    ACCOUNT_OPTIONS_FRAGMENT -> findNavController().navigate(it)
                    else -> Unit
                }
            }
        }
    }

    private fun onStartClick() {
        bind?.startButton?.setOnClickListener {
            viewModel.onStartButtonClick()
            findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }
}