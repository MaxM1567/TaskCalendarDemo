package com.example.taskcalendardemo.repository

import com.example.taskcalendardemo.database.TaskDatabase
import com.example.taskcalendardemo.model.Task


class TaskRepository(private val db: TaskDatabase) {

    suspend fun insertTask(task: Task) = db.getTaskDao().insertTask(task)
    suspend fun deleteTask(task: Task) = db.getTaskDao().deleteTask(task)
    suspend fun updateTask(task: Task) = db.getTaskDao().updateTask(task)

    fun getTasksByDate(date: String) = db.getTaskDao().getTasksByDate(date)
    fun getAllTasks() = db.getTaskDao().getAllTasks()
}