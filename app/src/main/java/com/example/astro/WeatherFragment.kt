package com.example.astro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.astro.model.Root
import com.example.astro.network.ApiEndpoints
import com.example.astro.network.ServiceBuilder
import kotlinx.android.synthetic.main.weather_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt


class WeatherFragment : Fragment() {

    private var weatherDescription: TextView? = null
    private var temperature: TextView? = null
    private var minTemperature: TextView? = null
    private var maxTemperature: TextView? = null
    private var feelsLikeTemperature: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(
            R.layout.weather_fragment,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val myViewPager2: ViewPager2? = view.findViewById(R.id.pager2)
        myViewPager2?.adapter = NewPagerAdapter(childFragmentManager, lifecycle)
        myViewPager2!!.orientation = ViewPager2.ORIENTATION_VERTICAL

        weatherDescription = view.findViewById(R.id.weather_description) as TextView
        temperature = view.findViewById(R.id.temperature) as TextView
        minTemperature = view.findViewById(R.id.min_temperature) as TextView
        maxTemperature = view.findViewById(R.id.max_temperature) as TextView
        feelsLikeTemperature = view.findViewById(R.id.feels_like_temperature) as TextView

        val request = ServiceBuilder.buildService(ApiEndpoints::class.java)
        val call = request.getWeather()
        call.enqueue(object : Callback<Root> {
            override fun onResponse(call: Call<Root>, response: Response<Root>) {
                if (response.isSuccessful){
                    temperature!!.text = String.format("%d°C", response.body().current.temp.roundToInt())
                    feelsLikeTemperature!!.text = String.format("%d°C", response.body().current.feels_like.roundToInt())
                    weatherDescription!!.text = response.body().current.weather[0].description.toUpperCase()
                    maxTemperature!!.text = String.format("%d°C", response.body().daily[0].temp.max.roundToInt())
                    minTemperature!!.text = String.format("%d°C", response.body().daily[0].temp.min.roundToInt())

                    when (response.body().current.weather[0].icon) {
                        "01d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_clear_sun_100,0,0)
                        "02d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_sun_100,0,0)
                        "03d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cloud_100,0,0)
                        "04d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cloud_100,0,0)
                        "09d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_rain_100,0,0)
                        "10d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_rain_100,0,0)
                        "11d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_storm_100,0,0)
                        "13d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_snow_100,0, 0)
                        "50d" -> weatherDescription!!.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_cloud_100,0,0)
                    }

                }
            }
            override fun onFailure(call: Call<Root>, t: Throwable) {
                Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

}