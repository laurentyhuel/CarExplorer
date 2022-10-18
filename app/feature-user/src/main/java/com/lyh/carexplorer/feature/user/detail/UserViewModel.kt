package com.lyh.carexplorer.feature.user.detail

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.FileUtils
import android.os.ext.SdkExtensions.getExtensionVersion
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyh.carexplorer.data.core.AppDispatchers
import com.lyh.carexplorer.domain.UserUseCase
import com.lyh.carexplorer.feature.user.mapper.toIsoDate
import com.lyh.carexplorer.feature.user.mapper.toLocalDate
import com.lyh.carexplorer.feature.user.mapper.toModel
import com.lyh.carexplorer.feature.user.model.UserUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.time.LocalDate

class UserViewModel(
    private val userUseCase: UserUseCase,
    private val dispatchers: AppDispatchers
) : ViewModel() {

    private val _user = MutableStateFlow(UserUi())
    val user: StateFlow<UserUi> = _user.asStateFlow()

    init {
        viewModelScope.launch {
            val userModel = userUseCase.getUser()
            userModel?.let {
                val birthday: LocalDate
                val birthdayString: String
                val birthdayLong = userModel.birthday

                if (birthdayLong != null) {
                    birthday = birthdayLong.toLocalDate()
                    birthdayString = birthday.toIsoDate()
                } else {
                    birthday = LocalDate.now()
                    birthdayString = ""
                }

                _user.value = _user.value.copy(
                    id = userModel.id,
                    photoUri = userModel.photoUri,
                    firstName = userModel.firstName ?: "",
                    lastName = userModel.lastName ?: "",
                    address = userModel.address ?: "",
                    birthday = birthday,
                    birthdayString = birthdayString
                )
            }
        }
    }

    fun setFirstName(it: String) {
        _user.value = _user.value.copy(firstName = it)
    }

    fun setLastName(it: String) {
        _user.value = _user.value.copy(lastName = it)
    }

    fun setAddress(it: String) {
        _user.value = _user.value.copy(address = it)
    }

    fun setBirthDay(year: Int, month: Int, day: Int) {
        val value = LocalDate.of(year, month, day)
        _user.value = _user.value.copy(
            birthday = value,
            birthdayString = value.toIsoDate(),
            showCalendar = false
        )
    }

    fun showCalendar() {
        _user.value = _user.value.copy(showCalendar = true)
    }

    fun hideCalendar() {
        _user.value = _user.value.copy(showCalendar = false)
    }

    fun showPickOrTakePhotoDialog() {
        _user.value = _user.value.copy(showPickOrTakePhotoDialog = true)
    }

    fun hidePickOrTakePhotoDialog() {
        _user.value = _user.value.copy(showPickOrTakePhotoDialog = false)
    }

    fun setPhotoFromPicker(context: Context, uri: Uri) {
        viewModelScope.launch {
            val picture = saveAccountImage(context, uri)
            if (picture != null) {
                _user.value = _user.value.copy(photoUri = picture.toString())
            }
        }
    }

    fun setPhotoFromCamera(uri: Uri) {
        _user.value = _user.value.copy(photoUri = uri.toString())
    }

    fun click() {
        if (_user.value.editable) {
            viewModelScope.launch {
                userUseCase.updateUser(_user.value.toModel())
                _user.value = _user.value.copy(editable = false)
            }
        } else {
            _user.value = _user.value.copy(editable = true)
        }
    }

    fun isPhotoPickerAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getExtensionVersion(Build.VERSION_CODES.R) >= 2
        } else {
            false
        }
    }


    private suspend fun saveAccountImage(context: Context, uri: Uri): Uri? =
        withContext(dispatchers.io) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                inputStream?.let {
                    val picture = File.createTempFile(
                        "account-image-",
                        "${System.currentTimeMillis()}",
                        context.cacheDir
                    )
                    FileUtils.copy(inputStream, picture.outputStream())
                    return@withContext picture.toUri()
                }
                null
            } catch (e: Exception) {
                Timber.e("Failed to save image of $uri", e)
                null
            }
        }
}
