package com.garibyan.armen.tbc_classwork_6.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.garibyan.armen.tbc_classwork_6.databinding.FragmentHomeBinding
import com.garibyan.armen.tbc_classwork_6.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate, HomeViewModel::class.java
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
    }

    private fun onClickListeners() = with(binding) {
        btnLogout.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    dataStore.clearToken()
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
                }
            }
        }
    }
}