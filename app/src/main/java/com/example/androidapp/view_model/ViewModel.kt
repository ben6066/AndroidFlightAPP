package com.example.androidapp.view_model

import com.example.androidapp.model.FGPlayer

class ViewModel {
    // private FGPlayer myModel = Model;
    private var myModel : FGPlayer? = null

    init {
        myModel = FGPlayer()
    }

    var VM_Throttle: Float?
        get() = myModel?.Throttle
        set(value){
            if (value != null) {
                myModel?.Throttle = value
            }
        }

    var VM_Rudder: Float?
        get() = myModel?.Rudder
        set(value){
            if (value != null) {
                myModel?.Rudder = value
            }
        }

    var VM_Elevator: Float?
        get() = myModel?.Elevator
        set(value){
            if (value != null) {
                myModel?.Elevator = value
            }
        }

    var VM_Aileron: Float?
        get() = myModel?.Aileron
        set(value){
            if (value != null) {
                myModel?.Aileron = value
            }
        }

    fun VM_Connect(ip: String, port: String){
        myModel?.connect(ip, port)
    }

    fun VM_startEngine(){
        myModel?.startEngine()
    }
}