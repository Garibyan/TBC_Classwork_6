package com.garibyan.armen.tbc_classwork_6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garibyan.armen.tbc_classwork_6.repository.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStore: DataStore
) : ViewModel() {

    fun cleanDataStore(){
        viewModelScope.launch {
            dataStore.clear()
        }
    }

}