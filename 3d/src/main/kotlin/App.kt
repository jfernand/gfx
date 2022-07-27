
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.DepthTest
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.PerspectiveCamera
import javafx.scene.Scene
import javafx.scene.effect.Glow
import javafx.scene.input.KeyCode
import javafx.scene.input.PickResult
import javafx.scene.paint.Color.*
import javafx.scene.shape.Circle
import javafx.scene.shape.Cylinder
import javafx.scene.shape.Line
import javafx.scene.shape.Sphere
import javafx.scene.transform.Rotate.X_AXIS
import javafx.scene.transform.Rotate.Z_AXIS
import javafx.stage.Stage

const val TILE_SIZE = 30.0
const val TILE_SEPARATION = 1
const val FACE_OFFSET = (TILE_SIZE + TILE_SEPARATION)

class App : Application() {

    private val root = Group()
    private var axisGroup = Group()
    private val world = Xform()
    private val camera = PerspectiveCamera(true)
    private val cameraXform = Xform()
    private val cameraXform2 = Xform()
    private val cameraXform3 = Xform()
    private var mousePosX = 0.0
    private var mousePosY = 0.0
    private var mouseOldX = 0.0
    private var mouseOldY = 0.0
    private var mouseDeltaX = 0.0
    private var mouseDeltaY = 0.0

    private fun buildCamera() {
        println("buildCamera()")
        root.children.add(cameraXform)
        cameraXform.children.add(cameraXform2)
        cameraXform2.children.add(cameraXform3)
        cameraXform3.children.add(camera)
        cameraXform3.setRotateZ(180.0)
        camera.nearClip = CAMERA_NEAR_CLIP
        camera.farClip = CAMERA_FAR_CLIP
        camera.translateZ = CAMERA_INITIAL_DISTANCE
        cameraXform.ry.angle = CAMERA_INITIAL_Y_ANGLE
        cameraXform.rx.angle = CAMERA_INITIAL_X_ANGLE
        root.effect
    }

    private fun setState(result: PickResult) {
        if (result.intersectedNode == null) {
            println(
                ("Scene\n\n"
                    + result.intersectedPoint.toString()) + "\n"
                    + result.intersectedTexCoord.point2DToString() + "\n"
                    + result.intersectedFace.toString() + "\n" + String.format("%.1f", result.intersectedDistance)
            )
        } else {
            result.intersectedNode.effect = Glow(0.8)
            println(
                (result.intersectedNode.id + "\n"
//                    + getCullFace(result.intersectedNode) + "\n"
                    + result.intersectedPoint.toString() + "\n"
//                    + point2DToString(result.intersectedTexCoord) + "\n"
//                    + String.format("%.1f", result.intersectedDistance)
                    )
            )
        }
    }

    private fun handleMouse(scene: Scene, root: Node) {
        scene.onMousePressed = EventHandler { me ->
            mousePosX = me.sceneX
            mousePosY = me.sceneY
            mouseOldX = me.sceneX
            mouseOldY = me.sceneY
            val res: PickResult = me.pickResult
            setState(res)
            me.consume()
        }
        scene.onMouseDragged = EventHandler { me ->
            mouseOldX = mousePosX
            mouseOldY = mousePosY
            mousePosX = me.sceneX
            mousePosY = me.sceneY
            mouseDeltaX = mousePosX - mouseOldX
            mouseDeltaY = mousePosY - mouseOldY
            var modifier = 1.0
            if (me.isControlDown) {
                modifier = CONTROL_MULTIPLIER
            }
            if (me.isShiftDown) {
                modifier = SHIFT_MULTIPLIER
            }
            if (me.isPrimaryButtonDown) {
                cameraXform.ry.angle = cameraXform.ry.angle - mouseDeltaX * MOUSE_SPEED * modifier * ROTATION_SPEED
                cameraXform.rx.angle = cameraXform.rx.angle + mouseDeltaY * MOUSE_SPEED * modifier * ROTATION_SPEED
            } else if (me.isSecondaryButtonDown) {
                val z = camera.translateZ
                val newZ = z + mouseDeltaX * MOUSE_SPEED * modifier
                camera.translateZ = newZ
            } else if (me.isMiddleButtonDown) {
                cameraXform2.t.x = cameraXform2.t.x + mouseDeltaX * MOUSE_SPEED * modifier * TRACK_SPEED
                cameraXform2.t.y = cameraXform2.t.y + mouseDeltaY * MOUSE_SPEED * modifier * TRACK_SPEED
            }
        }
    }

    private fun handleKeyboard(scene: Scene, root: Node) {
        scene.onKeyPressed = EventHandler { event ->
            when (event.code) {
                KeyCode.S -> {
//                    repaint(cube.scramble(randScramble()))
                }
                KeyCode.L -> {
//                    cube.performMove(if (event.isShiftDown) "L'" else "L")
//                    repaint(cube)
                }
                KeyCode.R -> {
//                    cube.performMove(if (event.isShiftDown) "R'" else "R")
//                    repaint(cube)
                }
                KeyCode.U -> {
//                    cube.performMove(if (event.isShiftDown) "U'" else "U")
//                    repaint(cube)
                }
                KeyCode.D -> {
//                    cube.performMove(if (event.isShiftDown) "D'" else "D")
//                    repaint(cube)
                }
                KeyCode.F -> {
//                    cube.performMove(if (event.isShiftDown) "F'" else "F")
//                    repaint(cube)
                }
                KeyCode.B -> {
//                    cube.performMove(if (event.isShiftDown) "B'" else "B")
//                    repaint(cube)
                }
                KeyCode.Y -> {
//                    cube.performMove(if (event.isShiftDown) "y'" else "y")
//                    repaint(cube)
                }
                KeyCode.X -> {
//                    cube.performMove(if (event.isShiftDown) "x'" else "x")
//                    repaint(cube)
                }
                KeyCode.Z -> {
//                cube.performMove(if (event.isShiftDown) "z'" else "z")
//                repaint(cube)
                }
                KeyCode.PERIOD -> {
//                    cube.performMove("R")
//                    cube.performMove("U")
//                    cube.performMove("R'")
//                    cube.performMove("U'")
//                    repaint(cube)
                }
                KeyCode.P -> {
                    cameraXform2.t.x = 0.0
                    cameraXform2.t.y = 0.0
                    camera.translateZ = CAMERA_INITIAL_DISTANCE
                    cameraXform.ry.angle = CAMERA_INITIAL_Y_ANGLE
                    cameraXform.rx.angle = CAMERA_INITIAL_X_ANGLE
                }
                KeyCode.A -> axisGroup.isVisible = !axisGroup.isVisible
                KeyCode.V -> println("V")
            }
        }
    }

//    private fun renderCubie(x: Int, y: Int, z: Int, cubie: Cubie): Group {
//        val group = Group()
//        for (color in cubie.colors) {
//            val box = Box(TILE_SIZE, TILE_SIZE, 1.0)
//            box.id="${color.dir} (${cubie.x}, ${cubie.y}, ${cubie.z})"
//            when (color.dir) {
//                Dir.U -> {
//                    box.transforms.add(Rotate(90.0, Rotate.X_AXIS))
//                    box.translateY = -(TILE_SIZE * 3 / 2 + TILE_SEPARATION)
//                    box.translateX = (x * (TILE_SIZE + TILE_SEPARATION)) - FACE_OFFSET
//                    box.translateZ = (y * (TILE_SIZE + TILE_SEPARATION)) - FACE_OFFSET
//                }
//                Dir.D -> {
//                    box.transforms.add(Rotate(90.0, Rotate.X_AXIS))
//                    box.translateY = (TILE_SIZE * 3 / 2 + TILE_SEPARATION)
//                    box.translateX = (x * (TILE_SIZE + TILE_SEPARATION)) - FACE_OFFSET
//                    box.translateZ = (y * (TILE_SIZE + TILE_SEPARATION)) - FACE_OFFSET
//                }
//                Dir.L -> {
//                    box.transforms.add(Rotate(90.0, Rotate.Y_AXIS))
//                    box.translateX = -(TILE_SIZE * 3 / 2 + TILE_SEPARATION)
//                    box.translateY = (z * (TILE_SIZE + TILE_SEPARATION)) - FACE_OFFSET
//                    box.translateZ = (y * (TILE_SIZE + TILE_SEPARATION)) - FACE_OFFSET
//                }
//                Dir.R -> {
//                    box.transforms.add(Rotate(90.0, Rotate.Y_AXIS))
//                    box.translateX = (TILE_SIZE * 3 / 2 + TILE_SEPARATION)
//                    box.translateY = (z * (TILE_SIZE + TILE_SEPARATION)) - FACE_OFFSET
//                    box.translateZ = (y * (TILE_SIZE + TILE_SEPARATION)) - FACE_OFFSET
//                }
//                Dir.F -> {
//                    box.transforms.add(Rotate(90.0, Rotate.Z_AXIS))
//                    box.translateZ = -(TILE_SIZE * 3 / 2 + TILE_SEPARATION)
//                    box.translateY = (z * (TILE_SIZE + TILE_SEPARATION)) - FACE_OFFSET
//                    box.translateX = (x * (TILE_SIZE + TILE_SEPARATION)) - FACE_OFFSET
//                }
//                Dir.B -> {
//                    box.transforms.add(Rotate(90.0, Rotate.Z_AXIS))
//                    box.translateZ = (TILE_SIZE * 3 / 2 + TILE_SEPARATION)
//                    box.translateY = (z * (TILE_SIZE + TILE_SEPARATION)) - FACE_OFFSET
//                    box.translateX = (x * (TILE_SIZE + TILE_SEPARATION)) - FACE_OFFSET
//                }
//                Dir.A -> {
//                    box.isVisible = true
//                }
//            }
//            when (color.color) {
//                CubeColor.White -> box.material = WHITE
//                CubeColor.Red -> box.material = RED
//                CubeColor.Orange -> box.material = ORANGE
//                CubeColor.Blue -> box.material = BLUE
//                CubeColor.Yellow -> box.material = YELLOW
//                CubeColor.Green -> box.material = GREEN
//                CubeColor.None -> box.material = BLACK
//            }
//            group.children.add(box)
//        }
//        return group
//    }

//    private fun buildCube() {
//        val box = Box(TILE_SIZE * 3, TILE_SIZE * 3, TILE_SIZE * 3)
//        box.material = BLACK
//        renderCubies(cube)
//        root.children.add(box)
//        root.children.add(cubeGroup)
//    }

//    private fun repaint(cube: Cube) {
//        root.children.remove(cubeGroup)
//        cubeGroup = Group()
//        renderCubies(cube)
//        root.children.add(cubeGroup)
//    }

//    private fun renderCubies(cube: Cube) {
//        for (i in 0..2) {
//            for (j in 0..2) {
//                for (k in 0..2) {
//                    cubeGroup.children.add(renderCubie(i, j, k, cube.cubiePos[i][j][k]))
//                }
//            }
//        }
//    }

    override fun start(primaryStage: Stage) {
         setUserAgentStylesheet(STYLESHEET_MODENA);
        println("start()")
        root.children.add(world)
        root.depthTest = DepthTest.ENABLE

        buildCamera()
        axisGroup = buildAxes(AXIS_LENGTH)
        val planes = buildPlanes()
        val rect = Sphere()
        world.children.addAll(axisGroup, rect)
        val scene = Scene(root, 1024.0, 768.0, true)
        scene.fill = TRANSPARENT
        handleKeyboard(scene, world)
        handleMouse(scene, world)
        primaryStage.title = "App"
        primaryStage.scene = scene
        primaryStage.show()
        scene.camera = camera
    }

    private fun buildPlanes(): Any {
        for (i in -250 ..250 step 50) {
            for(j in -250 ..250 step 50) {
                val sphere = Sphere(1.0)
                sphere.translateX = i    .toDouble()
                sphere.translateY = j.toDouble()
            }
        }
    }

    companion object {
        private const val CAMERA_INITIAL_DISTANCE = -450.0
        private const val CAMERA_INITIAL_X_ANGLE = 70.0
        private const val CAMERA_INITIAL_Y_ANGLE = 315.0
        private const val CAMERA_NEAR_CLIP = 0.1
        private const val CAMERA_FAR_CLIP = 10000.0
        private const val AXIS_LENGTH = 250.0
        private const val CONTROL_MULTIPLIER = 0.1
        private const val SHIFT_MULTIPLIER = 10.0
        private const val MOUSE_SPEED = 0.1
        private const val ROTATION_SPEED = 2.0
        private const val TRACK_SPEED = 0.3

        /**
         * The main() method is ignored in correctly deployed JavaFX application.
         * main() serves only as fallback in case the application can not be
         * launched through deployment artifacts, e.g., in IDEs with limited FX
         * support. NetBeans ignores main().
         *
         * @param args the command line arguments
         */
    }
}

fun buildAxes(length: Double): Group {
    println("buildAxes()")
    val xAxis = xAxis(length)
    val yAxis = yAxis(length)
    val zAxis = zAxis(length)
    xAxis.material = redMaterial
    yAxis.material = greenMaterial
    zAxis.material = blueMaterial
    val axisGroup = Group()
    axisGroup.children.addAll(xAxis, yAxis, zAxis)
    axisGroup.isVisible = true
    return axisGroup
}

private fun xAxis(length: Double): Cylinder {
    val xAxis = Cylinder(0.5, length * 2)
    xAxis.rotate = 90.0
    xAxis.rotationAxis = Z_AXIS
    return xAxis
}

private fun yAxis(length: Double): Cylinder {
    return Cylinder(0.5, length * 2)
}

private fun zAxis(length: Double): Cylinder {
    val axis = Cylinder(0.5, length * 2)
    axis.rotate = 90.0
    axis.rotationAxis = X_AXIS
    return axis
}

fun main(args: Array<String>) {
    Application.launch(App::class.java, *args)
}
