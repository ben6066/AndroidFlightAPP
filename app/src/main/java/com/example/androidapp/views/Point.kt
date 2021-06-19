package com.example.androidapp.views

import kotlin.math.absoluteValue
import kotlin.math.sqrt

const val eps = 1e-14

class Point(val x: Double, val y: Double) {
    override fun toString(): String {
        var xv = x
        if (xv == 0.0) {
            xv = 0.0
        }
        var yv = y
        if (yv == 0.0) {
            yv = 0.0
        }
        return "($xv, $yv)"
    }
}

fun sq(x: Double): Double {
    return x * x
}

fun intersects(p1: Point, p2: Point, cp: Point, r: Double, segment: Boolean): MutableList<Point> {
    val res = mutableListOf<Point>()
    val x0 = cp.x
    val y0 = cp.y
    val x1 = p1.x
    val y1 = p1.y
    val x2 = p2.x
    val y2 = p2.y
    val A = y2 - y1
    val B = x1 - x2
    val C = x2 * y1 - x1 * y2
    val a = sq(A) + sq(B)
    val b: Double
    val c: Double
    var bnz = true
    if (B.absoluteValue >= eps) {
        b = 2 * (A * C + A * B * y0 - sq(B) * x0)
        c = sq(C) + 2 * B * C * y0 - sq(B) * (sq(r) - sq(x0) - sq(y0))
    } else {
        b = 2 * (B * C + A * B * x0 - sq(A) * y0)
        c = sq(C) + 2 * A * C * x0 - sq(A) * (sq(r) - sq(x0) - sq(y0))
        bnz = false
    }
    var d = sq(b) - 4 * a * c // discriminant
    if (d < 0) {
        return res
    }

    // checks whether a point is within a segment
    fun within(x: Double, y: Double): Boolean {
        val d1 = sqrt(sq(x2 - x1) + sq(y2 - y1)) // distance between end-points
        val d2 = sqrt(sq(x - x1) + sq(y - y1))   // distance from point to one end
        val d3 = sqrt(sq(x2 - x) + sq(y2 - y))   // distance from point to other end
        val delta = d1 - d2 - d3
        return delta.absoluteValue < eps // true if delta is less than a small tolerance
    }

    var x = 0.0
    fun fx(): Double {
        return -(A * x + C) / B
    }

    var y = 0.0
    fun fy(): Double {
        return -(B * y + C) / A
    }

    fun rxy() {
        if (!segment || within(x, y)) {
            res.add(Point(x, y))
        }
    }

    if (d == 0.0) {
        // line is tangent to circle, so just one intersect at most
        if (bnz) {
            x = -b / (2 * a)
            y = fx()
            rxy()
        } else {
            y = -b / (2 * a)
            x = fy()
            rxy()
        }
    } else {
        // two intersects at most
        d = sqrt(d)
        if (bnz) {
            x = (-b + d) / (2 * a)
            y = fx()
            rxy()
            x = (-b - d) / (2 * a)
            y = fx()
            rxy()
        } else {
            y = (-b + d) / (2 * a)
            x = fy()
            rxy()
            y = (-b - d) / (2 * a)
            x = fy()
            rxy()
        }
    }

    return res
}