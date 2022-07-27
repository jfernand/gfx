
import javafx.scene.Group
import javafx.scene.transform.Rotate
import javafx.scene.transform.Scale
import javafx.scene.transform.Translate
//import org.junit.platform.engine.UniqueId.root

class Xform : Group {
    enum class RotateOrder {
        XYZ, XZY, YXZ, YZX, ZXY, ZYX
    }

    var t: Translate = Translate()
    var p: Translate = Translate()
    var ip: Translate = Translate()
    var rx: Rotate = Rotate()
    var ry: Rotate = Rotate()
    var rz: Rotate = Rotate()
    var s: Scale = Scale()

    constructor() : super() {
        transforms.addAll(t, rz, ry, rx, s)
    }

    constructor(rotateOrder: RotateOrder?) : super() {
        when (rotateOrder) {
            RotateOrder.XYZ -> transforms.addAll(t, p, rz, ry, rx, s, ip)
            RotateOrder.YXZ -> transforms.addAll(t, p, rz, rx, ry, s, ip)
            RotateOrder.YZX -> transforms.addAll(t, p, rx, rz, ry, s, ip) // For Camera
            RotateOrder.ZXY -> transforms.addAll(t, p, ry, rx, rz, s, ip)
            RotateOrder.ZYX -> transforms.addAll(t, p, rx, ry, rz, s, ip)
            RotateOrder.XZY -> transforms.addAll(t, p, rx, rz, ry, s, ip)
            null -> TODO()
        }
    }

    fun setTranslate(x: Double, y: Double, z: Double) {
        t.x = x
        t.y = y
        t.z = z
    }

    fun setTranslate(x: Double, y: Double) {
        t.x = x
        t.y = y
    }

    // Cannot override these methods as they are final:
    // public void setTranslateX(double x) { t.setX(x); }
    // public void setTranslateY(double y) { t.setY(y); }
    // public void setTranslateZ(double z) { t.setZ(z); }
    // Use these methods instead:
    fun setTx(x: Double) {
        t.x = x
    }

    fun setTy(y: Double) {
        t.y = y
    }

    fun setTz(z: Double) {
        t.z = z
    }

    fun setRotate(x: Double, y: Double, z: Double) {
        rx.angle = x
        ry.angle = y
        rz.angle = z
    }

    fun setRotateX(x: Double) {
        rx.angle = x
    }

    fun setRotateY(y: Double) {
        ry.angle = y
    }

    fun setRotateZ(z: Double) {
        rz.angle = z
    }

    fun setRy(y: Double) {
        ry.angle = y
    }

    fun setRz(z: Double) {
        rz.angle = z
    }

    fun setScale(scaleFactor: Double) {
        s.x = scaleFactor
        s.y = scaleFactor
        s.z = scaleFactor
    }

    // Cannot override these methods as they are final:
    // public void setScaleX(double x) { s.setX(x); }
    // public void setScaleY(double y) { s.setY(y); }
    // public void setScaleZ(double z) { s.setZ(z); }
    // Use these methods instead:
    fun setSx(x: Double) {
        s.x = x
    }

    fun setSy(y: Double) {
        s.y = y
    }

    fun setSz(z: Double) {
        s.z = z
    }

    fun setPivot(x: Double, y: Double, z: Double) {
        p.x = x
        p.y = y
        p.z = z
        ip.x = -x
        ip.y = -y
        ip.z = -z
    }

    fun reset() {
        t.x = 0.0
        t.y = 0.0
        t.z = 0.0
        rx.angle = 0.0
        ry.angle = 0.0
        rz.angle = 0.0
        s.x = 1.0
        s.y = 1.0
        s.z = 1.0
        p.x = 0.0
        p.y = 0.0
        p.z = 0.0
        ip.x = 0.0
        ip.y = 0.0
        ip.z = 0.0
    }

    fun resetTSP() {
        t.x = 0.0
        t.y = 0.0
        t.z = 0.0
        s.x = 1.0
        s.y = 1.0
        s.z = 1.0
        p.x = 0.0
        p.y = 0.0
        p.z = 0.0
        ip.x = 0.0
        ip.y = 0.0
        ip.z = 0.0
    }

    fun debug() {
        System.out.println(
            "t = (" +
                t.x.toString() + ", " +
                t.y.toString() + ", " +
                t.z.toString() + ")  " +
                "r = (" +
                rx.angle.toString() + ", " +
                ry.angle.toString() + ", " +
                rz.angle.toString() + ")  " +
                "s = (" +
                s.x.toString() + ", " +
                s.y.toString() + ", " +
                s.z.toString() + ")  " +
                "p = (" +
                p.x.toString() + ", " +
                p.y.toString() + ", " +
                p.z.toString() + ")  " +
                "ip = (" +
                ip.x.toString() + ", " +
                ip.y.toString() + ", " +
                ip.z.toString() + ")"
        )
    }

    init {
        rx.axis = Rotate.X_AXIS
    }

    init {
        ry.axis = Rotate.Y_AXIS
    }

    init {
        rz.axis = Rotate.Z_AXIS
    }
}
