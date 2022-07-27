
import java.awt.Component
import java.awt.Container
import java.awt.Toolkit
import java.awt.event.MouseEvent
import javax.swing.JMenuBar
import javax.swing.SwingUtilities.convertPoint
import javax.swing.SwingUtilities.getDeepestComponentAt
import javax.swing.event.MouseInputAdapter

internal class CBListener(
    liveButton: Component, menuBar: JMenuBar,
    glassPane: FancyGlassPane, contentPane: Container
) : MouseInputAdapter() {
    var toolkit: Toolkit = Toolkit.getDefaultToolkit()
    var liveButton: Component
    var menuBar: JMenuBar
    var glassPane: FancyGlassPane
    var contentPane: Container

    init {
        this.liveButton = liveButton
        this.menuBar = menuBar
        this.glassPane = glassPane
        this.contentPane = contentPane
    }

    override fun mouseMoved(e: MouseEvent) {
        redispatchMouseEvent(e, false)
    }

    override fun mouseDragged(e: MouseEvent) {
        redispatchMouseEvent(e, false)
    }

    override fun mouseClicked(e: MouseEvent) {
        redispatchMouseEvent(e, false)
    }

    override fun mouseEntered(e: MouseEvent) {
        redispatchMouseEvent(e, false)
    }

    override fun mouseExited(e: MouseEvent) {
        redispatchMouseEvent(e, false)
    }

    override fun mousePressed(e: MouseEvent) {
        redispatchMouseEvent(e, false)
    }

    override fun mouseReleased(e: MouseEvent) {
        redispatchMouseEvent(e, true)
    }

    //A basic implementation of redispatching events.
    private fun redispatchMouseEvent(
        e: MouseEvent,
        repaint: Boolean
    ) {
        val glassPanePoint = e.point
        val container = contentPane
        val containerPoint = convertPoint(
            glassPane,
            glassPanePoint,
            contentPane
        )
        if (containerPoint.y < 0) { //we're not in the content pane
            if (containerPoint.y + menuBar.height >= 0) {
                //The mouse event is over the menu bar.
                //Could handle specially.
            } else {
                //The mouse event is over non-system window
                //decorations, such as the ones provided by
                //the Java look and feel.
                //Could handle specially.
            }
        } else {
            //The mouse event is probably over the content pane.
            //Find out exactly which component it's over.
            val component = getDeepestComponentAt(
                container,
                containerPoint.x,
                containerPoint.y
            )
            if (component != null && component == liveButton) {
                //Forward events over the check box.
                val componentPoint = convertPoint(
                    glassPane,
                    glassPanePoint,
                    component
                )
                component.dispatchEvent(
                    MouseEvent(
                        component,
                        e.id,
                        e.getWhen(),
                        e.modifiers,
                        componentPoint.x,
                        componentPoint.y,
                        e.clickCount,
                        e.isPopupTrigger
                    )
                )
            }
        }

        //Update the glass pane if requested.
        if (repaint) {
            glassPane.point = glassPanePoint
            glassPane.repaint()
        }
    }
}
