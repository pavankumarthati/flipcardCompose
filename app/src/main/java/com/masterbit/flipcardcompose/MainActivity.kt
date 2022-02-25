package com.masterbit.flipcardcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.masterbit.flipcardcompose.ui.theme.FlipCardComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlipCardComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    var cardFace by remember {mutableStateOf(CardFace.Front)}
                    val infiniteTransition = rememberInfiniteTransition()
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.3f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(500, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )

                    FlipCard(
                        cardFace = cardFace,
                        onClick = {card -> cardFace = card.next},
                        front = {
                            Box(Modifier.fillMaxSize().padding(top = 10.dp, end = 10.dp), contentAlignment = Alignment.TopEnd) {
                                Box(
                                    Modifier
                                        .size(10.dp)
                                        .graphicsLayer {
                                            scaleX = scale
                                            scaleY = scale
                                        }
                                        .clip(CircleShape)
                                        .background(color = Color.Red)
                                        .align(Alignment.TopEnd)
                                )
                            }
                            Text("Front", color = Color.White, modifier = Modifier.align(Alignment.Center))
                        },
                        back = {
                            Box(Modifier.fillMaxSize().padding(top = 10.dp, end = 10.dp), contentAlignment = Alignment.TopEnd) {
                                Box(
                                    Modifier
                                        .size(10.dp)
                                        .graphicsLayer {
                                            scaleX = scale
                                            scaleY = scale
                                        }
                                        .clip(CircleShape)
                                        .background(color = Color.Blue)
                                        .align(Alignment.TopEnd)
                                )
                            }
                            Text(
                                "Back",
                                color = Color.White,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FlipCard(
    cardFace: CardFace,
    onClick: (CardFace) -> Unit,
    front: @Composable BoxScope.() -> Unit,
    back: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints(contentAlignment = Alignment.Center) {
        val constraintScope = this
        val maxWidth = with(LocalDensity.current) {
            constraintScope.maxWidth
        }

        val rotation = animateFloatAsState(
            targetValue = cardFace.angle,
            animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
        )

        Card(
            modifier = Modifier
                .requiredWidth(maxWidth / 2)
                .aspectRatio(1f)
                .wrapContentSize(Alignment.Center)
                .graphicsLayer { rotationY = rotation.value; cameraDistance = 12f * density }
                .clickable { onClick(cardFace) },
            shape = RoundedCornerShape(15.dp)
        ) {
            if (rotation.value <= 90f) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(color = Color.Blue), contentAlignment = Alignment.Center) {
                    front()
                }
            } else {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(color = Color.Red)
                        .graphicsLayer { rotationY = 180f }, contentAlignment = Alignment.Center) {
                    back()
                }
            }
        }
    }
}

enum class CardFace(val angle: Float) {
    Front(0f) {
        override val next: CardFace
            get() = Back
    },
    Back(180f) {
        override val next: CardFace
            get() = Front
    };

    abstract val next: CardFace
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlipCardComposeTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            var cardFace by remember {mutableStateOf(CardFace.Front)}
            FlipCard(
                cardFace = cardFace,
                onClick = {card -> cardFace = card.next},
                front = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color = Color.Blue)
                            .wrapContentSize(
                                Alignment.Center
                            )) {
                        Text("Front", color = Color.White)
                    }
                },
                back = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color = Color.Red)
                            .wrapContentSize(
                                Alignment.Center
                            )) {
                        Text("Back", color = Color.White)
                    }
                }
            )
        }
    }
}