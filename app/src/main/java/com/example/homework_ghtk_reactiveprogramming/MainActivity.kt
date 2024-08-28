package com.example.homework_ghtk_reactiveprogramming


import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homework_ghtk_reactiveprogramming.adapter.EmployeeAdapter
import com.example.homework_ghtk_reactiveprogramming.databinding.ActivityMainBinding
import com.example.homework_ghtk_reactiveprogramming.dialog.AddEmployeeDialogFragment
import com.example.homework_ghtk_reactiveprogramming.viewmodel.SearchViewModel
import com.example.homework_ghtk_reactiveprogramming.dialog.DialogHandler
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var employeeAdapter: EmployeeAdapter
    private lateinit var viewModel: SearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding.rcvEmployee.layoutManager = LinearLayoutManager(this)
        employeeAdapter = EmployeeAdapter()
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.rcvEmployee.addItemDecoration(dividerItemDecoration)
        binding.rcvEmployee.adapter = employeeAdapter


        binding.tvDateOfBirth.setOnClickListener {
            DialogHandler.createSingleItemDialog(
                this@MainActivity,
                viewModel.getBirthYears(),
                "Năm sinh"
            ) { selectedYear ->
                binding.tvDateOfBirth.text = selectedYear
                viewModel.onDateChanged(selectedYear.toInt())
            }

        }

        binding.tvAddress.setOnClickListener {
            DialogHandler.createSingleItemDialog(
                this@MainActivity,
                viewModel.getAddress(),
                "Quê quán"
            ) { selectedAddress ->
                binding.tvAddress.text = selectedAddress
                viewModel.onAddressChanged(selectedAddress)
            }

        }


        lifecycleScope.launch {
            viewModel.employees.collect { employees ->
                employeeAdapter.submitList(employees)
            }
        }
        binding.edtSearchName.addTextChangedListener { text ->
            viewModel.onSearchQueryChanged(text.toString())
        }
        onClickAddEmployee()
        employeeAdapter.onItemClick= { position ->
            showDeleteConfirmationDialog(position)
        }
    }

    private fun onClickAddEmployee() {
        binding.btnAddEmployee.setOnClickListener {
            val dialog = AddEmployeeDialogFragment()
            dialog.show(supportFragmentManager, "AddEmployeeDialog")
        }
    }

    private fun showDeleteConfirmationDialog(position:Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Xác nhận xóa")
        builder.setMessage("Bạn có chắc chắn muốn xóa không?")

        builder.setPositiveButton("Xóa") { dialog, _ ->
            viewModel.deleteEmployee(position)
            dialog.dismiss()
        }

        builder.setNegativeButton("Hủy") { dialog, _ ->
            // Xử lý hủy
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

}