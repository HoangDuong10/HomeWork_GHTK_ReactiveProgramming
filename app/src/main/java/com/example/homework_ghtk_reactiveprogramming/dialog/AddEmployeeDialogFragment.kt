package com.example.homework_ghtk_reactiveprogramming.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.homework_ghtk_reactiveprogramming.databinding.DialogAddEmployeeBinding
import com.example.homework_ghtk_reactiveprogramming.model.Employee
import com.example.homework_ghtk_reactiveprogramming.viewmodel.SearchViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddEmployeeDialogFragment : DialogFragment() {

    private lateinit var binding :DialogAddEmployeeBinding
    private lateinit var viewModel: SearchViewModel
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddEmployeeBinding.inflate(LayoutInflater.from(requireContext()))
        val view = binding.root
        viewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
        binding.btnAddEmployee.setOnClickListener {
            val name = binding.edtName.text.toString()
            val dateOfBirthStr = binding.edtDateOfBirth.text.toString()
            val address = binding.edtAddress.text.toString()

            val dateOfBirth: Date? = try {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateOfBirthStr)
            } catch (e: ParseException) {
                null
            }
            if (name.isNotBlank() && dateOfBirth != null && address.isNotBlank()) {
                val newEmployee = Employee(name, dateOfBirth, address)
                viewModel.addEmployee(newEmployee)
                dismiss()
            } else {
                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin và kiểm tra định dạng ngày tháng năm", Toast.LENGTH_LONG).show()
            }
        }
        val dialog = Dialog(requireContext())
        dialog.setContentView(view)
        ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return dialog
    }

}