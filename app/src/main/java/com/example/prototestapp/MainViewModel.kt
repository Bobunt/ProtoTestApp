package com.example.prototestapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val sumBeepMessage: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val pollingMessage: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val pollingButton: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    private val cardHelper = Helper()
    private var numBeep = 0

    fun addBeep() {
        numBeep++
        cardHelper.beep()
        sumBeepMessage.postValue(numBeep.toString())
    }

    fun initPlatformSpecificLib() {
        cardHelper.initPlatformSpecificLib()
    }

    fun startLookCard() {
        pollingButton.postValue("polling active")
        lookForCard()
    }

    private fun lookForCard() {
        val cardCallback = object : SDKService.CardCallback<String> {
            override fun onSuccess() {
                pollingMessage.postValue("onSuccess")
                pollingButton.postValue("enable pulling")
            }

            override fun onError(e: Exception) {
                pollingMessage.postValue("onError")
                pollingButton.postValue("enable pulling")
            }

        }
        cardHelper.searchRFCard(cardCallback)
    }
}

