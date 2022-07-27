import java.awt.Color
import java.awt.Container
import java.awt.FlowLayout
import java.awt.Graphics
import java.awt.Point
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import javax.swing.AbstractButton
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.SwingUtilities

internal class FancyGlassPane(
    button: AbstractButton,
    menuBar: JMenuBar,
    contentPane: Container
) : JComponent(), ItemListener {
    var point: Point? = null

    init {
        val listener = CBListener(button, menuBar, this, contentPane)
        addMouseListener(listener)
        addMouseMotionListener(listener)
    }

    //React to change org.cr.dsl.button clicks.
    override fun itemStateChanged(e: ItemEvent) {
        isVisible = e.stateChange == ItemEvent.SELECTED
    }

    override fun paintComponent(g: Graphics) {
        g.color = Color(0.4.toFloat(), 0.4.toFloat(), 0.4.toFloat(), 0.2.toFloat())
        g.fillRoundRect(0, 0, width, height, 20, 20)
        if (point != null) {
            g.fillOval(point!!.x - 10, point!!.y - 10, 20, 20)
        }
    }
}

fun createAndShowGUI() {
    val frame = JFrame("GlassPaneDemo")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    //Start creating and adding components.
    val checkBox = JCheckBox("Glass pane \"visible\"")
    checkBox.isSelected = false

    //Set up the content pane, where the "main GUI" lives.
    makeContentPane(frame, checkBox)
    val menuBar = makeMenu(frame)

    //Set up the glass pane, which appears over both menu bar
    //and content pane and is an item listener on the change
    //org.cr.dsl.button.
    val glassPane = FancyGlassPane(
        checkBox, menuBar,
        frame.contentPane
    )
    checkBox.addItemListener(glassPane)
    frame.glassPane = glassPane

    frame.pack()
    frame.isVisible = true
}

private fun makeMenu(frame: JFrame): JMenuBar {
    //Set up the menu bar, which appears above the content pane.
    val menuBar = JMenuBar()
    val menu = JMenu("Menu")
    menu.add(JMenuItem("Do nothing"))
    menuBar.add(menu)
    frame.jMenuBar = menuBar
    return menuBar
}

private fun makeContentPane(frame: JFrame, checkBox: JCheckBox) {
    val contentPane = frame.contentPane
    contentPane.layout = FlowLayout()
    contentPane.add(checkBox)
    contentPane.add(JButton("Button 1"))
    contentPane.add(JButton("Button 2"))
}

fun main() {
    SwingUtilities.invokeLater(::createAndShowGUI)
}
