package com.raiyanshahid.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.raiyanshahid.weather.databinding.ActivityWeatherBinding
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class WeatherActivity : AppCompatActivity() {

    lateinit var binding : ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var place = intent.getStringExtra("cityName")
        var temp = intent.getStringExtra("temp")
        var humidity = intent.getStringExtra("humidity")
        var description = intent.getStringExtra("description")
        var Cloudiness = intent.getStringExtra("Cloudiness")
        var windSpeed = intent.getStringExtra("windSpeed")
        var sunrise = intent.getStringExtra("sunrise")
        var sunset = intent.getStringExtra("sunset")

        val c: Calendar = Calendar.getInstance()
        val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)

        if (timeOfDay >= 0 && timeOfDay < 5) {
            binding.day.setVisibility(View.GONE)
            binding.night.setVisibility(View.VISIBLE)
            binding.greetings.setText("Good Night")
        }
        if (timeOfDay >= 5 && timeOfDay < 12) {
            binding.day.setVisibility(View.VISIBLE)
            binding.night.setVisibility(View.GONE)
            binding.greetings.setText("Good Morning")
        }
        else if (timeOfDay >= 12 && timeOfDay < 16) {
            binding.day.setVisibility(View.VISIBLE)
            binding.night.setVisibility(View.GONE)
            binding.greetings.setText("Good Afternoon")

        } else if (timeOfDay >= 16 && timeOfDay < 18) {
            binding.day.setVisibility(View.VISIBLE)
            binding.night.setVisibility(View.GONE)
            binding.greetings.setText("Good Evening")

        }
        else if (timeOfDay >=18 && timeOfDay < 18){
            binding.day.setVisibility(View.GONE)
            binding.night.setVisibility(View.VISIBLE)
            binding.greetings.setText("Good Evening")

        }
        else if (timeOfDay >= 21 && timeOfDay < 24) {
            binding.day.setVisibility(View.GONE)
            binding.night.setVisibility(View.VISIBLE)
            binding.greetings.setText("Good Night")

        }

        binding.add.setOnClickListener {
            finish()
        }



        binding.temp.setText(temp+" °C")
        binding.cloudiness.setText(Cloudiness+"%")
        binding.humidity.setText(humidity+"%")
        binding.state.setText(place)
        binding.wind.setText(windSpeed+" m/s")
        binding.desc.setText(description)
        binding.sunrise.setText("Sunrise at "+getTime(sunrise?.toLong()!!))
        binding.sunset.setText("Sunset at "+getTime(sunset?.toLong()!!))



    }

    fun getTime(timeStamp: Long): String? {
        try {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeStamp * 1000
            val sdf = SimpleDateFormat("hh:mm a")
            val date = calendar.time as Date
            return sdf.format(date)
        } catch (e: Exception) {
        }
        return ""
    }
}