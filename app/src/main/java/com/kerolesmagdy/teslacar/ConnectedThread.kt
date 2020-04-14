package com.kerolesmagdy.teslacar

import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import android.widget.Toast
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ConnectedThread(
    private var socket: BluetoothSocket,
    private val context: Context
) : Thread() {
    private var sendState = false
    var mode = DataSheet.C
    var data = DataSheet.C
    //    private var inputStream: InputStream? = null
    private var outStream: OutputStream? = null

    init {
        outStream = socket.outputStream
        Log.e("connectedThread ", " created")
    }

    override fun run() {

    }

    fun startSend() {
        sendState = true
        while (sendState) {
            write(data.toByteArray())
            sleep(10)
        }
        cancel()

    }

    fun stopSend() {
        sendState = false
    }

    fun write(input: ByteArray) {
        try {
            Log.e("socket send : ", "" + input.toString())
            if (mode == DataSheet.C)
                outStream?.write(input)
            else
                outStream?.write(mode.toByteArray())
        } catch (e: IOException) {
        }
    }

    fun cancel() {
        try {
            socket.close()
            sendState = false
            Log.e("closed socket", " :   success")
            Toast.makeText(context, "connection closed", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Log.e("closed socket", " :   failed")
        }
    }

    fun getSendStat(): Boolean = sendState

}