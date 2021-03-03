package com.knowtech.currencyconverter.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.knowtech.currencyconverter.data.repositories.CurrencyRepository
import com.knowtech.currencyconverter.data.repositories.CurrencyRepositoryImpl
import com.knowtech.currencyconverter.ui.CurrencyViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: CurrencyRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
            return CurrencyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}