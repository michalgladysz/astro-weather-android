package com.example.astro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.astro.model.Root
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

class ForecastAdapter(private val weatherForecast: Root) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
        val description: TextView = view.findViewById(R.id.description)
        val temperature: TextView = view.findViewById(R.id.temperature)
        val image: ImageView = view.findViewById(R.id.image)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.forecast_day_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        val formatter = SimpleDateFormat("E, d MMMM");
        val dateString = formatter.format(Date(weatherForecast.daily[position].dt * 1000))

        //val formatter = DateTimeFormatter.ofPattern("dd-mm-yyy", Locale.ENGLISH)
        //viewHolder.textView.text = LocalDate.parse(weatherForecast.daily[position].dt.toString(), formatter).toString()
        viewHolder.temperature.text = String.format("%d°C", weatherForecast.daily[position].temp.day.roundToInt())
        viewHolder.textView.text = dateString.toString()
        viewHolder.description.text = weatherForecast.daily[position].weather[0].description.capitalize()
        when (weatherForecast.daily[position].weather[0].icon) {
            "01d" -> viewHolder.image.setImageResource(R.drawable.ic_clear_sun_50)
            "02d" -> viewHolder.image.setImageResource(R.drawable.ic_sun_50)
            "03d" -> viewHolder.image.setImageResource(R.drawable.ic_cloud_50)
            "04d" -> viewHolder.image.setImageResource(R.drawable.ic_cloud_50)
            "09d" -> viewHolder.image.setImageResource(R.drawable.ic_rain_50)
            "10d" -> viewHolder.image.setImageResource(R.drawable.ic_rain_50)
            "11d" -> viewHolder.image.setImageResource(R.drawable.ic_storm_50)
            "13d" -> viewHolder.image.setImageResource(R.drawable.ic_snow_50)
            "50d" -> viewHolder.image.setImageResource(R.drawable.ic_cloud_50)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = weatherForecast.daily.size

}


