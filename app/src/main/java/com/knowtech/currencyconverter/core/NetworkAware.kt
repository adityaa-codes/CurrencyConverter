package com.knowtech.currencyconverter.core

interface NetworkAware {

    fun isOnline(): Boolean = false
}