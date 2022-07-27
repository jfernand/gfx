
import org.cr.geom.Line
import org.cr.geom.Point
import org.cr.geom.p
import org.cr.gfx.keyPressed
import java.awt.Color
import java.awt.GraphicsEnvironment
import javax.swing.JFrame
import kotlin.system.exitProcess

fun main() {
    val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
    val device = ge.defaultScreenDevice
//    val modes = device.displayModes
//    device.
//    val window = device.fullScreenWindow
//    device.displayMode = device.displayModes[0]
    println("full screen available: ${device.isFullScreenSupported}")
    DirectCanvas.start()
}
interface HasGraphics {
     val g:java.awt.Graphics
}
object DirectCanvas : JFrame(), HasGraphics {
    private val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
    private val device = ge.defaultScreenDevice
    private val modes = device.displayModes ?: error("Cannot get display modes")
    private var exit = false
    val center: Point
    override lateinit var g: java.awt.Graphics

    init {
        device.displayMode = device.displayModes[0]
        val fs = device.isFullScreenSupported
        isUndecorated = fs
        isResizable = !fs
//        window.addKeyListener(KeyLis)
        if (fs) {
            // Full-screen mode
            device.fullScreenWindow = this
            validate()
        } else {
            // Windowed mode
            pack()
            isVisible = true
        }
        center = p(size.width / 2, size.height / 2)
        createBufferStrategy(3)
    }

    fun start() {
        keyPressed {
            exit = true
        }
        loop()
    }

    private fun loop() {
        var x = 0
        while (!exit) {
            with(bufferStrategy.drawGraphics) {
                color = Color.BLACK
                fillRect(0, 0, size.width, size.height)
                color = Color(x % 256, 255 - (x % 256), 100)
                drawLine(x, 0, x, size.height)

                dispose()
                bufferStrategy.show()
                Thread.yield()
                x++
                x %= size.width
            }
        }
        exitProcess(0)
    }
}

fun DirectCanvas.line(l1: Line) {
    val l = l1 + center
    val d = l1.end - l1.start
    g.drawLine(l.start.x.toInt(), l.start.y.toInt(), d.x.toInt(), d.y.toInt())
}
