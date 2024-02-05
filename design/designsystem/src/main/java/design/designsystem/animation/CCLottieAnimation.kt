package design.designsystem.animation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun CCLottieAnimation(modifier: Modifier = Modifier, assetTitle: String) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(assetTitle))
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}