package org.cr.geom

import kotlin.math.pow
import kotlin.math.sqrt

typealias Vec2 = Point
typealias Dimension = Point

fun p(x: Number, y: Number) = Point(x.toDouble(), y.toDouble())

data class Point(val x: Double, val y: Double) {
    override fun toString() = "($x, $y)"

    operator fun times(s: Number) = s * this
    operator fun div(s: Number) = Point(x / s.toDouble(), y / s.toDouble())
    operator fun plus(p: Point) = Point(x + p.x, y + p.y)
    operator fun minus(p: Point) = Point(x - p.x, y - p.y)
    fun yFlip(height:Int) = Point(x, height-y)
}

operator fun Number.times(p: Point) = Point(p.x * toDouble(), p.y * toDouble())

infix fun Vec2.dot(p: Vec2) = Vec2(x * p.x, y * p.y)

data class Segment(val v: Point, val w: Point) {
    fun lengthSq() = distanceSq(v, w)
    fun length() = distance(v, w)
    fun lerp(t: Number) = lerp(v, w, t as Double)
    operator fun times(t: Number) = lerp(v, w, t as Double)
}

operator fun Number.times(s: Segment) = lerp(s.v, s.w, this as Double)

fun midPoint(p1: Point, p2: Point) = (p1 + p2) / 2.0
fun distance(p1: Point, p2: Point): Double {
    return sqrt(distanceSq(p1, p2))
}

fun distanceSq(p1: Point, p2: Point): Double {
    return (p1.x - p2.x).pow(2.0) + (p1.y - p2.y).pow(2.0)
}

/**
 * Linear Interpolate
 */
fun lerp(a: Point, b: Point, t: Double): Point = Point(
    a.x + (b.x - a.x) * t,
    a.y + (b.y - a.y) * t,
)

fun a(
    p1: Point,
    p2: Point,
    startAngle: Number,
    endAngle: Number,
) = Arc(p1, p2, startAngle.toDouble(), endAngle.toDouble())

data class Arc(val p1: Point, val p2: Point, val startAngle: Double, val endAngle: Double) {
    operator fun plus(offset: Point): Arc = Arc(p1 + offset, p2 + offset, startAngle, endAngle)

    fun yFlip(height:Int) = copy(p1 = p1.yFlip(height), p2 = p2.yFlip(height))
}

fun l(a: Point, b: Point) = Line(a, b)

data class Line(val start: Point, val end: Point) {
    operator fun plus(point: Point) = l(start + point, end + point)
    fun midPoint() = (start + end) / 2
    fun toBounds() = BoundingBox(start.x, start.y, (end - start).x, (end - start).y)
    override fun toString() = "$start - $end"
    fun yFlip(height: Int) = Line(start.yFlip(height), end.yFlip(height))
}

data class BoundingBox(val x: Double, val y: Double, val width: Double, val height: Double)
