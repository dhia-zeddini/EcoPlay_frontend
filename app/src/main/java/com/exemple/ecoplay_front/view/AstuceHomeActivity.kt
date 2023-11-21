package com.exemple.ecoplay_front.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecoplay_front.viewModel.AstuceViewModel
import com.exemple.ecoplay_front.R
import com.exemple.ecoplay_front.viewModel.AstuceAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AstuceHomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var astuceAdapter: AstuceAdapter

    private val viewModel: AstuceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_astuce_home)

        setUpRecyclerView()
        observeViewModel()
        fetchAstuceData()
    }

    private fun setUpRecyclerView() {
        recyclerView = findViewById(R.id.astuce_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        astuceAdapter = AstuceAdapter()
        recyclerView.adapter = astuceAdapter
    }

    private fun observeViewModel() {
        // Observe LiveData for astuces
        viewModel.getAstuces().observe(this, Observer { astuces ->
            astuces?.let {
                astuceAdapter.setAstuces(it)
            }
        })

        // Observe LiveData for error messages
        viewModel.getErrorMessage().observe(this, Observer { errorMessage ->
            errorMessage?.let {
                showError(it)
            }
        })
    }

    private fun fetchAstuceData() {
        // Trigger the loading of astuces
        viewModel.loadAstuces()
    }

    private fun showError(message: String) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show()
    }
}
