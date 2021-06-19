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
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var baseRadius: Float = 0f
    private var hatRadius: Float = 0f
    private var joystickCallback: JoystickListener? = null
    private val ratio = 5 //The smaller, the more shading will occur
    private fun setupDimensions() {
        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()
        baseRadius = (width.coerceAtMost(height) / 4).toFloat()
        hatRadius = (width.coerceAtMost(height) / 6).toFloat()
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
            val hypotenuse =
                sqrt(
                    (newX - centerX).toDouble().pow(2.0) + (newY - centerY).toDouble().pow(2.0)
                )
                    .toFloat()
            val sin = (newY - centerY) / hypotenuse //sin = o/h
            val cos = (newX - centerX) / hypotenuse //cos = a/h

            //Draw the base first before shading
            colors.setARGB(255, 100, 100, 100)
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors)
            for (i in 1..(baseRadius / ratio).toInt()) {
                colors.setARGB(
                    150 / i,
                    255,
                    0,
                    0
                ) //Gradually decrease the shade of black drawn to create a nice shading effect
                myCanvas.drawCircle(
                    newX - cos * hypotenuse * (ratio / baseRadius) * i,
                    newY - sin * hypotenuse * (ratio / baseRadius) * i,
                    i * (hatRadius * ratio / baseRadius),
                    colors
                ) //Gradually increase the size of the shading effect
            }

            //Drawing the joystick hat
            for (i in 0..(hatRadius / ratio).toInt()) {
                colors.setARGB(
                    255,
                    (i * (255 * ratio / hatRadius)).toInt(),
                    (i * (255 * ratio / hatRadius)).toInt(), 255
                ) //Change the joystick color for shading purposes
                myCanvas.drawCircle(
                    newX,
                    newY,
                    hatRadius - i.toFloat() * ratio / 2,
                    colors
                ) //Draw the shading for the hat
            }
            holder.unlockCanvasAndPost(myCanvas) //Write the new drawing to the SurfaceView
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        setupDimensions()
        drawJoystick(centerX, centerY)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(holder: SurfaceHolder) {}
    override fun onTouch(v: View, e: MotionEvent): Boolean {
        if(v == this){
            if(e.action != MotionEvent.ACTION_UP){
                val displacement = sqrt(((e.x - centerX) * (e.x - centerX)) + ((e.y - centerY) * (e.y - centerY)))

                // Joystick is in limits
                if(displacement < baseRadius){
                    joystickCallback?.onJoystickMoved((e.x - centerX) / baseRadius, (e.y - centerY) / baseRadius);
                    drawJoystick(e.x, e.y)
                }

                // Joystick is out of boundaries
                else{
//                    println("The intersection points (if any) between:")
//
                    var centerOfCircle = Point(centerX.toDouble(), centerY.toDouble())
                    var radiusAsDouble = baseRadius.toDouble()
//                    println("  A circle, center $centerOfCircle with radius $radiusAsDouble, and:")
//
                    var userPoint = Point((e.x).toDouble(), (e.y).toDouble())
//                    println("    a line containing the points $centerOfCircle and $userPoint is/are:")
//                    println("     ${intersects(centerOfCircle, userPoint, centerOfCircle, radiusAsDouble, false)}")
//                    println("User touched in ${e.x} ${e.y}")

                    var intersections = intersects(centerOfCircle, userPoint, centerOfCircle, radiusAsDouble, false)
                    var deltaOne = sqrt((e.y - (intersections[0].y).toFloat()) * (e.y - (intersections[0].y).toFloat()) +
                            (e.x - (intersections[0].x).toFloat()) * (e.x - (intersections[0].x).toFloat()))
                    var deltaTwo = sqrt((e.y - (intersections[1].y).toFloat()) * (e.y - (intersections[1].y).toFloat()) +
                            (e.x - (intersections[1].x).toFloat()) * (e.x - (intersections[1].x).toFloat()))

                    var constrainedX = 0f
                    var constrainedY = 0f

                    if(deltaOne < deltaTwo){
                        constrainedX = (intersections[0].x).toFloat()
                        constrainedY = (intersections[0].y).toFloat()
                    }

                    else{
                        constrainedX = (intersections[1].x).toFloat()
                        constrainedY = (intersections[1].y).toFloat()
                    }

                    joystickCallback?.onJoystickMoved((constrainedX - centerX) / baseRadius, (constrainedY - centerY) / baseRadius);
                    drawJoystick(constrainedX, constrainedY)
                }
            }

            else{
                joystickCallback?.onJoystickMoved(0f, 0f);
                drawJoystick(centerX, centerY)
            }
        }

        return true;
    }

    interface JoystickListener {
        fun onJoystickMoved(xPercent: Float, yPercent: Float)
    }
}