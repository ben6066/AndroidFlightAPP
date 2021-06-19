package com.example.androidapp.views

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.pow
import kotlin.math.sqrt


class JoystickView : SurfaceView, SurfaceHolder.Callback, OnTouchListener {
    private var circleCenterXVal: Float = 0f
    private var circleCenterYVal: Float = 0f
    private var outerRadius: Float = 0f
    private var innerRadius: Float = 0f
    private var joystickCallback: JoystickListener? = null
    private val ratio = 5 //The smaller, the more shading will occur
    private fun setupDimensions() {
        circleCenterXVal = (width / 2).toFloat()
        circleCenterYVal = (height / 2).toFloat()
        outerRadius = (width.coerceAtMost(height) / 4).toFloat()
        innerRadius = (width.coerceAtMost(height) / 6).toFloat()
    }

    constructor(context: Context?) : super(context) {
        holder.addCallback(this)
        setOnTouchListener(this)
        if (context is JoystickListener) joystickCallback = context
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSPARENT);
    }

    constructor(context: Context?, attributes: AttributeSet?, style: Int) : super(
        context,
        attributes,
        style
    ) {
        holder.addCallback(this)
        setOnTouchListener(this)
        if (context is JoystickListener) joystickCallback = context
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSPARENT);
    }

    constructor(context: Context?, attributes: AttributeSet?) : super(context, attributes) {
        holder.addCallback(this)
        setOnTouchListener(this)
        if (context is JoystickListener) joystickCallback = context
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSPARENT);
    }

    private fun drawJoystick(newX: Float, newY: Float) {
        if (holder.surface.isValid) {
            val myCanvas = this.holder.lockCanvas() //Stuff to draw
            val colors = Paint()
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR) // Clear the BG

            //First determine the sin and cos of the angle that the touched point is at relative to the center of the joystick
            val hypotenuse = sqrt((newX - circleCenterXVal).toDouble().pow(2.0) + (newY - circleCenterYVal).toDouble().pow(2.0)).toFloat()
            val sin = (newY - circleCenterYVal) / hypotenuse //sin = o/h
            val cos = (newX - circleCenterXVal) / hypotenuse //cos = a/h

            //Draw the base first before shading
            colors.setARGB(255, 100, 100, 100)
            myCanvas.drawCircle(circleCenterXVal, circleCenterYVal, outerRadius, colors)
            for (i in 1..(outerRadius / ratio).toInt()) { colors.setARGB(
                    150 / i,
                    255,
                    0,
                    0
                ) //Gradually decrease the shade of black drawn to create a nice shading effect
                myCanvas.drawCircle(
                    newX - cos * hypotenuse * (ratio / outerRadius) * i,
                    newY - sin * hypotenuse * (ratio / outerRadius) * i,
                    i * (innerRadius * ratio / outerRadius),
                    colors
                ) //Gradually increase the size of the shading effect
            }

            //Drawing the joystick hat
            for (i in 0..(innerRadius / ratio).toInt()) {
                colors.setARGB(
                    255,
                    (i * (255 * ratio / innerRadius)).toInt(),
                    (i * (255 * ratio / innerRadius)).toInt(), 255
                ) //Change the joystick color for shading purposes
                myCanvas.drawCircle(
                    newX,
                    newY,
                    innerRadius - i.toFloat() * ratio / 2,
                    colors
                ) //Draw the shading for the hat
            }
            holder.unlockCanvasAndPost(myCanvas) //Write the new drawing to the SurfaceView
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        setupDimensions()
        drawJoystick(circleCenterXVal, circleCenterYVal)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(holder: SurfaceHolder) {}
    override fun onTouch(v: View, e: MotionEvent): Boolean {
        if(v == this){
            if(e.action != MotionEvent.ACTION_UP){
                val displacement = sqrt(((e.x - circleCenterXVal) * (e.x - circleCenterXVal)) + ((e.y - circleCenterYVal) * (e.y - circleCenterYVal)))

                // Joystick is in limits
                if(displacement < outerRadius){
                    joystickCallback?.onJoystickMoved((e.x - circleCenterXVal) / outerRadius, (e.y - circleCenterYVal) / outerRadius);
                    drawJoystick(e.x, e.y)
                }

                // Joystick is out of boundaries
                else{
                    // Calculate intersections between the line that represents the center of the circle and the point that the user touches

                    var centerOfCircle = Point(circleCenterXVal.toDouble(), circleCenterYVal.toDouble())
                    var radiusAsDouble = outerRadius.toDouble()

                    var userPoint = Point((e.x).toDouble(), (e.y).toDouble())

                    var intersections = intersects(centerOfCircle, userPoint, centerOfCircle, radiusAsDouble, false)
                    var deltaOne = sqrt((e.y - (intersections[0].y).toFloat()) * (e.y - (intersections[0].y).toFloat()) +
                            (e.x - (intersections[0].x).toFloat()) * (e.x - (intersections[0].x).toFloat()))
                    var deltaTwo = sqrt((e.y - (intersections[1].y).toFloat()) * (e.y - (intersections[1].y).toFloat()) +
                            (e.x - (intersections[1].x).toFloat()) * (e.x - (intersections[1].x).toFloat()))

                    var constrainedX = 0f
                    var constrainedY = 0f

                    // Find the closest intersection point to problematic point
                    if(deltaOne < deltaTwo){
                        constrainedX = (intersections[0].x).toFloat()
                        constrainedY = (intersections[0].y).toFloat()
                    }

                    else{
                        constrainedX = (intersections[1].x).toFloat()
                        constrainedY = (intersections[1].y).toFloat()
                    }

                    joystickCallback?.onJoystickMoved((constrainedX - circleCenterXVal) / outerRadius, (constrainedY - circleCenterYVal) / outerRadius);
                    drawJoystick(constrainedX, constrainedY)
                }
            }

            else{
                joystickCallback?.onJoystickMoved(0f, 0f);
                drawJoystick(circleCenterXVal, circleCenterYVal)
            }
        }

        return true;
    }

    interface JoystickListener {
        fun onJoystickMoved(xPercent: Float, yPercent: Float)
    }
}