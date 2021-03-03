package com.knowtech.currencyconverter.data.models

import com.google.gson.JsonObject

data class ExchangeRateResponse(
    val base: String,
    val date: String,
    val rates: JsonObject
)