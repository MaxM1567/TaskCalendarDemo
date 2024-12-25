package com.example.taskcalendardemo.fragments

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.taskcalendardemo.MainActivity
import com.example.taskcalendardemo.R
import com.example.taskcalendardemo.databinding.FragmentEditTaskBinding
import com.example.taskcalendardemo.model.Task
import com.example.taskcalendardemo.viewmodel.TaskViewModel
import java.util.Calendar

class EditTaskFragment : Fragment(R.layout.fragment_edit_task) {

    private var editTaskBinding: FragmentEditTaskBinding? = null
    private val binding get() = editTaskBinding!!

    private lateinit var tasksViewModel: TaskViewModel
    private lateinit var currentTask: Task

    private val args: EditTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editTaskBinding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tasksViewModel = (activity as MainActivity).taskViewModel
        currentTask = args.task!!

        // Заполняем поля редактирования
        binding.editTaskTitle.setText(currentTask.taskTitle)
        binding.editTaskDesc.setText(currentTask.taskDesc)
        binding.editTaskData.setText(currentTask.taskData)
        binding.editTaskTime.setText(currentTask.taskTime)

        // Сохранение изменений
        binding.editTaskFab.setOnClickListener {
            val taskTitle = binding.editTaskTitle.text.toString().trim()
            val taskDesc = binding.editTaskDesc.text.toString().trim()
            val taskTime = binding.editTaskTime.text.toString().trim()

            if (taskTitle.isNotEmpty()) {
                val task = Task(currentTask.id, taskTitle, taskDesc, currentTask.taskData, taskTime)
                tasksViewModel.updateTask(task)

                view.findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                Toast.makeText(context, "Please enter task title", Toast.LENGTH_SHORT).show()
            }
        }

        // Кнопка добавить время
        binding.editTaskTimeButton.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(Calendar.MINUTE)

            TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                // Форматируем выбранное время
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.editTaskTime.text = formattedTime // Устанавливаем время в TextView
            }, hour, minute, true).show() // true для 24-часового формата
        }

        // Удаление задачи
        binding.deleteTaskFab.setOnClickListener {
            deleteTask()
        }
    }

    private fun deleteTask() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Task")
            setMessage("Do you want to delete this task?")
            setPositiveButton("Delete") { _, _ ->
                tasksViewModel.deleteTask(currentTask)
                Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        editTaskBinding = null
    }
}
