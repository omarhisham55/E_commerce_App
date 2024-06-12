package com.example.e_commerceapp.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityLoginRegisterBinding
import com.example.e_commerceapp.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRegisterActivity : AppCompatActivity() {
    private var _bind: ActivityLoginRegisterBinding? = null
    private val bind get() = _bind
    val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bind = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(bind?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _bind = null
    }
}