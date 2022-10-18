package com.lyh.carexplorer.feature.user.detail

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.lyh.carexplorer.feature.core.ui.AppTopBar
import com.lyh.carexplorer.feature.core.ui.ComposeFileProvider
import com.lyh.carexplorer.feature.user.R
import com.lyh.carexplorer.feature.user.model.UserUi
import org.koin.androidx.compose.getViewModel
import com.lyh.carexplorer.feature.core.R as RCore

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun UserRoute(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = getViewModel(),
    onBackClick: () -> Unit,
) {
    val user by viewModel.user.collectAsStateWithLifecycle()

    UserScreen(
        modifier = modifier,
        user = user,
        viewModel = viewModel,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    user: UserUi,
    viewModel: UserViewModel,
    onBackClick: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = stringResource(id = RCore.string.app_name),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            UserFooter(viewModel::click, user.editable)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            UserDetail(
                user = user,
                viewModel = viewModel,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserDetail(
    modifier: Modifier = Modifier,
    user: UserUi,
    viewModel: UserViewModel,
) {
    val context = LocalContext.current

    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcherPicker =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.data?.let { uri ->
                viewModel.setPhotoFromPicker(context, uri)
            }
        }
    val photoIntent = Intent(MediaStore.ACTION_PICK_IMAGES).apply {
        type = "image/*"
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success && cameraImageUri != null) {
                viewModel.setPhotoFromCamera(cameraImageUri!!)
            }
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        val focusManager = LocalFocusManager.current

        AsyncImage(
            model = user.photoUri?.let(Uri::parse),
            contentDescription = null, //Bad practice, accessibility
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .padding(20.dp)
                .clip(CircleShape)
                .clickable(enabled = user.editable) {
                    viewModel.showPickOrTakePhotoDialog()
                }
        )

        OutlinedTextField(
            value = user.firstName,
            onValueChange = { viewModel.setFirstName(it) },
            placeholder = {
                Text(
                    stringResource(id = R.string.firstName)
                )
            },
            label = {
                Text(
                    stringResource(id = R.string.firstName)
                )
            },
            enabled = user.editable,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        OutlinedTextField(
            value = user.lastName,
            onValueChange = { viewModel.setLastName(it) },
            singleLine = true,
            placeholder = {
                Text(stringResource(id = R.string.lastName))
            },
            label = {
                Text(stringResource(id = R.string.lastName))
            },
            enabled = user.editable,

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        OutlinedTextField(
            value = user.address,
            onValueChange = { viewModel.setAddress(it) },
            placeholder = {
                Text(
                    stringResource(id = R.string.address)
                )
            },
            label = {
                Text(
                    stringResource(id = R.string.address)
                )
            },
            singleLine = true,
            enabled = user.editable,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        OutlinedTextField(
            value = user.birthdayString,
            onValueChange = {},
            placeholder = {
                Text(
                    stringResource(id = R.string.birthday)
                )
            },
            label = {
                Text(
                    stringResource(id = R.string.birthday)
                )
            },
            trailingIcon = if (user.editable) {
                {
                    Icon(
                        painter = painterResource(id = RCore.drawable.calendar),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.clickable { viewModel.showCalendar() }
                    )
                }
            } else null,
            singleLine = true,
            enabled = false,
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        // then show the dialog based on the state
        if (user.showCalendar) {
            DatePickerDialog(
                context,
                { _, year, month, day -> viewModel.setBirthDay(year, month, day) },
                user.birthday.year,
                user.birthday.monthValue,
                user.birthday.dayOfMonth
            ).apply {
                setOnCancelListener { viewModel.hideCalendar() }
            }.show()
        }
        if (user.showPickOrTakePhotoDialog) {
            SelectOrTakePhoto(
                viewModel = viewModel,
                onLaunchCamera = {
                    val imageUri = ComposeFileProvider.getImageUri(context)
                    cameraImageUri = imageUri
                    cameraLauncher.launch(imageUri)
                },
                onLaunchPicker = { launcherPicker.launch(photoIntent) }

            )
        }
    }
}

@Composable
private fun UserFooter(
    onClickButton: () -> Unit,
    editable: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(20.dp),
    ) {
        val txt =
            if (editable) stringResource(id = R.string.save) else stringResource(id = R.string.edit)
        Button(
            onClick = { onClickButton() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = txt)
        }
    }
}
