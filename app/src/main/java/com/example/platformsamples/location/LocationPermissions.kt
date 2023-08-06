package com.example.platformsamples.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

/**
 * Simple screen that manages the location permission state
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissions(text: String, rationale: String, locationState: PermissionState) {
    LocationPermissions(
        text = text,
        rationale = rationale,
        locationState = rememberMultiplePermissionsState(permissions = listOf(locationState.permission))
    )
}

/**
 * Simple screen that manages the location permission state
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissions(text: String, rationale: String, locationState: MultiplePermissionsState) {
    var showRationale by remember(locationState) {
        mutableStateOf(false)
    }
    if (showRationale) {
        PermissionRationaleDialog(rationaleState = RationaleState(
            title = "Location Permission Access",
            rationale = rationale,
            onRationaleRely = { proceed ->
                if (proceed) {
                    locationState.launchMultiplePermissionRequest()
                }
                showRationale = false
            }
        ))
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        PermissionRequestButton(isGranted = false, title = text) {
            if (locationState.shouldShowRationale) {
                showRationale = true
            } else {
                locationState.launchMultiplePermissionRequest()
            }

        }
    }

}


/**
 * Simple AlertDialog that displays the given rational state
 */
@Composable
fun PermissionRequestButton(isGranted: Boolean, title: String, onClick: () -> Unit) {
    if (isGranted) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Check",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(text = title, modifier = Modifier.background(Color.Transparent))
        }
    } else {
        Button(onClick = onClick) {
            Text(text = "Request $title")
        }

    }
}

@Preview
@Composable
fun PermissionRequestButtonPreview() {
    PermissionRequestButton(isGranted = true, title = "Location") {

    }
}

/**
 * A button that shows the title or the request permission action.
 */

@Composable
fun PermissionRationaleDialog(
    rationaleState: RationaleState
) {
    AlertDialog(
        onDismissRequest = { rationaleState.onRationaleRely(false) },
        title = {
            Text(text = rationaleState.title)
        },
        text = {
            Text(text = rationaleState.rationale)
        },
        confirmButton = {
            TextButton(onClick = {
                rationaleState.onRationaleRely(true)
            }) {
                Text("Continue")
            }

        },
        dismissButton = {
            TextButton(onClick = {
                rationaleState.onRationaleRely(false)
            }) {
                Text("Dismiss")
            }

        })
}

@Preview
@Composable
fun permissionRationaleDialogPreview() {
    PermissionRationaleDialog(
        RationaleState(
            title = "Request Permission", rationale = "Give me your permission"
        ) {

        }
    )
}

data class RationaleState(
    val title: String,
    val rationale: String,
    val onRationaleRely: (proceed: Boolean) -> Unit
)