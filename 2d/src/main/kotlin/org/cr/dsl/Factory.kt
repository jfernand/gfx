package org.cr.dsl

import org.apache.batik.swing.JSVGCanvas
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.Component
import java.awt.Container
import java.awt.FlowLayout
import java.awt.GridBagLayout
import java.awt.GridLayout
import java.awt.LayoutManager
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.GroupLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.JToolBar
import javax.swing.SpringLayout

typealias Consumer<T> = T.() -> Unit

interface JComponentFactory {
    fun toolBar(build: Consumer<JToolBar>): JToolBar

    fun borderPanel(build: Consumer<BorderPanelBuilder>): JPanel

    fun cardPanel(build: Consumer<DefaultPanelBuilder<CardLayout>>): JPanel

    fun flowPanel(build: Consumer<DefaultPanelBuilder<FlowLayout>>): JPanel

    fun boxPanel(build: Consumer<DefaultPanelBuilder<BoxLayout>>): JPanel

    fun gridPanel(rows: Int, cols: Int, build: Consumer<DefaultPanelBuilder<GridLayout>>): JPanel

    fun gridBagPanel(rows: Int, cols: Int, build: Consumer<DefaultPanelBuilder<GridBagLayout>>): JPanel

    fun groupPanel(host: Container, cols: Int, build: Consumer<DefaultPanelBuilder<GroupLayout>>): JPanel

    fun springPanel(build: Consumer<DefaultPanelBuilder<SpringLayout>>): JPanel

    fun button(label: String, build: Consumer<JButton> = {}): JButton

    fun label(label: String = "", build: Consumer<JLabel> = {}): JLabel

    fun svg(label: String = "", build: Consumer<JSVGCanvas> = {}): JSVGCanvas

    fun textField(columns: Int, build: Consumer<JTextField> = {}): JTextField

    fun textArea(
        text: String? = null, rows: Int = 0, columns: Int = 0, build: Consumer<JTextArea> = {}
    ): JTextArea

    fun radioButton(label: String, build: Consumer<JRadioButton> = {}): JRadioButton

    fun <T : JComponent> border(title: String, build: () -> T): T
}

class DefaultJComponentFactory : JComponentFactory {
    override fun toolBar(build: Consumer<JToolBar>): JToolBar {
        val toolbar = JToolBar()
        toolbar.build()
        return toolbar
    }

    override fun borderPanel(build: Consumer<BorderPanelBuilder>): JPanel {
        val layout = BorderLayout()
        val panel = JPanel(layout)
        build(BorderPanelBuilder(panel))
        return panel
    }

    override fun cardPanel(build: Consumer<DefaultPanelBuilder<CardLayout>>): JPanel = CardLayout().makePanel(build)

    override fun flowPanel(build: Consumer<DefaultPanelBuilder<FlowLayout>>): JPanel {
        val panel = JPanel()
        build(DefaultPanelBuilder(panel))
        return panel
    }

    override fun boxPanel(build: Consumer<DefaultPanelBuilder<BoxLayout>>): JPanel {
        val panel = JPanel()
        val layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.layout = layout
        build(DefaultPanelBuilder(panel))
        return panel
    }

    override fun gridPanel(
        rows: Int, cols: Int, build: Consumer<DefaultPanelBuilder<GridLayout>>
    ): JPanel = GridLayout(rows, cols).makePanel(build)

    override fun gridBagPanel(rows: Int, cols: Int, build: Consumer<DefaultPanelBuilder<GridBagLayout>>): JPanel = GridBagLayout().makePanel(build)

    override fun groupPanel(host: Container, cols: Int, build: Consumer<DefaultPanelBuilder<GroupLayout>>): JPanel = GroupLayout(host).makePanel(build)

    override fun springPanel(build: Consumer<DefaultPanelBuilder<SpringLayout>>): JPanel = SpringLayout().makePanel(build)

    private fun <T : LayoutManager> T.makePanel(build: Consumer<DefaultPanelBuilder<T>>): JPanel {
        val panel = JPanel(this)
        build(DefaultPanelBuilder(panel))
        return panel
    }

    override fun button(label: String, build: Consumer<JButton>): JButton = make(JButton(label), build)

    override fun label(label: String, build: Consumer<JLabel>): JLabel = make(JLabel(label), build)

    override fun svg(label: String, build: Consumer<JSVGCanvas>): JSVGCanvas = make(JSVGCanvas(), build)

    override fun textField(columns: Int, build: Consumer<JTextField>): JTextField = make(JTextField(columns), build)

    override fun textArea(
        text: String?, rows: Int, columns: Int, build: Consumer<JTextArea>
    ): JTextArea = make(JTextArea(text, rows, columns), build)

    override fun radioButton(label: String, build: Consumer<JRadioButton>): JRadioButton = make(JRadioButton(label), build)

    override fun <T : JComponent> border(title: String, build: () -> T): T {
        val component = build()
        component.border = BorderFactory.createTitledBorder(title)
        return component
    }

    private fun <T : JComponent> make(component: T, build: Consumer<T>): T {
        component.build()
        return component
    }
}

interface PanelBuilder {
    val panel: JPanel
}

class BorderPanelBuilder(override val panel: JPanel) : PanelBuilder {
    var north: JComponent
        get() = throw NotImplementedError()
        set(value) = panel.add(value, BorderLayout.NORTH)
    var south: JComponent
        get() = throw NotImplementedError()
        set(value) = panel.add(value, BorderLayout.SOUTH)
    var east: JComponent
        get() = throw NotImplementedError()
        set(value) = panel.add(value, BorderLayout.EAST)
    var west: JComponent
        get() = throw NotImplementedError()
        set(value) = panel.add(value, BorderLayout.WEST)
    var center: JComponent
        get() = throw NotImplementedError()
        set(value) = panel.add(value, BorderLayout.CENTER)
    val layout = panel.layout as BorderLayout
}

class DefaultPanelBuilder<L : LayoutManager>(override val panel: JPanel) :
    PanelBuilder, JComponentFactory {

    override fun borderPanel(build: Consumer<BorderPanelBuilder>): JPanel = add(factory.borderPanel(build))

    override fun cardPanel(build: Consumer<DefaultPanelBuilder<CardLayout>>): JPanel = add(factory.cardPanel(build))

    override fun toolBar(build: Consumer<JToolBar>): JToolBar = add(factory.toolBar(build))

    override fun flowPanel(build: Consumer<DefaultPanelBuilder<FlowLayout>>): JPanel = add(factory.flowPanel(build))

    override fun boxPanel(build: Consumer<DefaultPanelBuilder<BoxLayout>>): JPanel = add(factory.boxPanel(build))

    override fun gridPanel(
        rows: Int, cols: Int, build: Consumer<DefaultPanelBuilder<GridLayout>>
    ): JPanel = add(factory.gridPanel(rows, cols, build))

    override fun gridBagPanel(rows: Int, cols: Int, build: Consumer<DefaultPanelBuilder<GridBagLayout>>): JPanel = add(factory.gridBagPanel(rows, cols, build))

    override fun groupPanel(host: Container, cols: Int, build: Consumer<DefaultPanelBuilder<GroupLayout>>): JPanel = add(factory.groupPanel(host, cols, build))

    override fun springPanel(build: Consumer<DefaultPanelBuilder<SpringLayout>>): JPanel = add(factory.springPanel(build))

    override fun button(label: String, build: Consumer<JButton>): JButton = add(factory.button(label, build))

    override fun label(label: String, build: Consumer<JLabel>): JLabel = add(factory.label(label, build))

    override fun svg(label: String, build: Consumer<JSVGCanvas>): JSVGCanvas = add(factory.svg(label, build))

    override fun textField(columns: Int, build: Consumer<JTextField>): JTextField = add(factory.textField(columns, build))

    override fun textArea(
        text: String?, rows: Int, columns: Int, build: Consumer<JTextArea>
    ): JTextArea = add(factory.textArea(text, rows, columns, build))

    override fun radioButton(label: String, build: Consumer<JRadioButton>): JRadioButton = add(factory.radioButton(label, build))

    override fun <T : JComponent> border(title: String, build: () -> T): T = add(factory.border(title, build))

    private fun <T : Component> add(component: T): T {
        panel.add(component)
        return component
    }

    @Suppress("UNCHECKED_CAST")
    val layout = panel.layout as L
}

// TODO all these properties should be probably cached

//val JButton.actionEvents: Observable<ActionEvent>
//    get() = observable { emitter ->
//        val listener = ActionListener { e -> emitter.onNext(e) }
//        addActionListener(listener)
//        emitter.setCancellable { removeActionListener(listener) }
//    }
//
//val JButton.mouseEvents: Observable<MouseEvent>
//    get() = observable { emitter ->
//        val listener = object : MouseListener {
//            override fun mouseClicked(e: MouseEvent) {
//                fireChange(e)
//            }
//
//            override fun mouseEntered(e: MouseEvent) {
//                fireChange(e)
//            }
//
//            override fun mouseExited(e: MouseEvent) {
//                fireChange(e)
//            }
//
//            override fun mousePressed(e: MouseEvent) {
//                fireChange(e)
//            }
//
//            override fun mouseReleased(e: MouseEvent) {
//                fireChange(e)
//            }
//
//            private fun fireChange(e: MouseEvent) {
//                emitter.onNext(e)
//            }
//        }
//        addMouseListener(listener)
//        emitter.setCancellable { removeMouseListener(listener) }
//    }

//val JTextField.actionEvents: Observable<ActionEvent>
//    get() = observable { emitter ->
//        val listener = ActionListener { e -> emitter.onNext(e) }
//        addActionListener(listener)
//        emitter.setCancellable { removeActionListener(listener) }
//    }

//val JRadioButton.actionEvents: Observable<ActionEvent>
//    get() = observable { emitter ->
//        val listener = ActionListener { e -> emitter.onNext(e) }
//        addActionListener(listener)
//        emitter.setCancellable { removeActionListener(listener) }
//    }

//val JTextComponent.documentChanges: Observable<DocumentEvent>
//    get() = observable { emitter ->
//        val listener = object : DocumentListener {
//            override fun insertUpdate(e: DocumentEvent) {
//                fireChange(e)
//            }
//
//            override fun removeUpdate(e: DocumentEvent) {
//                fireChange(e)
//            }
//
//            override fun changedUpdate(e: DocumentEvent) {
//                fireChange(e)
//            }
//
//            private fun fireChange(e: DocumentEvent) {
//                emitter.onNext(e)
//            }
//        }
//        document.addDocumentListener(listener)
//        emitter.setCancellable { document.removeDocumentListener(listener) }
//    }

//val JTextComponent.textChanges: Observable<String>
//    get() = documentChanges.map { text }

//val swingScheduler = object : Scheduler {
//
//    private val executor = object : Scheduler.Executor {
//
//        private val waiter = computationScheduler.newExecutor()
//
//        override fun submit(delayMillis: Long, task: () -> Unit) {
//            waiter.submit(delayMillis) {
//                SwingUtilities.invokeLater(task)
//            }
//        }
//
//        override fun submitRepeating(
//            startDelayMillis: Long,
//            periodMillis: Long,
//            task: () -> Unit
//        ) {
//            waiter.submitRepeating(startDelayMillis, periodMillis) {
//                SwingUtilities.invokeLater(task)
//            }
//        }
//
//        override val isDisposed: Boolean = waiter.isDisposed
//
//        override fun cancel() {
//            waiter.cancel()
//        }
//
//        override fun dispose() {
//            waiter.dispose()
//        }
//
//    }
//
//    override fun newExecutor(): Scheduler.Executor = executor
//
//    override fun destroy() {
//        /* does nothing for swing */
//    }
//
//}
