package com.example.weatherforecast.ui.cities

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.example.weatherforecast.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CitiesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: CitiesViewModel

    private lateinit var addCityButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.cities_fragment, container, false)
        initWidgets(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CitiesViewModel::class.java)
    }

    private fun initWidgets(view: View) {
        view.apply {
            addCityButton = findViewById(R.id.addCityButton)
        }
        addCityButton.setOnClickListener {
            openAddCityFragment()
        }
    }

    private fun openAddCityFragment() {
        val action = CitiesFragmentDirections.actionCitiesFragmentToAddCityFragment()
        findNavController().navigate(action)
    }
}
