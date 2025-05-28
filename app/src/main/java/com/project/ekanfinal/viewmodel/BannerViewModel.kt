package com.project.ekanfinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.ekanfinal.model.repository.BannerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class BannerViewModel(
    private val repository: BannerRepository = BannerRepository()
) : ViewModel() {

    private val _banners = MutableStateFlow<List<String>>(emptyList())
    val banners: StateFlow<List<String>> = _banners

    init {
        fetchBanners()
    }

    private fun fetchBanners() {
        viewModelScope.launch {
            _banners.value = repository.getBanners()
        }
    }
}