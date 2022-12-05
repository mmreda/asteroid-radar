package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.domain.Asteroid

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM Asteroid ORDER BY  closeApproachDate ASC")
    fun getAllAsteroids(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM Asteroid WHERE closeApproachDate == :todayDate ORDER BY closeApproachDate ASC")
    fun getTodayAsteroids(todayDate: String): LiveData<List<Asteroid>>

    @Query("SELECT * FROM Asteroid WHERE closeApproachDate  BETWEEN :startDate AND :endDate ORDER BY closeApproachDate ASC")
    fun getWeekAsteroids(startDate: String, endDate: String): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: Asteroid)

    @Query("DELETE FROM Asteroid")
   fun deleteAll()
}