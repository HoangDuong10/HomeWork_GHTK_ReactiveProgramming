package com.example.homework_ghtk_reactiveprogramming


import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homework_ghtk_reactiveprogramming.adapter.EmployeeAdapter
import com.example.homework_ghtk_reactiveprogramming.databinding.ActivityMainBinding
import com.example.homework_ghtk_reactiveprogramming.dialog.AddEmployeeDialogFragment
import com.example.homework_ghtk_reactiveprogramming.viewmodel.SearchViewModel
import com.example.homework_ghtk_reactiveprogramming.dialog.DialogHandler
import com.example.homework_ghtk_reactiveprogramming.model.Employee
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

        lifecycleScope.launch {
            viewModel.employees
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .onEach { employees ->
                    employeeAdapter.submitList(employees)
                    showData(employees)
                }
                .launchIn(this)
        }
        employeeAdapter.onItemClick= { position ->
            showDeleteConfirmationDialog(position)
        }
        onClickListener()
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
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
    private fun onClickListener(){
        binding.tvDateOfBirth.setOnClickListener {
            DialogHandler.createSingleItemDialog(
                this@MainActivity,
                viewModel.getBirthYears()!!,
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

        binding.edtSearchName.addTextChangedListener { text ->
            viewModel.onSearchQueryChanged(text.toString())
        }

        binding.btnAddEmployee.setOnClickListener {
            val dialog = AddEmployeeDialogFragment()
            dialog.show(supportFragmentManager, "AddEmployeeDialog")
        }

        binding.btnAddEmployee.setOnClickListener {
            val dialog = AddEmployeeDialogFragment()
            dialog.show(supportFragmentManager, "AddEmployeeDialog")
        }
    }
    private fun showData(listEmployee : List<Employee>){
        if (listEmployee.isEmpty()){
            binding.tvInfo.visibility = View.VISIBLE
        }else{
            binding.tvInfo.visibility = View.GONE
        }
    }

}