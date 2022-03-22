package com.amir.roomdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//we need the entity annotation
//inside of it we directly specify the table name
@Entity(tableName = "employee-table")

data class EmployeeEntity(
    ////these are going to be my column
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo
    val name: String = "",
    //if we want to give this different name internally, we make use of @ColumnInfo
    //if we don't override the name of a column, it will by default get the name that we gave the property(email)
    @ColumnInfo(name = "email-id")
    val email: String = ""
)
