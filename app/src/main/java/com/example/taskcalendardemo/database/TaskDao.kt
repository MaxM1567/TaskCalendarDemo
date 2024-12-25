package com.example.taskcalendardemo.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskcalendardemo.model.Task


@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM TASKS WHERE taskData = :date")
    fun getTasksByDate(date: String): LiveData<List<Task>>

    @Query("SELECT * FROM TASKS ORDER BY id DESC")
    fun getAllTasks(): LiveData<List<Task>>
}