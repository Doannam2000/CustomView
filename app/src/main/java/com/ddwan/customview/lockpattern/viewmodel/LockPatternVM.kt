package com.ddwan.customview.lockpattern.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ddwan.customview.lockpattern.LockPatternRepository

class LockPatternVM(application: Application) : AndroidViewModel(application) {
    var password = MutableLiveData<ArrayList<Int>>()
    var lockPatternRepository = LockPatternRepository()
    var isFirstTime = MutableLiveData<Boolean>()

    fun getPassword(){
        lockPatternRepository.getPasswordFromSharedPreference(password,getApplication())
    }

    fun checkFirstTime(){
        lockPatternRepository.check(isFirstTime,getApplication())
    }
}