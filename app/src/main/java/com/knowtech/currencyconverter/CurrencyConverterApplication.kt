package com.knowtech.currencyconverter

import android.app.Application
import com.knowtech.currencyconverter.data.CurrencyApiInst
import com.knowtech.currencyconverter.data.repositories.CurrencyRepositoryImpl
import com.knowtech.currencyconverter.utils.NetworkManager
import com.knowtech.currencyconverter.utils.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class CurrencyConverterApplication : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {

        import(androidXModule(this@CurrencyConverterApplication))

        bind() from singleton { NetworkManager(instance()) }
        bind() from singleton { CurrencyApiInst() }
        bind() from singleton { CurrencyRepositoryImpl(instance()) }

        bind() from provider { ViewModelFactory(instance()) }

    }
}