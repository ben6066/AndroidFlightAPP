package com.example.androidapp.views

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.androidapp.R
import com.example.androidapp.view_model.ViewModel


class MainActivity : AppCompatActivity(), JoystickView.JoystickListener {
    private var viewModel: ViewModel? = null
    private var ip: EditText? = null
    private var port: EditText? = null
    private var resetButton:Button? = null
    private var connectionButton:Button? = null
    private var throttleBar: SeekBar? = null
    private var rudderBar: SeekBar? = null
    private var joystick : JoystickView?= null
    private var engineStartButton : ImageButton?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        joystick = JoystickView(this)
        setContentView(R.layout.activity_main)
        ip = findViewById(R.id.ip)
        port = findViewById(R.id.port)
        connectionButton = findViewById(R.id.connect_btn)
        resetButton = findViewById(R.id.resetButton)
        throttleBar = findViewById(R.id.throttleBar)
        rudderBar = findViewById(R.id.rudderBar)
        viewModel = ViewModel()
        engineStartButton = findViewById(R.id.engine_start)
        connect()
        resetConnectionData()
        startEngine()

        rudderBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                val progressAsFloat = progress.toFloat()
                val translatedProgress = (progressAsFloat - 50) / 50
                viewModel!!.VM_Rudder = translatedProgress
            }

            override fun onStartTrackingTouch(seek: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        throttleBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                val progressAsFloat = progress.toFloat()
                val translatedProgress = progressAsFloat/100
                viewModel!!.VM_Throttle = translatedProgress
            }

            override fun onStartTrackingTouch(seek: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun resetConnectionData() {
        resetButton?.setOnClickListener {
            ip?.setText("", TextView.BufferType.EDITABLE)
            port?.setText("", TextView.BufferType.EDITABLE)
        }
    }

    private fun connect() {
        connectionButton?.setOnClickListener {
            // Hide keyboard after clicking "Connect" button
            val imm: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isActive)
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)

            //Call VM with ip and port
            viewModel?.VM_Connect(ip?.text.toString(), (port?.text.toString()))
        }
    }

    private fun startEngine(){
        engineStartButton?.setOnClickListener{
            viewModel?.VM_startEngine()
        }
    }

    override fun onJoystickMoved(xPercent: Float, yPercent: Float) {
        viewModel?.VM_Elevator = yPercent * (-1)
        viewModel?.VM_Aileron = xPercent
    }
}