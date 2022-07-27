package org.cr.dsl//import com.badoo.reaktive.base.setCancellable
//import com.badoo.reaktive.observable.Observable
//import com.badoo.reaktive.observable.map
//import com.badoo.reaktive.observable.observable
//import com.badoo.reaktive.scheduler.Scheduler
//import com.badoo.reaktive.scheduler.computationScheduler
import org.apache.batik.swing.JSVGCanvas
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.GraphicsEnvironment
import java.awt.GridLayout
import java.awt.Point
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.JToolBar
import javax.swing.SwingUtilities
import javax.swing.WindowConstants
import javax.swing.border.Border

internal val factory = DefaultJComponentFactory()

class MainFrame : JFrame() {
    private val screenSize: Dimension

    init {
        val displayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.displayMode
        screenSize = Dimension(displayMode.width, displayMode.height)
        location = (screenSize - size).div(2).toPoint()
    }
}

private fun Dimension.div(i: Int): Dimension = Dimension(width/i, height/i)
private fun Dimension.toPoint() : Point = Point(width, height)

private operator fun Dimension.minus(op: Dimension): Dimension {
    return Dimension(width - op.width, height - op.height)
}

fun mainFrame(title: String, build: JFrame.() -> Unit) = SwingUtilities.invokeAndWait {
    val frame = MainFrame()
    frame.title = title
    build(frame)
    frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    frame.pack()
    frame.isVisible = true
}

fun borderPanel(build: Consumer<BorderPanelBuilder>): JPanel =
    factory.borderPanel(build)

fun flowPanel(build: Consumer<DefaultPanelBuilder<FlowLayout>>): JPanel =
    factory.flowPanel(build)

fun boxPanel(build: Consumer<DefaultPanelBuilder<BoxLayout>>): JPanel =
    factory.boxPanel(build)

fun gridPanel(rows: Int, cols: Int, build: Consumer<DefaultPanelBuilder<GridLayout>>): JPanel =
    factory.gridPanel(rows, cols, build)

fun cardPanel(rows: Int, cols: Int, build: Consumer<DefaultPanelBuilder<java.awt.CardLayout>>): JPanel =
    factory.cardPanel(build)

fun button(label: String, build: Consumer<JButton> = {}): JButton =
    factory.button(label, build)

fun toolBar(build: Consumer<JToolBar>): JToolBar =
    factory.toolBar(build)

fun label(label: String = "", build: Consumer<JLabel> = {}): JLabel =
    factory.label(label, build)

fun svg(label: String = "", build: Consumer<JSVGCanvas> = {}): JSVGCanvas =
    factory.svg(label, build)

fun textField(columns: Int, build: Consumer<JTextField> = {}): JTextField =
    factory.textField(columns, build)

fun textArea(
    text: String? = null, rows: Int = 0, columns: Int = 0, build: Consumer<JTextArea> = {}
): JTextArea =
    factory.textArea(text, rows, columns, build)

fun <T : JComponent> border(title: String, build: () -> T): T =
    factory.border(title, build)

fun emptyBorder(width: Int): Border = emptyBorder(width, width, width, width)

fun emptyBorder(top: Int, left: Int, bottom: Int, right: Int): Border =
    BorderFactory.createEmptyBorder(top, left, bottom, right)
