package com.example.androidapp.view_model

import com.example.androidapp.model.FGPlayer

// in java- FGPlayer Model
class ViewModel (Model: FGPlayer){
    // private FGPlayer myModel = Model;
    private var myModel : FGPlayer = Model;

    var VM_Throttle: Float
        get() = myModel.Throttle
        set(value){
          myModel.Throttle = value
        }

    var VM_Rudder: Float
        get() = myModel.Rudder
        set(value){
            myModel.Rudder = value
        }

    var VM_Elevator: Float
        get() = myModel.Elevator
        set(value){
            myModel.Elevator = value
        }

    var VM_Aileron: Float
        get() = myModel.Aileron
        set(value){
            myModel.Aileron = value
        }

    fun VM_Connect(ip: String, port: String){
        myModel.connect(ip, port)
    }

    fun VM_startEngine(){
        myModel.startEngine()
    }
}