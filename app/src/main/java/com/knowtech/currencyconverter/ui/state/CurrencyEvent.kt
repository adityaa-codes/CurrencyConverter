package com.knowtech.currencyconverter.ui.state

sealed class CurrencyEvent {
    class Success(val result: String) : CurrencyEvent()
    class Error(val error: String) : CurrencyEvent()
    object Loading : CurrencyEvent()
}