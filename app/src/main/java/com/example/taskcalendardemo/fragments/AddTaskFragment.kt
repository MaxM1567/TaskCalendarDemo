package com.example.taskcalendardemo.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.taskcalendardemo.MainActivity
import com.example.taskcalendardemo.R
import com.example.taskcalendardemo.databinding.FragmentAddTaskBinding
import com.example.taskcalendardemo.model.Task
import com.example.taskcalendardemo.viewmodel.TaskViewModel
import java.util.Calendar

class AddTaskFragment : Fragment(R.layout.fragment_add_task) {

    private var addTaskBinding: FragmentAddTaskBinding? = null
    private val binding get() = addTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var addTaskView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addTaskBinding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = (activity as MainActivity).taskViewModel
        addTaskView = view

        binding.addTaskData.setText(arguments?.getString("selectedDate") ?: "Unknown Date")

        // Кнопка сохранить задачу
        binding.addTaskButton.setOnClickListener {
            saveTask()
        }

        // Кнопка добавить время
        binding.addTaskTimeButton.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(Calendar.MINUTE)

            TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                // Форматируем выбранное время
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.addTaskTime.text = formattedTime // Устанавливаем время в TextView
            }, hour, minute, true).show() // true для 24-часового формата
        }
    }

    private fun saveTask() {
        val taskTitle = binding.addTaskTitle.text.toString().trim()
        val taskDesc = binding.addTaskDesc.text.toString().trim()
        val selectedDate = arguments?.getString("selectedDate") ?: "Unknown Date"
        val taskTime = binding.addTaskTime.text.toString().trim()

        if (taskTitle.isNotEmpty() && taskTime != "##:##") {
            val task = Task(0, taskTitle, taskDesc, selectedDate, taskTime)
            taskViewModel.addTask(task)

            Toast.makeText(addTaskView.context, "Задача сохранена", Toast.LENGTH_SHORT).show()
            addTaskView.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Toast.makeText(addTaskView.context, "Заполните все поля и выберите время", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        addTaskBinding = null
    }
}