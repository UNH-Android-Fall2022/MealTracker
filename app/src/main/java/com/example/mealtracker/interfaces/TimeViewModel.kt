package com.example.mealtracker.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mealtracker.repository.DataRepository
import com.example.mealtracker.userProfie.Time


// https://www.youtube.com/watch?v=VVXKVFyYQdQ&ab_channel=Foxandroid
class TimeViewModel(val date: String, val userId: String) : ViewModel() {

    private val repository: DataRepository
    private val _allTimes = MutableLiveData<List<Time>>()
    val allTimes: LiveData<List<Time>> = _allTimes

    init {
        repository = DataRepository().getInstance()
        repository.loadData(_allTimes, date, userId)
    }

    fun getList(): MutableLiveData<List<Time>> {
        return _allTimes
    }

}

class MyViewModelFactory(val date: String, val userId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        modelClass.getConstructor(String::class.java, String::class.java)
            .newInstance(date, userId)
}