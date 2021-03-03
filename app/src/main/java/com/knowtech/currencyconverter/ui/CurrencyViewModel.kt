package com.knowtech.currencyconverter.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knowtech.currencyconverter.data.repositories.CurrencyRepository
import com.knowtech.currencyconverter.utils.Resource
import kotlinx.coroutines.launch
import kotlin.math.round

class CurrencyViewModel(
    private val repository: CurrencyRepository
) : ViewModel() {

    sealed class CurrencyEvent {
        class Success(val result: String) : CurrencyEvent()
        class Error(val error: String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val _conversion = MutableLiveData<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: LiveData<CurrencyEvent> = _conversion

    fun convertAmount(
        toCurrency: String,
        fromCurrency: String,
        amount: String
    ) {

        val fromAmt = amount.toFloatOrNull()
        if (fromAmt == null) {
            _conversion.value = CurrencyEvent.Error("Not a Valid Input")
            return
        }

        viewModelScope.launch {
            _conversion.value = CurrencyEvent.Loading

            when (val rateResponse = repository.getRates(fromCurrency, toCurrency)) {
                is Resource.Error -> _conversion.value = CurrencyEvent.Error(rateResponse.message!!)
                is Resource.Success -> {
                    val rates = rateResponse.data!!.rates
                    val rate = rates.get(toCurrency)?.asDouble
                    if (rate == null) {
                        _conversion.value = CurrencyEvent.Error("Unexpected Error")
                    } else {
                        val convertedCurrency = round(fromAmt * rate * 100) / 100
                        _conversion.value =
                            CurrencyEvent.Success("$fromAmt $fromCurrency  = $convertedCurrency $toCurrency")
                    }
                }

            }
        }

    }
}