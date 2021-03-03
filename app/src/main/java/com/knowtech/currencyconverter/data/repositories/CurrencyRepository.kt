package com.knowtech.currencyconverter.data.repositories

import com.knowtech.currencyconverter.data.models.ExchangeRateResponse
import com.knowtech.currencyconverter.utils.Resource

interface CurrencyRepository {
    suspend fun getRates(base:String,symbols : String) : Resource<ExchangeRateResponse>
}