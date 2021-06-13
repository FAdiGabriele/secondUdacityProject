package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.NASAApi.retrofitPictureService
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application : Application) : AndroidViewModel(application) {

    private val db = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(db)

    private var _pictureResponse = MutableLiveData<PictureOfDay>()
    val pictureResponse : LiveData<PictureOfDay>
        get() = _pictureResponse

    private var _asteroidsResponse = MutableLiveData<List<Asteroid>>()
    val asteroidsResponse : LiveData<List<Asteroid>>
        get() = _asteroidsResponse

    fun getPictureOfTheDay() {
        retrofitPictureService.getPicturesOfTheDay().enqueue( object: Callback<PictureOfDay> {
            override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                Log.e("Picture_error","Failure: " + t.message)
            }

            override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
                _pictureResponse.value = response.body()
            }
        })
    }

    init{
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
        }
        getWeeklyAsteroids()

    }

    fun getWeeklyAsteroids(){
        asteroidsRepository.weeklyAsteroids.observeForever {
            _asteroidsResponse.value = it
        }
    }

    fun getDailyAsteroids(){
        asteroidsRepository.dailyAsteroids.observeForever {
            _asteroidsResponse.value = it
        }
    }

}