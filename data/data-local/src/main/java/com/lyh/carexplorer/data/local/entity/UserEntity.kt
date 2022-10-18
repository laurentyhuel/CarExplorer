package com.lyh.carexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lyh.carexplorer.data.local.entity.UserEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    var photoUri: String?,
    var firstName: String?,
    var lastName: String?,
    var address: String?,
    var birthday: Long?
) {
    companion object {
        const val TABLE_NAME = "user"
    }
}