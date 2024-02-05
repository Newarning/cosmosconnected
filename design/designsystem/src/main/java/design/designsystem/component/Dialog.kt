package design.designsystem.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import design.designsystem.CCText


@Composable
fun CCDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    confirmText: String?,
    dismissText: String?,
) {
    AlertDialog(

        title = {
            CCText(text = dialogTitle)
        },
        text = {
            CCText(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            confirmText?.let {
                CCButton(title = it) {
                    onConfirmation()
                }
            }
        },
        dismissButton = {
            dismissText?.let {
                CCButton(title = it) {
                    onDismissRequest()
                }
            }

        }
    )
}