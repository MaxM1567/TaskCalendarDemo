package com.example.taskcalendardemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.taskcalendardemo.MainActivity
import com.example.taskcalendardemo.R
import com.example.taskcalendardemo.adapter.TaskAdapter
import com.example.taskcalendardemo.databinding.FragmentHomeBinding
import com.example.taskcalendardemo.model.Task
import com.example.taskcalendardemo.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var tasksViewModel: TaskViewModel
    private lateinit var tasksAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tasksViewModel = (activity as MainActivity).taskViewModel
        setupHomeRecyclerView()

        // Обработчик нажатия на календарь
        var selectedDate = convertToDateString(binding.calendarView.date) // Дата по умолчанию
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$year-${month + 1}-$dayOfMonth"
            observeTasksByDate(selectedDate) // Обновляем список задач для выбранной даты
        }

        // Обработчик нажатия на кнопку add
        binding.addTaskFab.setOnClickListener {
            // Передаем дату в AddTaskFragment через Bundle
            val bundle = Bundle().apply {
                putString("selectedDate", selectedDate)
            }
            it.findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment, bundle)
        }

        // Первоначальная загрузка задач для текущей даты
        observeTasksByDate(selectedDate)
    }

    private fun updateUI(task: List<Task>?) {
        if (task != null) {
            if (task.isNotEmpty()) {
                binding.emptyTasksText.visibility = View.GONE

                binding.homeRecyclerView.visibility = View.VISIBLE
            } else {
                binding.emptyTasksText.visibility = View.VISIBLE

                binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun setupHomeRecyclerView() {
        tasksAdapter = TaskAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = tasksAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

    private fun convertToDateString(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }

    private fun observeTasksByDate(date: String) {
        tasksViewModel.getTasksByDate(date).observe(viewLifecycleOwner) { tasks ->
            val sortedTasks = tasks.sortedByDescending { task ->
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                timeFormat.parse(task.taskTime)
            }

            tasksAdapter.differ.submitList(sortedTasks)
            updateUI(tasks)
        }
    }
}