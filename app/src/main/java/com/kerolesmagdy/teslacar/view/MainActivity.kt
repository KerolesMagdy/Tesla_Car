package com.kerolesmagdy.teslacar.view
        
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kerolesmagdy.teslacar.ConnectedThread
import com.kerolesmagdy.teslacar.DataSheet
import com.kerolesmagdy.teslacar.R
import com.kerolesmagdy.teslacar.adapter.DevicesCallBack
import com.kerolesmagdy.teslacar.extentions.showBluetoothListDialog
import com.kerolesmagdy.teslacar.extentions.showLoadingDialog
import com.kerolesmagdy.teslacar.ui.ConvertButton
import com.kerolesmagdy.teslacar.ui.GameJoystickView
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.name
    private var bluetoothStateImg: ConvertButton? = null
    private var pairedDevicesBtn: Button? = null
    private var unPairedDevicesBtn: Button? = null
    private var carMode: ConvertButton? = null
    private var shootBtn: ImageView? = null
    private var upBtn: ImageButton? = null
    private var downBtn: ImageButton? = null
    private var rightBtn: ImageButton? = null
    private var leftBtn: ImageButton? = null
    private var mJoystickView: GameJoystickView? = null

    // Our main handler that will receive callback notifications
    private val mHandler: Handler? = null

    // bluetooth background worker thread to send and receive data
    private var connectedThread: ConnectedThread? = null

    // bi-directional client-to-client data path
    private var BTSocket: BluetoothSocket? = null
    private var BTAdapter: BluetoothAdapter? = null
    private var connectedBluetoothDevice: BluetoothDevice? = null
    var pairedBTDevices = ArrayList<BluetoothDevice>()
    var unPairedBTDevices = ArrayList<BluetoothDevice>()
    private val BTMODULEUUID =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // "random" unique identifier
    private var loadingDialog: AlertDialog? = null
    private var pairedDevicesDialog: AlertDialog? = null
    private var unPairedDevicesDialog: AlertDialog? = null
    private var paireddevicesCallBack: DevicesCallBack? = null
    private var unPaireddevicesCallBack: DevicesCallBack? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initJoystickEvent()
        initArmEvents()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_setting -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.main_menu_doc -> {
                startActivity(Intent(this, DocActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() {
        mJoystickView = findViewById(R.id.joystick_view)
        carMode = findViewById(R.id.car_mode)
        pairedDevicesBtn = findViewById(R.id.show_paired_devices)
        unPairedDevicesBtn = findViewById(R.id.show_unpaired_devices)
        bluetoothStateImg = findViewById(R.id.bluetooth_state_btn)
        upBtn = findViewById(R.id.up_btn)
        downBtn = findViewById(R.id.down_btn)
        rightBtn = findViewById(R.id.right_btn)
        leftBtn = findViewById(R.id.left_btn)
        shootBtn = findViewById(R.id.shoot_btn)

        pairedDevicesBtn?.setOnClickListener {
            getAllPairedDevices()
        }
        BTAdapter = BluetoothAdapter.getDefaultAdapter()
        loadingDialog = AlertDialog.Builder(this).create()
        pairedDevicesDialog = AlertDialog.Builder(this).create()
        unPairedDevicesDialog = AlertDialog.Builder(this).create()
        paireddevicesCallBack = object : DevicesCallBack {
            override fun onItemClicked(device: BluetoothDevice) {
                connect(device)
            }
        }
        unPaireddevicesCallBack = object : DevicesCallBack {
            override fun onItemClicked(device: BluetoothDevice) {

            }
        }
        carMode?.setOnClickListener {
            carMode?.changeState()
        }

    }

    private fun initJoystickEvent() {
        mJoystickView!!.setOnJoystickMoveListener({ angle, power, direction ->
            when (direction) {
                GameJoystickView.UP_DIRECTION -> {
                    changeData(DataSheet.F)
                    Log.e("______________________", "UP_DIRECTION   $angle   $power")
                }
                GameJoystickView.LEFT_DIRECTION -> {
                    changeData(DataSheet.L)
                    Log.e("______________________", "LEFT_DIRECTION    $angle   $power")
                }
                GameJoystickView.DOWN_DIRECTION -> {
                    changeData(DataSheet.B)
                    Log.e("______________________", "DOWN_DIRECTION     $angle   $power")
                }
                GameJoystickView.RIGHT_DIRECTION -> {
                    changeData(DataSheet.R)
                    Log.e("______________________", "RIGHT_DIRECTION     $angle   $power")
                }

                GameJoystickView.VIEW_CENTER_CIRCLE -> {
                    changeData(DataSheet.C)
                    Log.e("______________________", "VIEW_CENTER_CIRCLE")
                }
            }
        }, GameJoystickView.DEFAULT_LOOP_INTERVAL)
    }

    private fun initArmEvents() {
        upBtn?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                changeData(DataSheet.u)

            } else if (event.action == MotionEvent.ACTION_UP) {
                if (carMode!!.isActionEnable())
                    changeData(DataSheet.C)
                else
                    changeData(DataSheet.A)
            }
            true
        }

        downBtn?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                changeData(DataSheet.d)

            } else if (event.action == MotionEvent.ACTION_UP) {
                if (carMode!!.isActionEnable())
                    changeData(DataSheet.C)
                else
                    changeData(DataSheet.A)
            }
            true
        }

        rightBtn?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                changeData(DataSheet.r)

            } else if (event.action == MotionEvent.ACTION_UP) {
                if (carMode!!.isActionEnable())
                    changeData(DataSheet.C)
                else
                    changeData(DataSheet.A)
            }
            true
        }

        leftBtn?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                changeData(DataSheet.l)

            } else if (event.action == MotionEvent.ACTION_UP) {
                if (carMode!!.isActionEnable())
                    changeData(DataSheet.C)
                else
                    changeData(DataSheet.A)
            }
            true
        }

        shootBtn?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (connectedThread != null)
                    connectedThread?.write(DataSheet.f.toByteArray())
                if (carMode!!.isActionEnable())

                    changeData(DataSheet.C)
                else
                    changeData(DataSheet.A)
            }
            true
        }

    }

    private fun getAllPairedDevices() {
        showProgress()
        if (BTAdapter == null) {
            hideProgress()
            Toast.makeText(
                applicationContext,
                "Device doesn't Support Bluetooth",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            hideProgress()
            if (!BTAdapter!!.isEnabled) {
                val enableAdapter = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableAdapter, 0)
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            } else {
                val bondedDevices =
                    BTAdapter?.bondedDevices
                if (bondedDevices!!.isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "Please Pair the Device first",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    pairedBTDevices.clear()
                    for (iterator in bondedDevices) {
                        Log.e(
                            "device   :   ",
                            "" + iterator.name + "      " + iterator.address
                        )
                        pairedBTDevices.add(iterator)
                    }
                    pairedDevicesDialog?.showBluetoothListDialog(
                        resources.getString(R.string.paired_devices),
                        pairedBTDevices,
                        paireddevicesCallBack!!
                    )
                }
            }
        }
    }


    @Throws(IOException::class)
    private fun createBluetoothSocket(device: BluetoothDevice): BluetoothSocket? {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID)
    }

    private fun connect(connectedBT: BluetoothDevice) {
        showProgress()
        if (BTAdapter != null && !BTAdapter!!.isEnabled) {
            showProgress()
            Toast.makeText(baseContext, "Bluetooth not on", Toast.LENGTH_SHORT).show()
            return
        }
        Log.e("Connecting...", "")
        object : Thread() {
            override fun run() {
                runOnUiThread { bluetoothStateImg?.changeState() }
                var fail = false
                val device: BluetoothDevice =
                    BTAdapter!!.getRemoteDevice(connectedBT?.address)
                Log.e("111111111111111", "")
                try {
                    BTSocket = createBluetoothSocket(device)
                    Log.e("bluetooth Connected", "")
                } catch (e: IOException) {
                    fail = true
                    Log.e("Socket creation failed", "" + e.message)
                }
                try {
                    BTSocket?.connect()
                } catch (e: IOException) {
                    Log.e("Socket failed", "" + e.message)
                    try {
                        fail = true
                        BTSocket?.close()
                    } catch (e2: IOException) {
                        //insert code to deal with this
                        Log.e("Socket creation failed", "" + e2.message)
                        Toast.makeText(
                            baseContext,
                            "Socket creation failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                if (!fail) {
                    if (connectedThread != null)
                        connectedThread =
                            ConnectedThread(
                                BTSocket!!,
                                applicationContext
                            )

                    connectedThread?.start()
                    connectedThread?.startSend()
                } else {
                    Log.e("Socket connection", " failed")
                }
                hideProgress()
            }
        }.start()

    }

    private fun changeData(input: String) {
        if (connectedThread != null) {
            connectedThread?.data = input
        }
    }

    private fun cancelConnection() {
        connectedThread?.cancel()
        connectedThread = null
    }

    private fun showProgress() {
        if (loadingDialog?.isShowing == false)
            loadingDialog!!.showLoadingDialog(
                R.layout.loading_dialog_view,
                R.drawable.sane_spanish_crayfish_small
            )
    }

    private fun hideProgress() {
        if (loadingDialog?.isShowing!!)
            loadingDialog!!.dismiss()
    }

}
