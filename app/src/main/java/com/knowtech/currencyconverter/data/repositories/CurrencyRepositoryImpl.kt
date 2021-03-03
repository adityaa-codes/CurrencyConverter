package com.knowtech.currencyconverter.data.repositories

import com.knowtech.currencyconverter.data.CurrencyApi
import com.knowtech.currencyconverter.data.CurrencyApiInst
import com.knowtech.currencyconverter.data.models.ExchangeRateResponse
import com.knowtech.currencyconverter.utils.NetworkAware
import com.knowtech.currencyconverter.utils.NetworkManager
import com.knowtech.currencyconverter.utils.Resource
import java.lang.Exception

class CurrencyRepositoryImpl(
    private val networkAware: NetworkAware
): CurrencyRepository{

    override suspend fun getRates(base: String, symbols: String): Resource<ExchangeRateResponse> {
        return try {
            if(networkAware.isOnline()){
                val response = CurrencyApiInst.api.getExRates(base, symbols)
                val result = response.body()
                if(response.isSuccessful && result!=null){
                    Resource.Success(result)
                }else{
                    Resource.Error(response.message())
                }
            }
            else{
                Resource.Error("No Internet Connection")
            }

        }
        catch (e: Exception){
            Resource.Error(e.message?: "An Error Occured")
        }
    }
}