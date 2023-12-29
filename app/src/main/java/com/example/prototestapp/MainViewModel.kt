package com.example.prototestapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class MainViewModel: ViewModel() {
    val message: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    var numBeep = 0

    fun addBeep() {
        numBeep++
        message.postValue("Кол/во Beeep = " + numBeep)
    }
}

