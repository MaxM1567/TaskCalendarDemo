package com.example.taskcalendardemo.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "tasks")
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val taskTitle: String,
    val taskDesc: String,
    val taskData: String,
    val taskTime: String
) : Parcelable