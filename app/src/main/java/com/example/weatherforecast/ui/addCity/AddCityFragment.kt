package com.example.weatherforecast.ui.addCity

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.weatherforecast.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.add_city_fragment.*
import timber.log.Timber
import javax.inject.Inject

class AddCityFragment : DaggerFragment(), SearchView.OnQueryTextListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AddCityViewModel

    private lateinit var citiesAdapter: GroupAdapter<ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_city_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddCityViewModel::class.java)

        setHasOptionsMenu(true)
        setupRecyclerView()

        viewModel.filteredCitiesList.observe(viewLifecycleOwner, Observer {
            Timber.d("onActivityCreated: it.isNullOrEmpty(): ${it.isNullOrEmpty()}")
            val items = it.map { city ->
                CityItem(city)
            }
            citiesAdapter.clear()
            citiesAdapter.addAll(items)
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.add_city_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId ==R.id.action_search) {
            Timber.d("onOptionsItemSelected: actionSearch clicked")
            val searchView = item.actionView as SearchView
            searchView.setOnQueryTextListener(this)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            viewModel.queryCitiesList(newText)
        }
        return false
    }

    override fun onQueryTextSubmit(query: String?) = false


    private fun setupRecyclerView() {
        citiesAdapter = GroupAdapter()
        citiesRecyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        citiesRecyclerView.adapter = citiesAdapter
        citiesAdapter.setOnItemClickListener { item, _ ->
            openCitiesFragment((item as CityItem))
        }
    }

    private fun openCitiesFragment(item: CityItem) {
        val action = AddCityFragmentDirections.actionAddCityFragmentToCitiesFragment()
        action.selectedCity = item.city
        Timber.d("openCitiesFragment: clicked item: ${item.city}")
        findNavController().navigate(action)
    }
}
