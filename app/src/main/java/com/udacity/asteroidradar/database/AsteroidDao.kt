package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.models.Asteroid

@Dao
interface AsteroidDao{
    @Insert
    suspend fun insert(night: Asteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg videos: Asteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFromList(listvideos: List<Asteroid>)

    @Update
    suspend fun update(night: Asteroid)

    @Query("SELECT * from asteroid WHERE id = :key")
    suspend fun get(key: Long): Asteroid?

    @Query("SELECT * from asteroid")
    fun getAsteroids(): LiveData<List<Asteroid>>

    @Query("DELETE FROM asteroid WHERE close_approach_date ")
    suspend fun clearOldAsteroid()


}