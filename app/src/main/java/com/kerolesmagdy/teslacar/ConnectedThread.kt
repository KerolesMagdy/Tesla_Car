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
    var sendState = false
    var data = "S"
    private var inputStream: InputStream? = null
    private var outStream: OutputStream? = null

    fun startSend() {
        outStream = socket.outputStream
        sendState = true
        while (sendState) {
            write(data.toByteArray())
            sleep(10)
        }
    }

    fun stopSend() {
        sendState = false
    }

    fun write(input: ByteArray) {
        try {
            Log.e("socket send : ", "" + input.toString())
//            outStream.write(input)
        } catch (e: IOException) {
        }
    }

    fun cancel() {
        try {
            sendState = false
            socket.close()
            Log.e("closed socket", " :   success")
            Toast.makeText(context, "connection closed", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Log.e("closed socket", " :   failed")
        }
    }

}