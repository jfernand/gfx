package org.cr.dsl

import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

internal class DefaultJComponentFactoryTest {

//    @org.junit.jupiter.api.Test
    fun gridPanel() {
        mainFrame("Test") {
            gridPanel(2,2) {
                label("A")
                label("B")
                label("C")
                label("D")
            }
        }
    }
}

fun main() {
    mainFrame("Test") {
        jMenuBar = makeMenu(this)
        contentPane = gridPanel(2,2) {
            label("A")
            label("B")
            label("C")
            label("D")
        }
    }
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
