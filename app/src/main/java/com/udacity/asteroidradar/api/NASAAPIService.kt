package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.util.Constants.API_KEY
import com.udacity.asteroidradar.util.Constants.BASE_URL
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val moshiRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

private val scalarRetrofit  = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface NASAAPIPictureService {
    @GET("planetary/apod?api_key=${API_KEY}")
    fun getPicturesOfTheDay() : Call<PictureOfDay>
}

interface NASAAPIAsteroidService {
    @GET("neo/rest/v1/feed?")
    fun getAsteroids(@Query(value="start_date") startDate : String, @Query(value="end_date") endDate : String, @Query(value="api_key") apiKey : String ) : Call<List<Asteroid>>
}

object NASAApi {
    val retrofitPictureService : NASAAPIPictureService by lazy { moshiRetrofit.create(NASAAPIPictureService::class.java) }
    val retrofitAsteroidsService :  NASAAPIAsteroidService by lazy { scalarRetrofit.create(NASAAPIAsteroidService::class.java) }
}