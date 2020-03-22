package com.kerolesmagdy.teslacar.adapter

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kerolesmagdy.teslacar.R

class DevicesAdapter(
    val context: Context,
    var devicesList: List<BluetoothDevice>,
    var deviceCallBack: DevicesCallBack
) :
    RecyclerView.Adapter<DevicesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesViewHolder =
        DevicesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.device_item_layout,
                null
            )
        )

    override fun getItemCount(): Int {
        return devicesList.size
    }

    override fun onBindViewHolder(holder: DevicesViewHolder, position: Int) {
        holder.bind(devicesList.get(position), deviceCallBack)
    }

}

class DevicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView? = null
    fun bind(device: BluetoothDevice, callBack: DevicesCallBack) {
        name = itemView.findViewById(R.id.device_item_layout_bluetooth_name)
        name?.text = device.name
        itemView.setOnClickListener {
            callBack.onItemClicked(device)
        }
    }
}

interface DevicesCallBack {
    fun onItemClicked(device: BluetoothDevice)
}