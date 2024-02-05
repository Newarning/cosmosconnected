package design.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import design.designsystem.theme.ConnectedCosmosTheme

@Composable
fun CCBlackText(modifier: Modifier = Modifier, text: String){
    Text(
        modifier = modifier,
        text = text,
        color = Color.Black
    )
}

@Composable
fun CCAlertText(modifier: Modifier = Modifier, text: String){
    Box(modifier = modifier
        .height(50.dp)
        .background(Color.Red)
        .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = text,
            color = Color.White,
        )
    }

}

@Composable
fun CCText(modifier: Modifier = Modifier, text: String){
    Text(
        modifier = modifier,
        text = text,
        color = ConnectedCosmosTheme.colors.onPrimaryContainer,
        textAlign = TextAlign.Center
    )
}

@Composable
fun CCSufaceText(modifier: Modifier = Modifier, text: String){
    Text(
        modifier = modifier,
        text = text,
        color = ConnectedCosmosTheme.colors.onSurface,
        textAlign = TextAlign.Center
    )
}

@Composable
fun CCTitleSurfaceBoldText(modifier: Modifier = Modifier, text: String){
    Text(
        modifier = modifier,
        text = text,
        fontWeight = FontWeight.Bold,
        color = ConnectedCosmosTheme.colors.onSurface
    )
}

@Composable
fun CCTitleBoldText(modifier: Modifier = Modifier, text: String){
    Text(
        modifier = modifier,
        text = text,
        fontWeight = FontWeight.Bold,
        color = ConnectedCosmosTheme.colors.onPrimaryContainer
    )
}

@Composable
fun CCTitleBlackBoldText(modifier: Modifier = Modifier, text: String){
    Text(
        modifier = modifier,
        text = text,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Preview
@Composable
fun CCTitleBoldTextPreview() {
    CCBlackText(text = "A bold text")
}


@Preview
@Composable
fun CCNormalTextPreview() {
    CCBlackText(text = "A normal text")
}

@Preview
@Composable
fun CCAlertTextPreview() {
    CCAlertText(text = "An alert")
}

