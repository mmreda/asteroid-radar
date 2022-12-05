package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.PictureOfDayApi
import com.udacity.asteroidradar.api.getStartDate
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getAllAsteroids()) {
        it.asDomainModel()
    }


    fun getTodayAsteroids(todayDate: String) = database
                        .asteroidDao.getTodayAsteroids(todayDate)

    fun getWeekAsteroids(startDate: String, endDate: String) = database
                        .asteroidDao.getWeekAsteroids(startDate, endDate)


    fun deleteAll() = database.asteroidDao.deleteAll()

    suspend fun refreshAsteroid(startDate: String, endDate: String, apiKey: String) {
        withContext(Dispatchers.IO) {
            try {
                val asteroid = AsteroidApi.retrofitService.getAsteroid(startDate, endDate, apiKey)
                val data = parseAsteroidsJsonResult(JSONObject(asteroid))
                database.asteroidDao.insertAll(*data.asDatabaseModel())
            } catch (_: Exception) {

            }
        }
    }

    suspend fun pictureOfDay(apiKey: String) = PictureOfDayApi.retrofitService.getPictureOfDay(apiKey)
}