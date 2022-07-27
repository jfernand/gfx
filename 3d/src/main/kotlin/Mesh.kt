/*
 * Copyright (c) 2014, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.application.Application
import javafx.application.ConditionalFeature
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.geometry.Point3D
import javafx.geometry.VPos
import javafx.scene.Camera
import javafx.scene.DepthTest
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.PerspectiveCamera
import javafx.scene.PointLight
import javafx.scene.Scene
import javafx.scene.SceneAntialiasing
import javafx.scene.SubScene
import javafx.scene.input.MouseEvent
import javafx.scene.input.PickResult
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.CullFace
import javafx.scene.shape.DrawMode
import javafx.scene.shape.MeshView
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape3D
import javafx.scene.shape.TriangleMesh
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import javafx.scene.transform.Rotate
import javafx.scene.transform.Rotate.X_AXIS
import javafx.scene.transform.Rotate.Z_AXIS
import javafx.stage.Stage
import javafx.util.Duration

/**
 * A Simple Picking Example
 */
class PickMesh3D : Application() {
    private lateinit var meshView: MeshView
    var caption: Text = Text()
    private var data: Text = Text("-- None --\n\n\n\n")

    override fun start(stage: Stage) {
        println("Starting")
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            throw RuntimeException("*** ERROR: common conditional SCENE3D is not supported")
        }
        stage.show()
        stage.title = "JavaFX 3D Simple Picking demo"
        val camera = PerspectiveCamera()
        val rotateZ = Rotate(0.0, Z_AXIS)
        val rotateX = Rotate(30.0, X_AXIS)
        camera.transforms.addAll(rotateX, rotateZ)
        rotateZ.timeline()
        val pickResultPanel = createOverlay()
        val root = Group()
        root.children.addAll(pickResultPanel, createSubScene(camera))
        val scene = Scene(root, 800.0, 800.0)
        scene.fill = Color.color(0.2, 0.2, 0.2)
        scene.onKeyTyped = EventHandler { e ->
            when (e.character) {
                "L" -> {
                    System.err.print("L ")
                    val wireframe = meshView!!.drawMode == DrawMode.LINE
                    meshView!!.drawMode = if (wireframe) DrawMode.FILL else DrawMode.LINE
                }
            }
        }
        stage.scene = scene
        stage.requestFocus()
    }

    private fun createSimpleMesh(): Node {
        val triangleMesh = buildTriangleMesh(2, 2, 30f)
        meshView = MeshView(triangleMesh)
        activateShape(meshView!!, "Simple Mesh")
        meshView!!.drawMode = DrawMode.FILL
        meshView!!.cullFace = CullFace.BACK
        val material = PhongMaterial()
        material.diffuseColor = Color.GOLD
        material.specularColor = Color.rgb(30, 30, 30)
        meshView!!.material = material
        val group: Node = Group(meshView)
        group.translateX = 550.0
        group.translateY = 550.0
        return group
    }

    private fun activateShape(shape: Shape3D, name: String) {
        shape.id = name
        val moveHandler = EventHandler<MouseEvent> { event ->
            val res = event.pickResult
            setState(res)
            event.consume()
        }
        shape.onMouseMoved = moveHandler
        shape.onMouseDragOver = moveHandler
        shape.onMouseEntered = EventHandler { event ->
            val res = event.pickResult
            if (res == null) {
                System.err.println("Mouse entered has not pickResult")
            }
            setState(res)
        }
        shape.onMouseExited = EventHandler { event ->
            val res = event.pickResult
            if (res == null) {
                System.err.println("Mouse exited has not pickResult")
            }
            setState(res)
            event.consume()
        }
    }

    private fun createOverlay(): Node {
        val hBox = HBox(10.0)
        caption = Text("Node:\n\nPoint:\nTexture Coord:\nFace:\nDistance:")
        caption.font = Font.font("Inconsolata for Powerline", 18.0)
        caption.textOrigin = VPos.TOP
        caption.textAlignment = TextAlignment.RIGHT
        data.font = Font.font("Inconsolata for Powerline", 18.0)
        data.textOrigin = VPos.TOP
        data.textAlignment = TextAlignment.LEFT
        val rect = Rectangle(300.0, 150.0, Color.color(0.2, 0.5, 0.3, 0.8))
        hBox.children.addAll(caption, data)
        return Group(rect, hBox)
    }

    private fun createSubScene(camera: Camera): SubScene {
        val simpleMesh = createSimpleMesh()
        val parent = Group(simpleMesh)
        parent.translateZ = 600.0
        parent.translateX = -150.0
        parent.translateY = -200.0
        parent.scaleX = 0.8
        parent.scaleY = 0.8
        parent.scaleZ = 0.8
        val pointLight = PointLight(Color.ANTIQUEWHITE)
        pointLight.translateX = 100.0
        pointLight.translateY = 100.0
        pointLight.translateZ = -300.0
        val root = Group(parent, pointLight, Group(camera))
        root.depthTest = DepthTest.ENABLE
        val subScene = SubScene(root, 800.0, 800.0, true, SceneAntialiasing.BALANCED)
        subScene.camera = camera
        subScene.fill = Color.TRANSPARENT
        subScene.id = "SubScene"
        return subScene
    }

    private fun setState(result: PickResult?) {
        if (result!!.intersectedNode == null) {
            data.text = """
                Scene
                
                ${result.intersectedPoint.asString()}
                ${result.intersectedTexCoord.point2DToString()}
                ${result.intersectedFace}
                ${String.format("%.1f", result.intersectedDistance)}
                """.trimIndent()
        } else {
            data.text = """
                ${result.intersectedNode.id}
                ${getCullFace(result.intersectedNode)}
                ${result.intersectedPoint.asString()}
                ${result.intersectedTexCoord.point2DToString()}
                ${result.intersectedFace}
                ${String.format("%.1f", result.intersectedDistance)}
                """.trimIndent()
        }
    }

    companion object {
        private const val minX = -10f
        private const val minY = -10f
        private const val maxX = 10f
        private const val maxY = 10f
        fun buildTriangleMesh(subDivX: Int, subDivY: Int, scale: Float): TriangleMesh {
            val pointSize = 3
            val texCoordSize = 2
            // 3 point indices and 3 texCoord indices per triangle
            val faceSize = 6
            val numDivX = subDivX + 1
            val numVerts = (subDivY + 1) * numDivX
            val points = FloatArray(numVerts * pointSize)
            val texCoords = FloatArray(numVerts * texCoordSize)
            val faceCount = subDivX * subDivY * 2
            val faces = IntArray(faceCount * faceSize)

            // Create points and texCoords
            for (y in 0..subDivY) {
                val dy = y.toFloat() / subDivY
                val fy = (1 - dy) * minY + dy * maxY.toDouble()
                for (x in 0..subDivX) {
                    val dx = x.toFloat() / subDivX
                    val fx = (1 - dx) * minX + dx * maxX.toDouble()
                    var index = y * numDivX * pointSize + x * pointSize
                    points[index] = fx.toFloat() * scale
                    points[index + 1] = fy.toFloat() * scale
                    points[index + 2] = 0.0f
                    index = y * numDivX * texCoordSize + x * texCoordSize
                    texCoords[index] = dx
                    texCoords[index + 1] = dy
                }
            }

            // Create faces
            for (y in 0 until subDivY) {
                for (x in 0 until subDivX) {
                    val p00 = y * numDivX + x
                    val p01 = p00 + 1
                    val p10 = p00 + numDivX
                    val p11 = p10 + 1
                    val tc00 = y * numDivX + x
                    val tc01 = tc00 + 1
                    val tc10 = tc00 + numDivX
                    val tc11 = tc10 + 1
                    var index = (y * subDivX * faceSize + x * faceSize) * 2
                    faces[index + 0] = p00
                    faces[index + 1] = tc00
                    faces[index + 2] = p10
                    faces[index + 3] = tc10
                    faces[index + 4] = p11
                    faces[index + 5] = tc11
                    index += faceSize
                    faces[index + 0] = p11
                    faces[index + 1] = tc11
                    faces[index + 2] = p01
                    faces[index + 3] = tc01
                    faces[index + 4] = p00
                    faces[index + 5] = tc00
                }
            }
            val triangleMesh = TriangleMesh()
            triangleMesh.points.setAll(*points)
            triangleMesh.texCoords.setAll(*texCoords)
            triangleMesh.faces.setAll(*faces)
            return triangleMesh
        }

        private fun getCullFace(n: Node): String =
            if (n is Shape3D) {
                "(CullFace." + n.cullFace + ")"
            } else ""
    }
}

fun Point3D?.asString(): String = if (this == null) "null" else String.format("(%.1f, %.1f, %.1f)", x, y, z)

fun Point2D?.point2DToString(): String {
    return if (this == null) {
        "null"
    } else
        String.format("(%.2f, %.2f)", x, y)
}

fun main(args: Array<String>) {
    Application.launch(PickMesh3D::class.java, *args)
}

fun Rotate.timeline() {
    val timeline = Timeline(
        KeyFrame(
            Duration.seconds(0.0),
            KeyValue(this.angleProperty(), 0)
        ),
        KeyFrame(
            Duration.seconds(15.0),
            KeyValue(this.angleProperty(), 360)
        )
    )
    timeline.cycleCount = Timeline.INDEFINITE
    timeline.play()
}
