package com.knowtech.currencyconverter.data

import com.knowtech.currencyconverter.data.models.ExchangeRateResponse
import com.knowtech.currencyconverter.utils.Resource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("/latest")
    suspend fun getExRates(
        @Query("base") base : String,
        @Query("symbols") symbols : String
    ) : Response<ExchangeRateResponse>
}