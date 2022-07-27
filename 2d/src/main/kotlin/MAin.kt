
import org.apache.batik.swing.JSVGCanvas
import org.cr.dsl.borderPanel
import org.cr.dsl.flowPanel
import org.cr.dsl.label
import org.cr.dsl.mainFrame
import org.cr.dsl.svg
import org.cr.dsl.textArea
import java.io.File
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.UIManager

fun main() {
//    createSvg("resources/main/icon.org.cr.dsl.svg")
    UIManager.getInstalledLookAndFeels().forEach { println(it.className) }
    UIManager.setLookAndFeel(
//        "com.sun.java.swing.plaf.motif.MotifLookAndFeel"
//    "javax.swing.plaf.nimbus.NimbusLookAndFeel"
//    "javax.swing.plaf.metal.MetalLookAndFeel"
//    "com.apple.laf.AquaLookAndFeel"
    UIManager.getSystemLookAndFeelClassName()
    )
    mainFrame("Thing") {
        contentPane = borderPanel {
            center = svg {
                uri = getResource("src/main/resources/icon.svg")
            }
            west = textArea("Something", 50, 1) {  }
        south = label("status")
        }
        jMenuBar = makeMenu(this)
        isUndecorated = true
//        org.cr.gfx.componentResized {
//            shape = RoundRectangle2D.Double(0.0, 0.0, size.width.toDouble(), size.height.toDouble(), -100.0, 100.0)
//        }
        isVisible = true
    }
    println(".")
}

fun maain() {
    val f = JFrame()
    val svg = JSVGCanvas()
    svg.uri = getResource("src/main/resources/icon.svg")
    f.contentPane.add(svg)
    f.isVisible = true
    Thread.sleep(50000)
}

private fun JFrame.makeMenu(frame: JFrame): JMenuBar {
    //Set up the menu bar, which appears above the content pane.
    val menuBar = JMenuBar()
    val menu = JMenu("Menu")
    menu.add(JMenuItem("Do nothing"))
    menuBar.add(menu)
    frame.jMenuBar = menuBar
    return menuBar
}

fun fileMenu() {
    val menu = JMenu()
}

// protected static ImageIcon createImageIcon(String path,
//                                               String description) {
//        java.net.URL imgURL = LabelDemo.class.getResource(path);
//        if (imgURL != null) {
//            return new ImageIcon(imgURL, description);
//        } else {
//            System.err.println("Couldn't find file: " + path);
//            return null;
//        }
//    }

fun createSvg(path:String) : JSVGCanvas {
    val uri = getResource(path)
    if(uri != null) {
        val canvas = JSVGCanvas()
        println(uri)
        canvas.uri = uri
        return canvas
    }
    error("asshat")
}

private fun getResource(path: String) = File(path).absoluteFile.toURI().toASCIIString()

fun statusBar() {
    flowPanel {
    }
}
