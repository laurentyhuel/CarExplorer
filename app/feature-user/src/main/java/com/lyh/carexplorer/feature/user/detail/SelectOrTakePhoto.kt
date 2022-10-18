package com.lyh.carexplorer.feature.user.detail

import android.Manifest
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.lyh.carexplorer.feature.core.ui.ComposeFileProvider
import com.lyh.carexplorer.feature.user.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SelectOrTakePhoto(
    viewModel: UserViewModel,
    onLaunchPicker: () -> Unit,
    onLaunchCamera: () -> Unit,
) {


    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    Dialog(
        onDismissRequest = { viewModel.hidePickOrTakePhotoDialog() }) {
        Column {
            Text(
                text = stringResource(id = R.string.dialog_select_take_photo_title),
                style = MaterialTheme.typography.titleMedium
            )
            if (viewModel.isPhotoPickerAvailable()) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.hidePickOrTakePhotoDialog()
                        onLaunchPicker()
                    }
                ) {
                    Text(stringResource(id = R.string.select_photo))
                }
            } else {
                Text(
                    text = stringResource(id = R.string.select_photo_unsupported),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            when (cameraPermissionState.status) {
                is PermissionStatus.Denied -> {
                    Text(
                        text = stringResource(id = R.string.permission_camera),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { cameraPermissionState.launchPermissionRequest() }
                    ) {
                        Text(stringResource(id = R.string.request_permission))
                    }
                }
                PermissionStatus.Granted -> {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.hidePickOrTakePhotoDialog()
                            onLaunchCamera()
                        }
                    ) {
                        Text(stringResource(id = R.string.take_photo))
                    }
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.hidePickOrTakePhotoDialog() }
            ) {
                Text(stringResource(id = com.lyh.carexplorer.feature.core.R.string.cancel))
            }
        }

    }
}
