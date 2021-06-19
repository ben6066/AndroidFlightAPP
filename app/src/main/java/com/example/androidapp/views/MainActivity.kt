package com.example.androidapp.views

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidapp.R
import com.example.androidapp.model.FGPlayer
import com.example.androidapp.view_model.ViewModel


class MainActivity : AppCompatActivity(), JoystickView.JoystickListener {
    var viewModel: ViewModel? = null
    var Model: FGPlayer?= null
    var ip: EditText? = null
    var port: EditText? = null
    var resetButton:Button? = null
    var connectionButton:Button? = null
    var throttleBar: SeekBar? = null
    var rudderBar: SeekBar? = null
    var joystick : JoystickView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        joystick = JoystickView(this)
        setContentView(R.layout.activity_main)
        ip = findViewById(R.id.ip);
        port = findViewById(R.id.port);
        connectionButton = findViewById(R.id.connect_btn)
        resetButton = findViewById(R.id.resetButton);
        throttleBar = findViewById(R.id.throttleBar)
        rudderBar = findViewById(R.id.rudderBar)
        Model = FGPlayer()
        viewModel = ViewModel(Model!!);
        connect()
        resetConnectionData()

        rudderBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                var progressAsFloat = progress.toFloat()
                var translatedProgress = (progressAsFloat - 50) / 50
                viewModel!!.VM_Rudder = translatedProgress
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        throttleBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                var progressAsFloat = progress.toFloat()
                var translatedProgress = progressAsFloat/100
                viewModel!!.VM_Throttle = translatedProgress
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun resetConnectionData() {
        resetButton?.setOnClickListener {
            ip?.setText("", TextView.BufferType.EDITABLE);
            port?.setText("", TextView.BufferType.EDITABLE);
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

    override fun onJoystickMoved(xPercent: Float, yPercent: Float) {
        viewModel?.VM_Elevator = yPercent * -1
        viewModel?.VM_Aileron = xPercent
    }
}