package com.davidmendozamartinez.ad340.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.*

class ForecastDetailsViewModelFactory(
    private val args: ForecastDetailsFragmentArgs,
    private val dateFormat: SimpleDateFormat
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForecastDetailsViewModel::class.java)) {
            return ForecastDetailsViewModel(args, dateFormat) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ForecastDetailsViewModel(args: ForecastDetailsFragmentArgs, dateFormat: SimpleDateFormat) :
    ViewModel() {
    private val _viewState: MutableLiveData<ForecastDetailsViewState> = MutableLiveData()
    val viewState: LiveData<ForecastDetailsViewState> = _viewState

    init {
        _viewState.value = ForecastDetailsViewState(
            temp = args.temp,
            description = args.description,
            date = dateFormat.format(Date(args.date * 1000)),
            iconUrl = "http://openweathermap.org/img/wn/${args.iconId}@2x.png"
        )
    }
}