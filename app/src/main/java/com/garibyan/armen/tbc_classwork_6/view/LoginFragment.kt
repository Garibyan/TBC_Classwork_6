package com.garibyan.armen.tbc_classwork_6.view

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.garibyan.armen.tbc_classwork_6.R
import com.garibyan.armen.tbc_classwork_6.databinding.FragmentLoginBinding
import com.garibyan.armen.tbc_classwork_6.network.Resource
import com.garibyan.armen.tbc_classwork_6.utils.FragmentResult
import com.garibyan.armen.tbc_classwork_6.utils.PreferenceKeys
import com.garibyan.armen.tbc_classwork_6.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate
) {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentResultListener()
        onClickListeners()
        isLoggedIn()
    }

    private fun onClickListeners() = with(binding) {
        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            if (isValidInput(email, password)) {
                observers()
                viewModel.login(email, password)
            }
        }
        btnRegistration.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }
    }

    private fun isLoggedIn() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getToken(PreferenceKeys.KEY_AUTH).collect {
                    if (it != "") {
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    }
                }
            }
        }
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginFlow.collectLatest {
                    when (it) {
                        is Resource.Success -> {
                            if (binding.chackBoxRememberMe.isChecked) {
                                viewModel.saveToken(it.value.token.toString())
                            } else {
                                successfulState()
                                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                            }
                        }
                        is Resource.Error -> {
                            errorState(it.isNetworkError!!)
                        }
                        is Resource.Loading -> {
                            loadingState()
                        }
                    }
                }
            }
        }
    }

    private fun fragmentResultListener() {
        setFragmentResultListener(FragmentResult.AUTH_KEY) { _, bundle ->
            binding.edtEmail.setText(bundle.getString(FragmentResult.EMAIL, "No Value"))
            binding.edtPassword.setText(bundle.getString(FragmentResult.PASSWORD, "No Value"))
        }
    }

    private fun successfulState() {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(
            requireContext(),
            requireContext().getString(R.string.successfully_login),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun errorState(isNetworkError: Boolean) {
        binding.progressBar.visibility = View.GONE
        if (isNetworkError) {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.network_error),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.wrong_credentials),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadingState() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun isValidInput(email: String, password: String): Boolean {
        var isValid = false
        when (false) {
            !isEmptyEmail(email) -> {
                Toast.makeText(
                    requireContext(),
                    requireContext().getText(R.string.empty_email),
                    Toast.LENGTH_SHORT
                ).show()
            }
            isValidEmail(email) -> {
                Toast.makeText(
                    requireContext(),
                    requireContext().getText(R.string.incorrect_email),
                    Toast.LENGTH_SHORT
                ).show()
            }
            !isEmptyPassword(password) -> {
                Toast.makeText(
                    requireContext(),
                    requireContext().getText(R.string.empty_password),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> isValid = true
        }
        return isValid
    }

    private fun isEmptyEmail(email: String) = email.isEmpty()
    private fun isEmptyPassword(password: String) = password.isEmpty()
    private fun isValidEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

}