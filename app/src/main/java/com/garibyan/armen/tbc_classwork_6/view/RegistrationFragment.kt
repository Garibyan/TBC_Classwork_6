package com.garibyan.armen.tbc_classwork_6.view

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.garibyan.armen.tbc_classwork_6.R
import com.garibyan.armen.tbc_classwork_6.databinding.FragmentRegistrationBinding
import com.garibyan.armen.tbc_classwork_6.network.Resource
import com.garibyan.armen.tbc_classwork_6.viewmodel.RegistrationViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegistrationFragment : BaseFragment<FragmentRegistrationBinding, RegistrationViewModel>(
    FragmentRegistrationBinding::inflate, RegistrationViewModel::class.java
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            edtEmail.setText("eve.holt@reqres.in")
        }
        onClickListeners()
    }

    private fun onClickListeners() = with(binding) {
        btnRegistration.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val repeatedPassword = edtRepeatPassword.text.toString()
            if (isValidInput(email, password, repeatedPassword)) {
                observers()
                viewModel.register(email, password)
            }
        }
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerFlow.collectLatest {
                    when (it) {
                        is Resource.Success -> {
                            successfulState()
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

    private fun successfulState() {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(
            requireContext(),
            requireContext().getString(R.string.successful_registration),
            Toast.LENGTH_SHORT
        ).show()
        findNavController().popBackStack()
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

    private fun isValidInput(email: String, password: String, repeatedPassword: String): Boolean {
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
            isPasswordsMatch(password, repeatedPassword) -> {
                Toast.makeText(
                    requireContext(),
                    R.string.passwords_doesnot_match,
                    Toast.LENGTH_SHORT
                ).show()
            }
            !isEmptyRepeatedPassword(repeatedPassword) -> {
                Toast.makeText(requireContext(), R.string.repeated_password_empty, Toast.LENGTH_SHORT).show()
            }
            else -> isValid = true
        }
        return isValid
    }

    private fun isEmptyEmail(email: String) = email.isEmpty()
    private fun isEmptyPassword(password: String) = password.isEmpty()
    private fun isEmptyRepeatedPassword(repeatedPassword: String) = repeatedPassword.isEmpty()
    private fun isValidEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun isPasswordsMatch(password: String, repeatedPassword: String) =
        password == repeatedPassword


}