package com.knowtech.currencyconverter.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.knowtech.currencyconverter.databinding.ActivityMainBinding
import com.knowtech.currencyconverter.utils.ViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

    private lateinit var binding: ActivityMainBinding
    override val kodein by kodein()
    private val factory: ViewModelFactory by instance()
    lateinit var viewModel: CurrencyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, factory).get(CurrencyViewModel::class.java)

        binding.btnConvert.setOnClickListener {
            val amount = binding.etAmount.text.toString().trim()
            val fromCurrency = binding.spFromCurrency.selectedItem.toString().trim()
            val toCurrency = binding.spToCurrency.selectedItem.toString().trim()

            viewModel.convertAmount(toCurrency, fromCurrency, amount)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.observe(this@MainActivity, Observer { response ->
                when (response) {
                    is CurrencyViewModel.CurrencyEvent.Success -> {
                        hideProgressBar()
                        binding.tvResult.text = response.result

                    }
                    is CurrencyViewModel.CurrencyEvent.Error -> {
                        hideProgressBar()
                        binding.tvResult.text = response.error

                    }
                    is CurrencyViewModel.CurrencyEvent.Loading -> {
                        showProgressBar()
                    }
                    else -> Unit
                }

            })
        }


    }


    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.tvResult.visibility = View.VISIBLE
        binding.btnConvert.visibility = View.VISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvResult.visibility = View.GONE
        binding.btnConvert.visibility = View.GONE
    }
}