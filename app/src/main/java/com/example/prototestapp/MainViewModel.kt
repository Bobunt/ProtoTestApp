package com.example.prototestapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.termt.intellireader.modules.Miscellaneous
import com.termt.intellireader.modules.Unspecified
import com.termt.intellireader.transfer.IRTransfer
import com.termt.intellireader.transport.IRTransportSerial
import misc.buzzer.Buzzer
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.atomic.AtomicBoolean


class MainViewModel: ViewModel() {
//    private lateinit var transfer: IRTransfer
//    private lateinit var transport: IRTransportSerial

//    private var misc: Miscellaneous? = null
//    private var unspecified: Unspecified? = null

    val message: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val messageCard: MutableLiveData<String> by lazy { MutableLiveData<String>() }

//    private val continueSearch = AtomicBoolean(true)
//    private val isPollingThreadRunning = AtomicBoolean(false)
    private val cardHelper = CardHelper()

    var numBeep = 0

    fun addBeep() {
        numBeep++
        cardHelper.beep()
        message.postValue("Кол/во Beeep = " + numBeep)
    }

    fun initPlatformSpecificLib() {
        cardHelper.initPlatformSpecificLib()
    }

    fun startLookCard() {
        messageCard.postValue("attach a card")
        lookForCard()
    }

    private fun lookForCard() {
        val cardCallback = object : SDKService.CardCallback<String> {
            override fun onSuccess() {
                messageCard.postValue("onSuccess")
            }

            override fun onError(e: Exception) {
                messageCard.postValue("onError")
            }

            override fun onNotFound() {
                messageCard.postValue("onNotFound")
            }
        }
        cardHelper.searchRFCard(cardCallback)
    }

//    fun initPlatformSpecificLib() {
//        initTransfer()
//        unspecified = Unspecified(transfer)
//        misc = Miscellaneous(transfer)
//    }
//
//    private fun beep() {
//        try {
//            val notes = ArrayList<Buzzer.Note>()
//            notes.add(Buzzer.Note.newBuilder().setDurationMs(250).setFrequencyHz(523).setSilenceDurationMs(10).build())
//            misc?.makeSound(notes)
//        } catch (ex: IllegalStateException) {
//            ex.printStackTrace()
//        }
//    }

//    private fun initTransfer() {
//        val addr = getProp("persist.sys.irdev")
//        val baud = getProp("sys.irbaudrate")
//        transport = IRTransportSerial(
//            address = addr,
//            baud = baud.toInt(),
//            timeout = 10000
//        )
//        transfer = IRTransfer(transport)
//    }
//
//    private fun getProp(prop: String?): String {
//        var process: Process? = null
//        var bufferedReader: BufferedReader? = null
//        return try {
//            process =
//                ProcessBuilder().command("/system/bin/getprop", prop).redirectErrorStream(true)
//                    .start()
//            bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
//            var line = bufferedReader.readLine()
//            if (line == null)
//                line = ""
//            line
//        } catch (e: Exception) { "" }
//        finally {
//            if (bufferedReader != null) {
//                try { bufferedReader.close() }
//                catch (_: IOException) { }
//            }
//            process?.destroy()
//        }
//    }
//
//    fun searchRFCard(cardCallback: SDKService.CardCallback<String>) {
//        val rfSearchCallback = object : SDKService.RfSearchCallback<Int> {
//            override fun onSuccess() {
//                TODO("Not yet implemented")
//            }
//
//            override fun onNotFound() {
//                TODO("Not yet implemented")
//            }
//
//            override fun onError() {
//                TODO("Not yet implemented")
//            }
//
//            override fun onInterrupted() {
//                TODO("Not yet implemented")
//            }
//
//        }
//        pollingStarted()
//        val piccThread = PICC_Thread(rfSearchCallback, 10000)
//
//    }
//
//    private class PICC_Thread(
//        _callback: SDKService.RfSearchCallback<Int>,
//        _timeout: Long = 0
//    ) {
//        val callback: SDKService.RfSearchCallback<Int>
//        val timeout: Long
//
//        init {
//            callback = _callback
//            timeout = _timeout
//        }
//
//        public fun run() {
//        }
//    }
//
//    private fun pollingStopped(): Boolean {
//        return !continueSearch.get()
//    }
//
//    private fun pollingStarted() {
//        continueSearch.set(true)
//    }

}

