package com.knowtech.currencyconverter.utils

interface NetworkAware {

    fun isOnline(): Boolean = false
}