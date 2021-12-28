package com.raiyanshahid.weather

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import android.widget.EditText
import java.text.DecimalFormat
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.raiyanshahid.weather.databinding.ActivityMainBinding

import org.json.JSONException

import org.json.JSONObject

import org.json.JSONArray

class MainActivity : AppCompatActivity() {


    private val url = "https://api.openweathermap.org/data/2.5/weather"
    private val appid = "6ec403c84c4575026a6c2f6cab776bb7"
    var df: DecimalFormat = DecimalFormat("#.##")

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnGet?.setOnClickListener {
            getWeatherDetails()
        }



    }

    fun getWeatherDetails() {
        var tempUrl = ""
        val city = binding.etCity!!.text.toString().trim { it <= ' ' }
        val country = "IN"
        if (city == "") {
            binding.tvResult!!.text = "City field can not be empty!"
        } else {
            tempUrl = if (country != "") {
                "$url?q=$city,$country&appid=$appid"
            } else {
                "$url?q=$city&appid=$appid"
            }
            val stringRequest =
                StringRequest(Request.Method.POST, tempUrl, object : Response.Listener<String?> {
                    override fun onResponse(response: String?) {
                        var output = ""
                        try {
                            val jsonResponse = JSONObject(response)
                            val jsonArray = jsonResponse.getJSONArray("weather")
                            val jsonObjectWeather = jsonArray.getJSONObject(0)
                            val description = jsonObjectWeather.getString("description")
                            val desc = jsonObjectWeather.getString("main")
                            val jsonObjectMain = jsonResponse.getJSONObject("main")
                            val temp = jsonObjectMain.getDouble("temp") - 273.15
                            val temperature = df.format(temp)
                            val feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15
                            val pressure = jsonObjectMain.getInt("pressure").toFloat()
                            val humidity = jsonObjectMain.getInt("humidity")
                            val hum : String = humidity.toString()
                            val jsonObjectWind = jsonResponse.getJSONObject("wind")
                            val wind = jsonObjectWind.getString("speed")
                            val jsonObjectClouds = jsonResponse.getJSONObject("clouds")
                            val clouds = jsonObjectClouds.getString("all")
                            val jsonObjectSys = jsonResponse.getJSONObject("sys")
                            val countryName = jsonObjectSys.getString("country")
                            val cityName = jsonResponse.getString("name")
                            val sunRise = jsonObjectSys.getString("sunrise")
                            val sunSet = jsonObjectSys.getString("sunset")
                            binding.tvResult?.setTextColor(Color.rgb(68, 134, 199))
                            output += """Current weather of $cityName ($countryName)
                                 Temp: ${df.format(temp)} °C
                                 Feels Like: ${df.format(feelsLike)} °C
                                 Humidity: $humidity%
                                 Description: $description
                                 Wind Speed: ${wind}m/s (meters per second)
                                 Cloudiness: $clouds%
                                 Pressure: $pressure hPa"""
                            binding.tvResult!!.text = output
                            val intent = Intent(this@MainActivity, WeatherActivity::class.java)
                            intent.putExtra("temp",temperature)
                            intent.putExtra("humidity",hum)
                            intent.putExtra("description",desc)
                            intent.putExtra("Cloudiness",clouds)
                            intent.putExtra("windSpeed",wind)
                            intent.putExtra("cityName",cityName)
                            intent.putExtra("sunrise",sunRise)
                            intent.putExtra("sunset",sunSet)
                            binding.etCity.setText("")
                            startActivity(intent)


                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }, object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Toast.makeText(
                            applicationContext,
                            error.toString().trim(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            val requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)
            requestQueue.add(stringRequest)
        }
    }
}