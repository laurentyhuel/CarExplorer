package com.lyh.carexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lyh.carexplorer.data.local.entity.CarEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class CarEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var make: String,
    var model: String,
    var year: Int,
    var pictureUrl: String,
    var equipments: String //TODO improve storage of equipments, in another table for instance
) {
    companion object {
        const val TABLE_NAME = "car"
    }
}

