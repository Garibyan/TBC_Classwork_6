package com.garibyan.armen.tbc_classwork_6.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.garibyan.armen.tbc_classwork_6.databinding.FragmentHomeBinding
import com.garibyan.armen.tbc_classwork_6.utils.FragmentResult
import com.garibyan.armen.tbc_classwork_6.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentResultListener()
        onClickListeners()
    }

    private fun onClickListeners() = with(binding) {
        btnLogout.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.cleanDataStore()
            }
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
        }
    }

    private fun fragmentResultListener() {
        setFragmentResultListener(FragmentResult.AUTH_KEY) { _, bundle ->
            binding.txtEmail.text = bundle.getString(FragmentResult.EMAIL, "No Value")
        }
    }
}