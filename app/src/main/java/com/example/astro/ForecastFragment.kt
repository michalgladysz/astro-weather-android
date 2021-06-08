package com.example.astro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.astro.model.Root
import com.example.astro.network.ApiEndpoints
import com.example.astro.network.ServiceBuilder

import kotlinx.android.synthetic.main.forecast_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecastFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(
            R.layout.forecast_fragment,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // set up the RecyclerView
        // set up the RecyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            val request = ServiceBuilder.buildService(ApiEndpoints::class.java)
            val call = request.getWeather()
            call.enqueue(object : Callback<Root> {
                override fun onResponse(call: Call<Root>, response: Response<Root>) {
                    if (response.isSuccessful){
                        val weatherForecast : Root = response.body()
                        adapter = ForecastAdapter(weatherForecast)
                    }
                }
                override fun onFailure(call: Call<Root>, t: Throwable) {
                    Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })


        }
    }
}