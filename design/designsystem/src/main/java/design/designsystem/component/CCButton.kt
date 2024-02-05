package design.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import design.designsystem.theme.ConnectedCosmosTheme

const val buttonHeight = 50

@Composable
fun CCButton(
    modifier: Modifier = Modifier,
    title: String,
    backgroundColor: Color = ConnectedCosmosTheme.colors.primary,
    textColor: Color = Color.White,
    height : Dp = buttonHeight.dp,
    onclick: () -> Unit) {
    Button(
        modifier = modifier
            .height(height),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor, contentColor = textColor),
        shape = RoundedCornerShape(50),
        onClick = {
            onclick.invoke()
        }) {
        Text(text = title)
    }
}