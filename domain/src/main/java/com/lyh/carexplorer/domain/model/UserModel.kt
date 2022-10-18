package com.lyh.carexplorer.domain.model

data class UserModel(
    var id: Int?,
    var photoUri: String?,
    var firstName: String?,
    var lastName: String?,
    var address: String?,
    var birthday: Long?
)
