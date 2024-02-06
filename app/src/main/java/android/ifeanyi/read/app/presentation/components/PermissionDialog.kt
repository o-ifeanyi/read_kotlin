package android.ifeanyi.read.app.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        modifier = modifier.background(color = MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.medium),
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Permission required", textAlign = TextAlign.Center)
                Text(
                    text = permissionTextProvider.getDescription(
                        isPermanentlyDeclined = isPermanentlyDeclined
                    ),
                    textAlign = TextAlign.Center
                )
                HorizontalDivider()
                TextButton(
                    onClick = {
                        if (isPermanentlyDeclined) {
                            onGoToAppSettingsClick()
                        } else {
                            onOkClick()
                        }
                    }
                ) {
                    if(isPermanentlyDeclined) {
                        Text(text = "Grant permission")
                    } else {
                        Text(text = "OK")
                    }
                }
            }
        },
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class StoragePermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined access to storage. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs access to your storage to save and open picked files."
        }
    }
}
