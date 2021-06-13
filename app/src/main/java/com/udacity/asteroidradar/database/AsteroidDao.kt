package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.models.Asteroid

@Dao
interface AsteroidDao {
    @Insert
    suspend fun insert(night: Asteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg videos: Asteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFromList(listvideos: List<Asteroid>)

    @Update
    suspend fun update(night: Asteroid)

    @Query("SELECT * FROM asteroid WHERE id = :key")
    suspend fun get(key: Long): Asteroid?

    @Query("SELECT * FROM asteroid ORDER BY close_approach_date")
    fun getAsteroids(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid WHERE close_approach_date = :today")
    fun getTodayAsteroids(today: String): LiveData<List<Asteroid>>

    @Query("DELETE FROM asteroid WHERE close_approach_date NOT IN (:actualWeek)")
    suspend fun deleteOldAsteroids(actualWeek: ArrayList<String>)


}