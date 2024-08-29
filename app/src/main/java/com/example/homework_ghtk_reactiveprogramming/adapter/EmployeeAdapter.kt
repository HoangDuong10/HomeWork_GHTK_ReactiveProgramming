package com.example.homework_ghtk_reactiveprogramming.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_ghtk_reactiveprogramming.databinding.ItemEmployeeBinding
import com.example.homework_ghtk_reactiveprogramming.model.Employee
import java.text.SimpleDateFormat
import java.util.Locale

class EmployeeAdapter :
    ListAdapter<Employee, EmployeeAdapter.EmployeeViewHolder>(EmployeeDiffCallback()) {
     var onItemClick: ((position: Int) -> Unit)? = null
    class EmployeeViewHolder(val binding: ItemEmployeeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val binding = ItemEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = getItem(position)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(employee.dateOfBirth)
        holder.binding.apply {
            tvName.text = employee.name
            tvDateOfBirth.text = formattedDate
            tvAddress.text = employee.address
        }
        holder.binding.ivEmployeeDelete.setOnClickListener{
            onItemClick?.invoke(position)
        }
    }

    class EmployeeDiffCallback : DiffUtil.ItemCallback<Employee>() {
        override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem == newItem
        }
    }
}