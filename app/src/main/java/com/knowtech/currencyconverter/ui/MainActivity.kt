package com.knowtech.currencyconverter.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.knowtech.currencyconverter.core.ViewModelFactory
import com.knowtech.currencyconverter.databinding.ActivityMainBinding
import com.knowtech.currencyconverter.ui.state.CurrencyEvent
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

        setupViews()
        observeData()

    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            viewModel.conversion.observe(this@MainActivity, Observer { response ->
                when (response) {
                    is CurrencyEvent.Success -> {
                        showProgressBar(loading = false)
                        binding.tvResult.text = response.result

                    }
                    is CurrencyEvent.Error -> {
                        showProgressBar(loading = false)
                        binding.tvResult.text = null
                        Toast.makeText(this@MainActivity, response.error, Toast.LENGTH_SHORT).show()

                    }
                    is CurrencyEvent.Loading -> {
                        showProgressBar(loading = true)
                    }
                    else -> Unit
                }

            })
        }


    }

    private fun setupViews() {
        binding.btnConvert.setOnClickListener {
            val amount = binding.etAmount.text.toString().trim()
            val fromCurrency = binding.spFromCurrency.selectedItem.toString().trim()
            val toCurrency = binding.spToCurrency.selectedItem.toString().trim()

            viewModel.convertAmount(toCurrency, fromCurrency, amount)
        }
    }


    private fun showProgressBar(loading: Boolean) {

        with(binding) {
            progressBar.isVisible = loading
            tvResult.isVisible = !loading
            btnConvert.isVisible = !loading
        }

    }
}