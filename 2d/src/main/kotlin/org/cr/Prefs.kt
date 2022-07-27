package org.cr

import org.cr.gfx.Window
import java.util.prefs.*
import kotlin.reflect.KProperty

sealed class Pref {
    companion object {
        val prefs: Preferences = Preferences.userRoot()
    }
}

class IntPref(private val s: String) : Pref() {
    operator fun getValue(canvas: Window, property: KProperty<*>): Int {
        val value = prefs.get("$s.${property.name}", "100").toInt()
        println("Reading $s.${property.name}: $value")
        return value
    }

    operator fun setValue(canvas: Window, property: KProperty<*>, value: Int) {
        println("Writing $s.${property.name}: $value")
//        prefs.put("$s.${property.name}", value.toString())
        prefs.flush()
    }
}
