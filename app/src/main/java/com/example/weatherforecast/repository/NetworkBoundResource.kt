package com.example.weatherforecast.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.weatherforecast.api.ApiEmptyResponse
import com.example.weatherforecast.api.ApiErrorResponse
import com.example.weatherforecast.api.ApiResponse
import com.example.weatherforecast.api.ApiSuccessResponse
import com.example.weatherforecast.util.wrapper.Resource

abstract class NetworkBoundResource<ResultType, RequestType> {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    //TODO use coroutines to handle this
//                    appExecutors.diskIO().execute {
//                        saveCallResult(processResponse(response))
//                        appExecutors.mainThread().execute {
//                            // we specially request a new live data,
//                            // otherwise we will get immediately last cached value,
//                            // which may not be updated with latest results received from network.
//                            result.addSource(loadFromDb()) { newData ->
//                                setValue(Resource.success(newData))
//                            }
//                        }
//                    }
                }
                is ApiEmptyResponse -> {
                    //TODO use coroutines to handle this
//                    appExecutors.mainThread().execute {
//                        // reload from disk whatever we had
//                        result.addSource(loadFromDb()) { newData ->
//                            setValue(Resource.success(newData))
//                        }
//                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        setValue(Resource.error(response.errorMessage, newData))
                    }
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}