package com.example.navigationcomponentbasics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.navigationcomponentbasics.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {

    private var mBinding: FragmentRegistrationBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentRegistrationBinding.inflate(inflater, container, false)

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

}