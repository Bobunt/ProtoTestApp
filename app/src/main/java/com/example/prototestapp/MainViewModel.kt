package com.example.prototestapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.termt.intellireader.modules.Miscellaneous
import com.termt.intellireader.modules.Unspecified
import com.termt.intellireader.transfer.IRTransfer
import com.termt.intellireader.transport.IRTransportSerial
import misc.buzzer.Buzzer
import misc.leds.LedsOuterClass
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class MainViewModel: ViewModel() {
    private lateinit var transfer: IRTransfer
    private lateinit var transport: IRTransportSerial
    private var misc: Miscellaneous? = null
    private var unspecified: Unspecified? = null
    val message: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    var numBeep = 0

    fun addBeep() {
        numBeep++
        beep()
        message.postValue("Кол/во Beeep = " + numBeep)
    }

    private fun beep() {
        try {
            val notes = ArrayList<Buzzer.Note>()
            notes.add(Buzzer.Note.newBuilder().setDurationMs(250).setFrequencyHz(523).setSilenceDurationMs(10).build())
            misc?.makeSound(notes)
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
    }

    private fun initTransfer() {
        val addr = getProp("persist.sys.irdev")
        val baud = getProp("sys.irbaudrate")
            transport = IRTransportSerial(
                address = addr,
                baud = baud.toInt(),
                timeout = 10000
            )
        transfer = IRTransfer(transport)
    }

    fun initPlatformSpecificLib() {
        initTransfer()
        unspecified = Unspecified(transfer)
        misc = Miscellaneous(transfer)
    }

    private fun getProp(prop: String?): String {
        var process: Process? = null
        var bufferedReader: BufferedReader? = null
        return try {
            process =
                ProcessBuilder().command("/system/bin/getprop", prop).redirectErrorStream(true)
                    .start()
            bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            var line = bufferedReader.readLine()
            if (line == null)
                line = ""
            line
        } catch (e: Exception) { "" }
        finally {
            if (bufferedReader != null) {
                try { bufferedReader.close() }
                catch (_: IOException) { }
            }
            process?.destroy()
        }
    }

}

