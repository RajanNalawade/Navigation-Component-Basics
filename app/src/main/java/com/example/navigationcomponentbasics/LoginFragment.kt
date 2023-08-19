package com.example.navigationcomponentbasics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.navigationcomponentbasics.databinding.FragmentLoginBinding
import com.example.navigationcomponentbasics.models.UserRequest
import com.example.navigationcomponentbasics.utils.NetworkResult
import com.example.navigationcomponentbasics.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var mBinding: FragmentLoginBinding? = null
    private val binding get() = mBinding!!

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentLoginBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val validateResult = validateUserInput()
            if (validateResult.first) {
                authViewModel.loginUser(getUserRequest())
            } else {
                binding.txtError.text = validateResult.second
            }
        }

        binding.btnSignUp.setOnClickListener {
            //removes top from the backStack
            findNavController().popBackStack()
        }

        bindObservers()

    }

    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    //save token
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
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
            true
        )
    }

    private inline fun getUserRequest(): UserRequest {
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        return UserRequest(email, password, "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}