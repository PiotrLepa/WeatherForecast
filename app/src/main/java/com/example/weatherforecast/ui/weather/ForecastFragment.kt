package com.example.weatherforecast.ui.weather

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager

import com.example.weatherforecast.R
import com.example.weatherforecast.db.entity.WeatherForecastResponse
import com.example.weatherforecast.db.entity.WeatherResponse
import com.example.weatherforecast.ui.MainActivity
import com.example.weatherforecast.util.WeatherForecastUtils
import com.example.weatherforecast.util.WeatherUnitUtils
import com.example.weatherforecast.util.wrapper.Status
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.forecast_fragment.*
import timber.log.Timber
import javax.inject.Inject

const val DAY_COUNT_WEATHER_FORECAST = 5;

class ForecastFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ForecastViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.forecast_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ForecastViewModel::class.java)

        setupPullToRefresh()

        val selectedCityId = getSelectedCityIdArgs()
        Timber.d("onActivityCreated: selectedCityId: $selectedCityId")
        if (selectedCityId != null) {
            viewModel.fetchWeatherForecast(selectedCityId)
        } else {
            viewModel.loadLatestFetchedForecast()
        }

        observeWeatherForecast()
    }

    private fun setupPullToRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshWeather()
        }
    }

    private fun getSelectedCityIdArgs(): Int? {
        val safeArgs: ForecastFragmentArgs by navArgs()
        return if (safeArgs.selectedCityId != -1) {
            safeArgs.selectedCityId
        } else {
            null
        }
    }

    private fun observeWeatherForecast() {
        viewModel.loadedWeatherForecast.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    swipeRefreshLayout.isRefreshing = false
                    updateCityNameToolbar(it.data!!)
                    setupWeathersViewPager(it.data)
                }
                Status.ERROR -> {
                    swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(context, "Updating error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun updateCityNameToolbar(data: WeatherForecastResponse) {
        Timber.d("updateCityNameToolbar: ${data.city}")
        (activity as MainActivity).supportActionBar?.title = data.city.name
    }

    private fun setupWeathersViewPager(weatherForecast: WeatherForecastResponse) {
        Timber.d("setupWeathersViewPager: started")
        weathersViewPager.offscreenPageLimit = DAY_COUNT_WEATHER_FORECAST - 1

        val weathersDays = WeatherForecastUtils.getWeathersDaysList(weatherForecast)
        setupViewPagerAdapter(weathersDays)
        updateToolbarDate(weathersDays)
    }

    private fun setupViewPagerAdapter(weathersDays: List<List<WeatherResponse>>) {
        val weathersFragments = ArrayList<Fragment>().apply {
            weathersDays.forEach {
                add(WeatherFragment.newInstance(it))
            }
        }

        weathersViewPager.adapter = ScreenSlidePageAdapter(childFragmentManager, weathersFragments)
    }

    private fun updateToolbarDate(weathersDays: List<List<WeatherResponse>>) {
        (activity as MainActivity).supportActionBar?.subtitle =
            WeatherUnitUtils.formatDate(weathersDays[0][0].dt_txt)

        weathersViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            (activity as MainActivity).supportActionBar?.subtitle =
                WeatherUnitUtils.formatDate(weathersDays[position][0].dt_txt)
        }
        })
    }

    private class ScreenSlidePageAdapter(
        fragmentManager: FragmentManager,
        private val fragmentsList: List<Fragment>
    ): FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int) = fragmentsList[position]

        override fun getCount() = fragmentsList.size
    }
}
