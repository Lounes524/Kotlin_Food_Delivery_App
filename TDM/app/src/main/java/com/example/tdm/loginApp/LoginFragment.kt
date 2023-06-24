package com.example.tdm.loginApp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.tdm.R
import com.example.tdm.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.confirmSignInBtn.setOnClickListener(){
                view: View ->view.findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
        }

        binding.registerTextView.setOnClickListener(){
                view: View ->view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

    }


}