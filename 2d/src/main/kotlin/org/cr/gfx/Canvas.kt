package org.cr.gfx

import DirectCanvas.g
import org.cr.geom.Arc
import org.cr.geom.Line
import org.cr.geom.Point
import org.cr.geom.l
import org.cr.geom.p
import java.awt.Color
import java.awt.Graphics
import javax.swing.JComponent
import javax.swing.JPanel

class Canvas : JPanel() {
    private val colors = ArrayDeque<Color>()
    private val center
        get() = p(size.width / 2, size.height / 2)

    override fun paint(g: Graphics?) {
        super.paint(g)
        Ctx(g ?: return, center).draw()
    }

    fun Ctx.draw() {
        g.color = Color.BLACK
        g.fillRect(0, 0, size.width, size.height)
//                color = Color(x % 256, 255 - (x % 256), 100)
//                drawLine(x, 0, x, size.height)

        g.color = Color.WHITE
        line(l(p(0, center.y), p(0, -center.y)), PLAIN)
        line(l(p(center.x, 0), p(-center.x, 0)), PLAIN)
//            arc(a(p(-100, 0), p(0, 50), 15, 90))
//                capFrameRate(60.0)
        g.color = Color.BLACK
    }
    fun JComponent.push() {
        colors.addFirst(graphics.color)
    }

    fun JComponent.pop() {
        graphics.color = colors.removeFirst()
    }
}

const val PLAIN = true
context(Ctx)
fun Canvas.line(l1: Line, plain: Boolean = false) {
    val l = (l1 + center).yFlip(height)
    push()
    g.drawLine(
        l.start.x.toInt(),
        l.start.y.toInt(),
        l.end.x.toInt(),
        l.end.y.toInt(),
    )
    g.color = Color.BLACK
    g.fillOval(l.start.x.toInt() - 5, l.start.y.toInt() - 5, 10, 10)
    g.fillOval(l.end.x.toInt() - 5, l.end.y.toInt() - 5, 10, 10)
    g.color = Color.WHITE
    if (!plain) {
        g.drawOval(l.start.x.toInt() - 5, l.start.y.toInt() - 5, 10, 10)
        g.drawOval(l.end.x.toInt() - 5, l.end.y.toInt() - 5, 10, 10)
        g.drawString("$l", l.start.x.toInt() + 5, l.start.y.toInt() + 5)
        g.drawString("$l", l.end.x.toInt() + 5, l.end.y.toInt() + 5)
    }
}

class Ctx(val g: Graphics, val center: Point)

context(Ctx)
fun JComponent.arc(a: Arc) {
    val l = (a + center).yFlip(size.height)
    g.drawLine(
        l.p1.x.toInt(),
        l.p1.y.toInt(),
        l.p2.x.toInt(),
        l.p2.y.toInt(),
    )
//    val width = (l.p2.x - l.p1.x).toInt()
//    val height = -abs(l.p2.y - l.p1.y)
//    g.drawRect(
//        l.p1.x.toInt(),
//        l.p1.y.toInt(),
//        width,
//        height.toInt(),
//    )
//    g.drawArc(
//        l.p1.x.toInt(),
//        l.p1.y.toInt(),
//        width,
//        height.toInt(),
//        l.startAngle.toInt(),
//        l.endAngle.toInt()
//    )
//    val backColor = g.color
//    oval(l.p1 - p(5, 5), 10, 10)
    g.color = Color.BLACK
    g.fillOval(l.p1.x.toInt() - 5, l.p1.y.toInt() - 5, 10, 10)
    g.color = Color.WHITE
    g.fillOval(l.p2.x.toInt() - 5, l.p2.y.toInt() - 5, 10, 10)

//    g.drawOval(l.p1.x.toInt() - 5, l.p1.y.toInt() - 5, 10, 10)
//    g.drawOval(l.p2.x.toInt() - 5, l.p2.y.toInt() - 5, 10, 10)
//    g.color = backColor
    g.drawString("p1", l.p1.x.toInt() + 5, l.p1.y.toInt() + 5)
    g.drawString("p2", l.p2.x.toInt() + 5, l.p2.y.toInt() + 5)
}

fun JComponent.oval(center: Point, radius1: Int, radius2: Int = radius1) {
    val savedColor = g.color
    g.color = Color.WHITE
    g.fillOval(center.x.toInt() - 5, center.y.toInt() - 5, radius1, radius2)
    g.drawOval(center.x.toInt() - 5, center.y.toInt() - 5, radius1, radius2)
    g.color = savedColor
}
