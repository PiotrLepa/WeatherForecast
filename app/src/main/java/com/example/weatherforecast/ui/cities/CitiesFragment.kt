package com.example.weatherforecast.ui.cities

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.weatherforecast.R
import com.example.weatherforecast.db.entity.City
import com.example.weatherforecast.ui.MainActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.cities_fragment.*
import timber.log.Timber
import javax.inject.Inject
import com.example.weatherforecast.util.wrapper.Status.LOADING
import com.example.weatherforecast.util.wrapper.Status.SUCCESS
import com.example.weatherforecast.util.wrapper.Status.ERROR

class CitiesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: CitiesViewModel

    private lateinit var citiesWeatherAdapter: GroupAdapter<ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cities_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CitiesViewModel::class.java)

        setupToolbar()
        setupViews()

        val selectedCity = getSelectedCityArgs()
        selectedCity?.let {
            viewModel.onCityAdded(selectedCity)
        }

        viewModel.citiesList.observe(viewLifecycleOwner, Observer { citiesList ->
            Timber.d("onActivityCreated: citiesList size: ${citiesList.size}")
            //if is something in room database
            if (citiesList.size >= 1 ) {
                viewModel.fetchCitiesWeathers(citiesList)
            }
        })

        viewModel.citiesWeathers.observe(viewLifecycleOwner, Observer {
            Timber.d("onActivityCreated: citiesWeathers status: ${it.status}")
            when (it.status) {
                LOADING -> {

                }
                SUCCESS -> {
                    Timber.d("onActivityCreated: SUCCESS listSize: ${it.data!!.size}")
                    citiesWeatherAdapter.clear()
                    citiesWeatherAdapter.addAll(it.data.map { CityWeatherItem(it) })
                }
                ERROR -> {
                    Toast.makeText(context, "Error ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.subtitle = ""
    }

    private fun setupViews() {
        addCityButton.setOnClickListener {
            openAddCityFragment()
        }
        setupRecyclerView()
    }

    private fun getSelectedCityArgs(): City? {
        val safeArgs: CitiesFragmentArgs by navArgs()
        return safeArgs.selectedCity
    }

    private fun setupRecyclerView() {
        citiesWeatherAdapter = GroupAdapter()
        citiesRecyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        citiesRecyclerView.adapter = citiesWeatherAdapter
        citiesWeatherAdapter.setOnItemClickListener { item, _ ->
            openWeatherFragment((item as CityWeatherItem))
        }
    }

    private fun openWeatherFragment(item: CityWeatherItem) {
        val action = CitiesFragmentDirections.actionCitiesFragmentToWeatherFragment()
        action.selectedCity = item.weather
        findNavController().navigate(action)
    }

    private fun openAddCityFragment() {
        val action = CitiesFragmentDirections.actionCitiesFragmentToAddCityFragment()
        findNavController().navigate(action)
    }
}
