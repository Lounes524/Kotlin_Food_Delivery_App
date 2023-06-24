package com.example.tdm.loginApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tdm.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
}