package com.example.earnoccupation.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.Indigo300
import com.example.ui.theme.Purple300
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

/**
 * An interactive 3D Tilt Card that responds to touch drag with real dynamic 3D rotation,
 * dynamic perspective camera depth, Z-axis elevation, and dynamic 3D specular light reflection!
 */
@Composable
fun Interactive3DTiltCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    maxTiltDegrees: Float = 15f,
    glareOpacity: Float = 0.35f,
    content: @Composable BoxScope.() -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var componentSize by remember { mutableStateOf(IntSize.Zero) }

    val rotationXAnim = remember { Animatable(0f) }
    val rotationYAnim = remember { Animatable(0f) }
    val pressScaleAnim = remember { Animatable(1f) }

    // Dynamic light sheen center relative coordinates (0..1)
    var lightSourceOffset by remember { mutableStateOf(Offset(0.5f, 0.5f)) }

    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                componentSize = coordinates.size
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        coroutineScope.launch {
                            pressScaleAnim.animateTo(1.03f, spring(stiffness = 500f))
                        }
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            launch { rotationXAnim.animateTo(0f, spring(dampingRatio = 0.6f, stiffness = 400f)) }
                            launch { rotationYAnim.animateTo(0f, spring(dampingRatio = 0.6f, stiffness = 400f)) }
                            launch { pressScaleAnim.animateTo(1f, spring(dampingRatio = 0.6f, stiffness = 400f)) }
                            lightSourceOffset = Offset(0.5f, 0.5f)
                        }
                    },
                    onDragCancel = {
                        coroutineScope.launch {
                            launch { rotationXAnim.animateTo(0f, spring()) }
                            launch { rotationYAnim.animateTo(0f, spring()) }
                            launch { pressScaleAnim.animateTo(1f, spring()) }
                            lightSourceOffset = Offset(0.5f, 0.5f)
                        }
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        if (componentSize.width > 0 && componentSize.height > 0) {
                            val touchX = change.position.x
                            val touchY = change.position.y

                            val normX = (touchX / componentSize.width - 0.5f).coerceIn(-0.5f, 0.5f)
                            val normY = (touchY / componentSize.height - 0.5f).coerceIn(-0.5f, 0.5f)

                            val targetRotY = normX * maxTiltDegrees * 2f
                            val targetRotX = -normY * maxTiltDegrees * 2f

                            lightSourceOffset = Offset(
                                (0.5f + normX * 1.2f).coerceIn(0f, 1f),
                                (0.5f + normY * 1.2f).coerceIn(0f, 1f)
                            )

                            coroutineScope.launch {
                                launch { rotationXAnim.snapTo(targetRotX) }
                                launch { rotationYAnim.snapTo(targetRotY) }
                            }
                        }
                    }
                )
            }
            .graphicsLayer {
                rotationX = rotationXAnim.value
                rotationY = rotationYAnim.value
                scaleX = pressScaleAnim.value
                scaleY = pressScaleAnim.value
                cameraDistance = 18f * density
            }
            .clip(shape)
    ) {
        content()

        // Dynamic 3D Specular Light Overlay
        if (glareOpacity > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = glareOpacity),
                                Color.White.copy(alpha = glareOpacity * 0.3f),
                                Color.Transparent
                            ),
                            center = Offset(
                                x = lightSourceOffset.x * (componentSize.width.takeIf { it > 0 } ?: 300).toFloat(),
                                y = lightSourceOffset.y * (componentSize.height.takeIf { it > 0 } ?: 300).toFloat()
                            ),
                            radius = (componentSize.width.coerceAtLeast(1) * 0.9f)
                        )
                    )
            )
        }
    }
}

/**
 * An interactive 3D Floating Career Wireframe Cube that rotates continuously in isometric 3D space
 * and spins faster when swiped by the user!
 */
@Composable
fun Interactive3DCareerCube(
    modifier: Modifier = Modifier,
    cubeSize: Dp = 120.dp
) {
    var manualSpinAngleX by remember { mutableFloatStateOf(0f) }
    var manualSpinAngleY by remember { mutableFloatStateOf(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "cube3d")
    val autoRotate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "autoRotate"
    )

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    manualSpinAngleY += dragAmount.x * 0.5f
                    manualSpinAngleX -= dragAmount.y * 0.5f
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val totalRotY = (autoRotate + manualSpinAngleY) * (Math.PI / 180.0)
            val totalRotX = (30f + manualSpinAngleX) * (Math.PI / 180.0)

            val sizePx = cubeSize.toPx() / 2f
            val cx = size.width / 2f
            val cy = size.height / 2f

            // 8 Vertices of 3D Cube
            val vertices = arrayOf(
                doubleArrayOf(-sizePx.toDouble(), -sizePx.toDouble(), -sizePx.toDouble()),
                doubleArrayOf(sizePx.toDouble(), -sizePx.toDouble(), -sizePx.toDouble()),
                doubleArrayOf(sizePx.toDouble(), sizePx.toDouble(), -sizePx.toDouble()),
                doubleArrayOf(-sizePx.toDouble(), sizePx.toDouble(), -sizePx.toDouble()),
                doubleArrayOf(-sizePx.toDouble(), -sizePx.toDouble(), sizePx.toDouble()),
                doubleArrayOf(sizePx.toDouble(), -sizePx.toDouble(), sizePx.toDouble()),
                doubleArrayOf(sizePx.toDouble(), sizePx.toDouble(), sizePx.toDouble()),
                doubleArrayOf(-sizePx.toDouble(), sizePx.toDouble(), sizePx.toDouble())
            )

            // Projected 2D Points
            val projected = vertices.map { v ->
                // Rotate Y
                val x1 = v[0] * cos(totalRotY) + v[2] * sin(totalRotY)
                val y1 = v[1]
                val z1 = -v[0] * sin(totalRotY) + v[2] * cos(totalRotY)

                // Rotate X
                val x2 = x1
                val y2 = y1 * cos(totalRotX) - z1 * sin(totalRotX)
                val z2 = y1 * sin(totalRotX) + z1 * cos(totalRotX)

                // Perspective projection factor
                val perspective = 500.0 / (500.0 + z2)
                Offset(
                    (cx + x2 * perspective).toFloat(),
                    (cy + y2 * perspective).toFloat()
                )
            }

            // 12 Edges connecting vertices
            val edges = arrayOf(
                Pair(0, 1), Pair(1, 2), Pair(2, 3), Pair(3, 0), // Back face
                Pair(4, 5), Pair(5, 6), Pair(6, 7), Pair(7, 4), // Front face
                Pair(0, 4), Pair(1, 5), Pair(2, 6), Pair(3, 7)  // Connecting edges
            )

            // Draw glowing 3D wireframe edges
            edges.forEach { (startIdx, endIdx) ->
                drawLine(
                    brush = Brush.linearGradient(
                        colors = listOf(Indigo300, CyanAccent, EmeraldGreen)
                    ),
                    start = projected[startIdx],
                    end = projected[endIdx],
                    strokeWidth = 4f
                )
            }

            // Draw 3D corner vertex glowing nodes
            projected.forEach { point ->
                drawCircle(
                    color = Color.White,
                    radius = 6f,
                    center = point
                )
                drawCircle(
                    color = CyanAccent.copy(alpha = 0.5f),
                    radius = 12f,
                    center = point
                )
            }
        }
    }
}

/**
 * Interactive 3D Orbit Ring around icons or stats displaying occupations & salary growth
 */
@Composable
fun Interactive3DOrbitBadge(
    modifier: Modifier = Modifier,
    badgeText: String,
    badgeColor: Color = Indigo300,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orbit")
    val rotZ by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotZ"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Orbit ring canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val rx = size.width / 2f - 8.dp.toPx()
            val ry = size.height / 4f

            rotate(degrees = -15f, pivot = center) {
                drawPath(
                    path = Path().apply {
                        addOval(androidx.compose.ui.geometry.Rect(center.x - rx, center.y - ry, center.x + rx, center.y + ry))
                    },
                    color = badgeColor.copy(alpha = 0.4f),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }

        content()
    }
}
