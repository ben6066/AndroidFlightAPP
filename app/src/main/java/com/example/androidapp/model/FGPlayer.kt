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
}