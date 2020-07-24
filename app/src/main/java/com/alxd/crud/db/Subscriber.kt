package com.alxd.crud.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Subscriber_data_table")
data class Subscriber (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Subscriber_id")
    var id : Int,
    @ColumnInfo(name = "Subscriber_name")
    var name : String,
    @ColumnInfo(name = "Subscriber_email")
    var email : String
)