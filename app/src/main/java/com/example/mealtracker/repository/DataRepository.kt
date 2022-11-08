package com.example.mealtracker.repository

import androidx.lifecycle.MutableLiveData
import com.example.mealtracker.userProfie.Time
import com.google.firebase.database.*

class DataRepository {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Users")

    @Volatile
    private var INSTANCE: DataRepository? = null

    fun getInstance(): DataRepository {
        return INSTANCE ?: synchronized(this) {
            val instance = DataRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadData(timeList: MutableLiveData<List<Time>>, date: String, userId: String) {
        databaseReference.child(userId).child(date)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    try {

                        val _timelist: List<Time> = snapshot.children.map { dataSnapshot ->
                            dataSnapshot.getValue(Time::class.java)!!
                        }
                        timeList.postValue(_timelist)

                    } catch (e: Exception) {

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }
}