package org.cr.gfx

import DirectCanvas.g
import org.cr.IntPref
import org.cr.geom.Arc
import org.cr.geom.Line
import org.cr.geom.Point
import org.cr.geom.p
import java.awt.Color.BLACK
import java.awt.Color.WHITE
import java.awt.Graphics
import java.awt.GraphicsEnvironment
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager
import kotlin.system.exitProcess
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource.Monotonic.markNow

fun main() {
    SwingUtilities.invokeLater {
        UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf")
        Window.start()
    }
}

object Window : JFrame() {
    private val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
    private val device = ge.defaultScreenDevice
    private var savedWidth by IntPref("org.cr.window")
    private var savedHeight by IntPref("org.cr.window")
    private var exit = false
    private val center
        get() = center()

    init {
        isResizable = true
        isVisible = true
        componentResized {
            println(center)
            savedWidth = size.width
            savedHeight = size.height
        }
        createBufferStrategy(3)
    }

    private fun center() = p(size.width / 2, size.height / 2)

    fun start() {
        setSize(savedWidth, savedHeight)
        keyPressed {
            exit = true
        }
        contentPane = Canvas()
    }

    fun paint() {}
    private fun loop() {
        var x = 0
        while (!exit) {
            val g = bufferStrategy.drawGraphics
            with(Ctx(g, center)) {
                g.color = BLACK
                g.fillRect(0, 0, size.width, size.height)
//                color = Color(x % 256, 255 - (x % 256), 100)
//                drawLine(x, 0, x, size.height)

                g.color = WHITE
//                line(l(p(0, center.y), p(0, -center.y)))
//                line(l(p(center.x, 0), p(-center.x, 0)))
//                arc(a(p(-100, 0), p(0, 50), 15, 90))
//                capFrameRate(60.0)
                g.color = BLACK
                dispose()
                bufferStrategy.show()
                Thread.yield()
                x++
                x %= size.width.coerceAtLeast(1)
            }
        }
        exitProcess(0)
    }
}


@OptIn(ExperimentalTime::class)
var t = markNow()

@OptIn(ExperimentalTime::class)
fun capFrameRate(fps: Double) {
    val wait: Double = 1 / fps
    val diff = t.elapsedNow().inWholeMilliseconds
    if (diff < wait) {
        Thread.sleep((wait - diff).toLong())
    }
    t = markNow()
}
