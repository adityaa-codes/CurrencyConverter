package com.knowtech.currencyconverter.data.repositories

import com.knowtech.currencyconverter.data.RetrofitInstance
import com.knowtech.currencyconverter.data.models.ExchangeRateResponse
import com.knowtech.currencyconverter.core.NetworkAware
import com.knowtech.currencyconverter.core.Resource
import com.knowtech.currencyconverter.data.CurrencyApi

class CurrencyRepositoryImpl(
    private val networkAware: NetworkAware,
    private val retrofitInstance: RetrofitInstance
): CurrencyRepository {

    val api by lazy {
        retrofitInstance.retrofit.create(CurrencyApi::class.java)
    }

    override suspend fun getRates(base: String, symbols: String): Resource<ExchangeRateResponse> {
        return try {
            if (networkAware.isOnline()) {
                val response = api.getExRates(base, symbols)
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Resource.Success(result)
                } else {
                    Resource.Error(response.message())
                }
            }
            else{
                Resource.Error("No Internet Connection")
            }

        }
        catch (e: Exception){
            Resource.Error(e.message?: "An Error Occurred")
        }
    }
}