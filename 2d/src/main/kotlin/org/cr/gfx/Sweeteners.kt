package org.cr.gfx
import java.awt.Window
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.KeyEvent
import javax.swing.JFrame

fun Window.keyTyped(handler: Handler<KeyEvent>) {
    addKeyListener(KeyTypedListener(handler))
}

fun Window.keyPressed(handler: Handler<KeyEvent>) {
    addKeyListener(KeyPressListener(handler))
}

fun Window.keyReleased(handler: Handler<KeyEvent>) {
    addKeyListener(KeyReleaseListener(handler))
}

typealias Handler<T> = T.() -> Unit

class KeyTypedListener(val handler: Handler<KeyEvent>) : java.awt.event.KeyListener {
    override fun keyTyped(e: KeyEvent?) {
        e?.handler()
    }

    override fun keyPressed(e: KeyEvent?) {
    }

    override fun keyReleased(e: KeyEvent?) {
    }
}

class KeyPressListener(val handler: KeyEvent.() -> Unit) : java.awt.event.KeyListener {
    override fun keyTyped(e: KeyEvent?) {
    }

    override fun keyPressed(e: KeyEvent?) {
        e?.handler()
    }

    override fun keyReleased(e: KeyEvent?) {
    }
}

class KeyReleaseListener(val handler: KeyEvent.() -> Unit) : java.awt.event.KeyListener {
    override fun keyTyped(e: KeyEvent?) {
    }

    override fun keyPressed(e: KeyEvent?) {
    }

    override fun keyReleased(e: KeyEvent?) {
        e?.handler()
    }
}

fun JFrame.componentResized(handler: Handler<ComponentEvent>) {
    addComponentListener(KComponentListener(handler))
}
class KComponentListener(val handler: Handler<ComponentEvent>) : ComponentAdapter() {
    override fun componentResized(e: ComponentEvent?) {
        super.componentResized(e)
        e?.handler()
    }
}
