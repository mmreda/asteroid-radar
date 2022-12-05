package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.api.getEndDate
import com.udacity.asteroidradar.api.getStartDate
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.domain.asDomainModel
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {
    enum class NasaApiStatus { LOADING, DONE, ERROR }

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    val asteroidList: LiveData<List<Asteroid>> = asteroidRepository.asteroids

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _status = MutableLiveData<NasaApiStatus>()
    val status: LiveData<NasaApiStatus>
        get() = _status

    private val _navigateToDetails = MutableLiveData<Asteroid?>()
    val navigateToDetails: LiveData<Asteroid?>
        get() = _navigateToDetails

    init {
        getPictureOfDay()
        refreshAsteroid()
    }

    fun getTodayAsteroids(): LiveData<List<Asteroid>> {
        return Transformations.map(asteroidRepository.getTodayAsteroids(getStartDate())) {
            it.asDomainModel()
        }
    }

    fun getWeekAsteroids(): LiveData<List<Asteroid>> {
        return Transformations.map(asteroidRepository.getWeekAsteroids(getStartDate(), getEndDate())) {
            it.asDomainModel()
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToDetails.value = asteroid
    }

    fun displayAsteroidDetailsDone() {
        _navigateToDetails.value = null
    }

    private fun refreshAsteroid() {
        viewModelScope.launch {
            try {
                asteroidRepository.refreshAsteroid(getStartDate(), getEndDate(), API_KEY)
            } catch (_:Exception) {

            }
        }
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            _status.value = NasaApiStatus.LOADING
            try {
                _pictureOfDay.value = asteroidRepository.pictureOfDay(API_KEY)
                _status.value = NasaApiStatus.DONE
            } catch (_:Exception) {
                _status.value = NasaApiStatus.ERROR
            }
        }
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}