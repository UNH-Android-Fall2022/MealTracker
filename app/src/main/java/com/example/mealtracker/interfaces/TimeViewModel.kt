package com.example.mealtracker.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mealtracker.repository.DataRepository
import com.example.mealtracker.userProfie.Time

class TimeViewModel : ViewModel() {

    private val repository: DataRepository
    private val _allTimes = MutableLiveData<List<Time>>()
    val allTimes: LiveData<List<Time>> = _allTimes

    init {
        repository = DataRepository().getInstance()
        repository.loadData(_allTimes, "a", "")
    }

}