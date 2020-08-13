package com.chan.marriagepraposals.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by Chan on 11/08/20.
 */

@Entity(tableName = "user", indices = [Index(value = ["uid"], unique = true)])
data class User(
    @PrimaryKey var uid : String,
    @ColumnInfo var name : String,
    @ColumnInfo var age : Int,
    @ColumnInfo var dob : String,
    @ColumnInfo var address : String,
    @ColumnInfo var picture : String,
    @ColumnInfo var username : String,
    @ColumnInfo var password : String,
    @ColumnInfo var email : String,
    @ColumnInfo var status : String
)