package com.davidmendozamartinez.ad340.forecast.weekly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.davidmendozamartinez.ad340.R
import com.davidmendozamartinez.ad340.TempDisplaySettingManager
import com.davidmendozamartinez.ad340.api.model.DailyForecast
import com.davidmendozamartinez.ad340.databinding.ItemDailyForecastBinding
import com.davidmendozamartinez.ad340.formatTempForDisplay
import java.text.SimpleDateFormat
import java.util.*

class DailyForecastViewHolder(
    private val binding: ItemDailyForecastBinding,
    private val tempDisplaySettingManager: TempDisplaySettingManager,
    private val dateFormat: SimpleDateFormat
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(dailyForecast: DailyForecast) {
        binding.tempText.text =
            formatTempForDisplay(
                dailyForecast.temp.max, tempDisplaySettingManager.getTempDisplaySetting()
            )
        binding.descriptionText.text = dailyForecast.weather[0].description
        binding.dateText.text = dateFormat.format(Date(dailyForecast.date * 1000))
        val iconId = dailyForecast.weather[0].icon
        binding.forecastIcon.load("http://openweathermap.org/img/wn/${iconId}@2x.png")
    }
}

class DailyForecastListAdapter(
    private val tempDisplaySettingManager: TempDisplaySettingManager,
    private val clickHandler: (DailyForecast) -> Unit
) : ListAdapter<DailyForecast, DailyForecastViewHolder>(
    DIFF_CONFIG
) {

    companion object {
        val DIFF_CONFIG = object : DiffUtil.ItemCallback<DailyForecast>() {
            override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(
                oldItem: DailyForecast,
                newItem: DailyForecast
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val binding =
            ItemDailyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val dateFormat =
            SimpleDateFormat(parent.context.getString(R.string.date_format), Locale.getDefault())
        return DailyForecastViewHolder(
            binding,
            tempDisplaySettingManager,
            dateFormat
        )
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            clickHandler(getItem(position))
        }
    }
}