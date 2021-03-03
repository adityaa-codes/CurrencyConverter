package com.knowtech.currencyconverter.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knowtech.currencyconverter.data.models.ExchangeRateResponse
import com.knowtech.currencyconverter.data.repositories.CurrencyRepository
import com.knowtech.currencyconverter.data.repositories.CurrencyRepositoryImpl
import com.knowtech.currencyconverter.utils.NetworkManager
import com.knowtech.currencyconverter.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
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

    val rates: MutableLiveData<Resource<ExchangeRateResponse>> = MutableLiveData()
    private val _conversion = MutableLiveData<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: LiveData<CurrencyEvent> = _conversion
    private lateinit var response: Response<ExchangeRateResponse>

    /*private suspend fun getRates(base: String, symbols: String) {
        try {
            if (networkManager.isOnline()) {
                response = repositoryImpl.getRates(base, symbols)
                rates.postValue(handleRatesResponse(response))
            } else {
                rates.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            rates.postValue(Resource.Error(e.message ?: "An Error Occurred"))
        }
    }*/
/*
    private fun handleRatesResponse(response: Response<ExchangeRateResponse>): Resource<ExchangeRateResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())

    }*/

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

            val rateResponse = repository.getRates(fromCurrency, toCurrency)
            when (rateResponse) {
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