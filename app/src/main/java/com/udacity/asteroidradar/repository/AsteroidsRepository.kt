package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import com.udacity.asteroidradar.api.NASAApi
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.util.*
import com.udacity.asteroidradar.util.getTheNextSeventhDayFormatted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.withContext as withContext

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    val dailyAsteroids: LiveData<List<Asteroid>> = Transformations.map(
            database.asteroidDao.getTodayAsteroids(getActualDateFormatted())) { it }
    val weeklyAsteroids: LiveData<List<Asteroid>> = Transformations.map(
            database.asteroidDao.getAsteroids()) { it }

    fun refreshAsteroids() {

        val call = NASAApi.retrofitAsteroidsService.getAsteroids(getActualDateFormatted(), getTheNextSeventhDayFormatted(), Constants.API_KEY)

        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                val json = JSONObject(Gson().toJson(response.body()))
                val asteroidList = parseAsteroidsJsonResult(json)
                insertAll(asteroidList)
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {}
        })
    }

    private fun insertAll(asteroidList: List<Asteroid>) {
        CoroutineScope(Dispatchers.IO).launch {
            database.asteroidDao.insertAllFromList(asteroidList)
        }
    }

    suspend fun deleteOldAsteroids() {
        withContext(Dispatchers.IO) {
            val actualWeek = getNextSevenDaysFormattedDates()
            database.asteroidDao.deleteOldAsteroids(actualWeek)
        }
    }

}