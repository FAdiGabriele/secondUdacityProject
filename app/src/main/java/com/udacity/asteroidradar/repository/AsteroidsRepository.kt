package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import com.udacity.asteroidradar.api.NASAApi
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.util.Constants
import com.udacity.asteroidradar.util.getActualDateFormatted
import com.udacity.asteroidradar.util.getNextSevenDaysDateFormatted
import com.udacity.asteroidradar.util.parseAsteroidsJsonResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import kotlinx.coroutines.withContext as withContext

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getAsteroids()) { it }

    fun refreshAsteroids() {

        val call = NASAApi.retrofitAsteroidsService.getAsteroids(getActualDateFormatted(), getNextSevenDaysDateFormatted(), Constants.API_KEY)

            call.enqueue(object :Callback<Any>{
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    val json = JSONObject(Gson().toJson(response.body()))
                    val asteroidList = parseAsteroidsJsonResult(json)
                    insertAll(asteroidList)
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {}
            })
    }

    fun insertAll(asteroidList: List<Asteroid>){
        CoroutineScope(Dispatchers.IO).launch {
            database.asteroidDao.insertAllFromList(asteroidList)
        }
    }

}