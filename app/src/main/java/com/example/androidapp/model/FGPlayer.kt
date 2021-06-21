package com.example.androidapp.model

import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class FGPlayer {
    private var throttle: Float = 0f
    private var elevator: Float = 0f
    private var rudder: Float = 0f
    private var aileron: Float = 0f
    private var client: Socket? = null
    private var out: PrintWriter? = null
    private val dispatchQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()

    constructor() {
        Thread {
            while (true) {
                try {
                    dispatchQueue.take().run()
                } catch (e: InterruptedException) {
                    // okay, just terminate the dispatcher
                }
            }
        }.start()
    }

    var Throttle: Float
        get() = throttle
        set(value) {
            throttle = value

            if (out != null) {
                dispatchQueue.put(Runnable {
                    var throttleAsString = throttle.toString()
                    out?.print("set /controls/engines/current-engine/throttle $throttleAsString \r\n")
                    out?.flush()
                })
            }
        }

    var Rudder: Float
        get() = rudder
        set(value) {
            rudder = value

            if (out != null) {
                dispatchQueue.put(Runnable {
                    var rudderAsString = rudder.toString()
                    out?.print("set /controls/flight/rudder $rudderAsString \r\n")
                    out?.flush()
                })
            }
        }

    var Elevator: Float
        get() = elevator
        set(value) {
            elevator = value

            if (out != null) {
                dispatchQueue.put(Runnable {
                    var elevatorAsString = elevator.toString()
                    out?.print("set /controls/flight/elevator $elevatorAsString \r\n")
                    out?.flush()
                })
            }
        }

    var Aileron: Float
        get() = aileron
        set(value) {
            aileron = value

            if (out != null) {
                dispatchQueue.put(Runnable {
                    var aileronAsString = aileron.toString()
                    out?.print("set /controls/flight/aileron $aileronAsString \r\n")
                    out?.flush()
                })
            }
        }

     fun connect(ip: String, port: String) {
        dispatchQueue.put(Runnable {
            client = Socket(ip, (port.toInt()))
            out = PrintWriter(client!!.getOutputStream(), true)
        })
    }

    fun startEngine(){
        dispatchQueue.put(Runnable {
            out?.print("set /consumables/fuel/tank[0]/selected 1 \r\n");
            out?.flush()
            out?.print("set /consumables/fuel/tank[1]/selected 1 \r\n");
            out?.flush()
            out?.print("set /consumables/fuel/tank[2]/selected 0 \r\n");
            out?.flush()
            out?.print("set /consumables/fuel/tank[3]/selected 0 \r\n");
            out?.flush()
            out?.print("set /controls/engines/current-engine/mixture 1 \r\n");
            out?.flush()
            out?.print("set /engines/active-engine/carb_ice 0.0 \r\n");
            out?.flush()
            out?.print("set /engines/active-engine/carb_icing_rate 0.0 \r\n");
            out?.flush()
            out?.print("set /controls/engines/current-engine/carb-heat 1 \r\n");
            out?.flush()
            out?.print("set /engines/active-engine/running 1 \r\n");
            out?.flush()
            out?.print("set /engines/active-engine/auto-start 1 \r\n");
            out?.flush()
            out?.print("set /engines/active-engine/cranking 1 \r\n");
            out?.flush()
            out?.print("set /controls/engines/engine[0]/primer 3 \r\n");
            out?.flush()
            out?.print("set /controls/engines/engine[0]/primer-lever 0 \r\n");
            out?.flush()
            out?.print("set /controls/engines/current-engine/throttle 0.2 \r\n");
            out?.flush()
            out?.print("set /controls/flight/elevator-trim -0.03 \r\n");
            out?.flush()
            out?.print("set /controls/switches/dome-red 0 \r\n");
            out?.flush()
            out?.print("set /controls/switches/dome-white 0 \r\n");
            out?.flush()
            out?.print("set /controls/switches/magnetos 3 \r\n");
            out?.flush()
            out?.print("set /controls/switches/master-bat 1 \r\n");
            out?.flush()
            out?.print("set /controls/switches/master-alt 1 \r\n");
            out?.flush()
            out?.print("set /controls/switches/master-avionics 1 \r\n");
            out?.flush()
            out?.print("set /controls/switches/starter 1 \r\n");
            out?.flush()
            out?.print("set /controls/lighting/beacon 1 \r\n");
            out?.flush()
            out?.print("set /controls/lighting/taxi-light 0 \r\n");
            out?.flush()
            out?.print("set /fdm/jsbsim/running 0 \r\n");
            out?.flush()
            out?.print("set /fdm/jsbsim/inertia/pointmass-weight-lbs[0] 170 \r\n");
            out?.flush()
            out?.print("set /fdm/jsbsim/inertia/pointmass-weight-lbs[1] 0 \r\n");
            out?.flush()
            out?.print("set /sim/model/c172p/securing/tiedownL-visible 0 \r\n");
            out?.flush()
            out?.print("set /sim/model/c172p/securing/tiedownR-visible 0 \r\n");
            out?.flush()
            out?.print("set /sim/model/c172p/securing/tiedownT-visible 0 \r\n");
            out?.flush()
            out?.print("set /sim/model/c172p/securing/pitot-cover-visible 0 \r\n");
            out?.flush()
            out?.print("set /sim/model/c172p/securing/chock 0 \r\n");
            out?.flush()
            out?.print("set /sim/model/c172p/securing/cowl-plugs-visible 0 \r\n");
            out?.flush()
            out?.print("set /sim/model/c172p/cockpit/control-lock-placed 0 \r\n");
            out?.flush()
            out?.print("set /controls/gear/gear-down-command 1 \r\n");
            out?.flush()
            out?.print("set /sim/model/c172p/brake-parking 0 \r\n");
            out?.flush()
        })
    }
}