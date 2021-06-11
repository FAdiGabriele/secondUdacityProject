package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import com.udacity.asteroidradar.api.NASAApi
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.util.Constants
import com.udacity.asteroidradar.util.getActualDateFormatted
import com.udacity.asteroidradar.util.getNextSevenDaysDateFormatted
import com.udacity.asteroidradar.util.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getAsteroids()) { it }

    suspend fun refreshAsteroids() {

        withContext(Dispatchers.IO) {
            val call = NASAApi.retrofitAsteroidsService.getAsteroids(getActualDateFormatted(), getNextSevenDaysDateFormatted(),
                Constants.API_KEY
            ).await()
            //TODO: funziona solo se la chiamata non fallisce all'inizio
            val json = JSONObject(Gson().toJson(call))
            val asteroidList = parseAsteroidsJsonResult(json)
            database.asteroidDao.insertAllFromList(asteroidList)
        }


    }

}