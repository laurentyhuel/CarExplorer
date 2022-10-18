package com.lyh.carexplorer.feature.user.model

import java.time.LocalDate

data class UserUi(
    val id: Int? = null,
    val photoUri: String? = null,
    val firstName: String = "",
    val lastName: String = "",
    val address: String = "",
    val birthdayString: String = "",
    val birthday: LocalDate = LocalDate.now(),
    val editable: Boolean = false,
    val showCalendar: Boolean = false,
    val showPickOrTakePhotoDialog: Boolean = false
)