package com.ddwan.customview.lockpattern

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.MutableLiveData

class LockPatternRepository {
    fun getPasswordFromSharedPreference(
        liveData: MutableLiveData<ArrayList<Int>>,
        appliaction: Application,
    ) {
        val shared = appliaction.getSharedPreferences("PASSWORD", MODE_PRIVATE)
        val array = ArrayList<Int>()
        val size = shared.getInt("size",0)
        for (i in 0..size)
            array.add(shared.getInt("key$i",0))
        liveData.value = array
    }

    fun check(liveData: MutableLiveData<Boolean>,
              appliaction: Application){
        val shared = appliaction.getSharedPreferences("PASSWORD", MODE_PRIVATE)
        val size = shared.getInt("size",0)
        liveData.value = (size != 0)
    }
}