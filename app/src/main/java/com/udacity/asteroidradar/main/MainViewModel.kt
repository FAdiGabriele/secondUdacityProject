package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.udacity.asteroidradar.api.NASAApi.retrofitAsteroidsService
import com.udacity.asteroidradar.api.NASAApi.retrofitPictureService
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.util.Constants.API_KEY
import com.udacity.asteroidradar.util.getActualDateFormatted
import com.udacity.asteroidradar.util.getNextSevenDaysDateFormatted
import com.udacity.asteroidradar.util.parseAsteroidsJsonResult
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application : Application) : AndroidViewModel(application) {

    private val db = getDatabase(application)

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
        val call = retrofitAsteroidsService.getAsteroids(getActualDateFormatted(), getNextSevenDaysDateFormatted(), API_KEY)
        call.enqueue( object: Callback<Any>{
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.e("NANIFALLIS", "Failure: $t")
                _asteroidsResponse.value = mutableListOf(Asteroid(0, "Failure","error",0.0,0.0,0.0,0.0,false ))
            }

            override fun onResponse(
                call: Call<Any>,
                response: Response<Any>
            ) {
                val json = JSONObject(Gson().toJson(response.body()!!))
                val list = parseAsteroidsJsonResult(json)
                db.asteroidDao.insertAllFromList(list)
                _asteroidsResponse.value = list
            }

        })

    }


}