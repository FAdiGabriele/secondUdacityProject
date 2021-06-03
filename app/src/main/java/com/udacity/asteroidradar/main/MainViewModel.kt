package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.api.NASAApi.retrofitAsteroidsService
import com.udacity.asteroidradar.api.NASAApi.retrofitPictureService
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.util.Constants.API_KEY
import com.udacity.asteroidradar.util.getActualDateFormatted
import com.udacity.asteroidradar.util.getNextSevenDaysDateFormatted
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainViewModel : ViewModel() {
    private var _pictureResponse = MutableLiveData<String>()
    val pictureResponse : LiveData<String>
        get() = _pictureResponse

    private var _asteroidsResponse = MutableLiveData<List<Asteroid>>()
    val asteroidsResponse : LiveData<List<Asteroid>>
        get() = _asteroidsResponse

    fun getPictureOfTheDay() {
        retrofitPictureService.getPicturesOfTheDay().enqueue( object: Callback<PictureOfDay> {
            override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                _pictureResponse.value = "Failure: " + t.message
            }

            override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
                _pictureResponse.value = response.body()?.url
            }
        })
    }

    fun getAsteroidList(){
        retrofitAsteroidsService.getAsteroids(getActualDateFormatted(), getNextSevenDaysDateFormatted(), API_KEY).enqueue( object: Callback<List<Asteroid>>{
            override fun onFailure(call: Call<List<Asteroid>>, t: Throwable) {
                _asteroidsResponse.value = mutableListOf(Asteroid(0, "Failure: $t","error",0.0,0.0,0.0,0.0,false ))
            }

            override fun onResponse(
                call: Call<List<Asteroid>>,
                response: Response<List<Asteroid>>
            ) {
                _asteroidsResponse.value = response.body()
            }

        })
    }


}