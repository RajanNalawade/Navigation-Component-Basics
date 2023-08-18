package com.example.navigationcomponentbasics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.navigationcomponentbasics.databinding.FragmentRegistrationBinding
import com.example.navigationcomponentbasics.models.UserRequest
import com.example.navigationcomponentbasics.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var mBinding: FragmentRegistrationBinding? = null
    private val binding get() = mBinding!!

    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentRegistrationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        binding.btnSignUp.setOnClickListener {

            val validateResult = validateUserInput()

            if (validateResult.first) {
                authViewModel.registerUser(
                    getUserRequest()
                )
            } else {
                binding.txtError.text = validateResult.second
            }
        }

        bindObservers()
    }


    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    //save token
                    findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
                }

                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateUserCredentials(
            userRequest.userName,
            userRequest.email,
            userRequest.password,
            false
        )
    }

    private fun getUserRequest(): UserRequest {
        val userName = binding.txtUsername.text.toString()
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        return UserRequest(email, password, userName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

}