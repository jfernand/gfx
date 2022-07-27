import javafx.scene.paint.Color
import javafx.scene.paint.Color.*
import javafx.scene.paint.PhongMaterial

private fun makePhongMaterial(diffuse: Color, specular: Color): PhongMaterial {
    val material = PhongMaterial()
    material.diffuseColor = diffuse.translucent(.3)
    material.specularColor = specular.translucent(.3)
    return material
}

val redMaterial = makePhongMaterial(DARKRED, RED)
val greenMaterial = makePhongMaterial(DARKGREEN, GREEN)
val blueMaterial = makePhongMaterial(DARKBLUE, BLUE)
val whiteMaterial = makePhongMaterial(WHITESMOKE, WHITE)
val yellowMaterial = makePhongMaterial(color(0.5, 0.5, 0.0), YELLOW)
val orangeMaterial = makePhongMaterial(DARKORANGE, ORANGE)
val blackMaterial = makePhongMaterial(BLACK, BLACK)

operator fun Color.plus(color: Color): Color = Color(
    red + color.red,
    green + color.green,
    blue + color.blue,
    opacity + color.opacity,
) / 2.0

fun Color.translucent(alpha: Double) = Color(red, green, blue, alpha)

operator fun Color.div(c: Double) = Color(red / c, green / c, blue / c, opacity / c)
