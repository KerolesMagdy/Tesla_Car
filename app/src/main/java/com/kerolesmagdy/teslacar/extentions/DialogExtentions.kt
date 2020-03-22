package com.kerolesmagdy.teslacar.extentions

import android.bluetooth.BluetoothDevice
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kerolesmagdy.teslacar.R
import com.kerolesmagdy.teslacar.adapter.DevicesAdapter
import com.kerolesmagdy.teslacar.adapter.DevicesCallBack

fun AlertDialog.showLoadingDialog(dialogResource: Int, loadingImgResource: Int) {

    var loadingDialog: View = LayoutInflater.from(context).inflate(dialogResource, null)
    Glide.with(context)
        .load(loadingImgResource)
        .into(loadingDialog.findViewById(R.id.loadingDialog_loadImg))
//    var loadingText: TextView = loadingDialog.findViewById(R.id.loadingDialog_loadText)
//    loadingText.text = "Auth..."
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    this.setView(loadingDialog)
    this.setCancelable(true)
    this.show()
}

fun AlertDialog.showBluetoothListDialog(
    devicesTitle: String,
    devicesList: List<BluetoothDevice>,
    devicesCallBack: DevicesCallBack
) {

    val loadingDialog: View =
        LayoutInflater.from(context).inflate(R.layout.bluetooth_devices_view, null)
    val recyclerView: RecyclerView = loadingDialog.findViewById(R.id.devices_category_recyclerView)
    val title: TextView = loadingDialog.findViewById(R.id.devices_category_title)
    title.text = devicesTitle
    val adapter: DevicesAdapter = DevicesAdapter(context, devicesList, devicesCallBack)
    recyclerView.adapter = adapter


//    var loadingText: TextView = loadingDialog.findViewById(R.id.loadingDialog_loadText)
//    loadingText.text = "Auth..."
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    this.setView(loadingDialog)
    this.setCancelable(true)
    this.show()
}