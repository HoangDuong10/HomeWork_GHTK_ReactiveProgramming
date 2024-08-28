package com.example.homework_ghtk_reactiveprogramming.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework_ghtk_reactiveprogramming.model.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class SearchViewModel  : ViewModel() {
    private var listEmployee : MutableList<Employee> = mutableListOf()
    private val _employees = MutableStateFlow<List<Employee>>(listOf())
    val employees: StateFlow<List<Employee>> get() = _employees
    private val _selectedBirthYear = MutableStateFlow<Int?>(null)
    private val _selectedAddress = MutableStateFlow("")
    private val _searchQuery = MutableStateFlow("")
    init {
        viewModelScope.launch {
            combine(
                _searchQuery.debounce(1000),
                _selectedBirthYear,
                _selectedAddress,
                getListEmployee()
            ) { query, selectedBirthYear, selectedAddress, employees ->
                employees.filter { employee ->
                    val matchesName = query.isBlank() || employee.name.contains(query, ignoreCase = true)
                    val matchesBirthYear = selectedBirthYear == null ||
                            Calendar.getInstance().apply {
                                time = employee.dateOfBirth
                            }.get(Calendar.YEAR) == selectedBirthYear
                    val matchesAddress = selectedAddress.isBlank() ||
                            employee.address.contains(selectedAddress, ignoreCase = true)
                    matchesName && matchesBirthYear && matchesAddress
                }.distinctBy { employee ->
                    employee.name to employee.dateOfBirth to employee.address
                }
            }.collect { filteredEmployees ->
                _employees.value = filteredEmployees
            }
        }

    }
    fun addEmployee(newEmployee: Employee) {
        viewModelScope.launch {
            listEmployee.add(newEmployee)
            val updatedList = _employees.value.toMutableList()
            updatedList.add(newEmployee)
            _employees.value = updatedList
        }
    }
    fun deleteEmployee(position:Int) {
        viewModelScope.launch {
            listEmployee.removeAt(position)
            val updatedList = _employees.value.toMutableList()
            updatedList.removeAt(position)
            _employees.value = updatedList
        }
    }
      private fun getListEmployee(): Flow<List<Employee>> {
       return flow{
           val dateFormat = SimpleDateFormat("dd/MM/yyyy")
           listEmployee.add(Employee("Hoàng Xuân Dương", dateFormat.parse("04/04/2002"), "Hưng Yên"))
           listEmployee.add(Employee("Hoàng Hải Yến", dateFormat.parse("22/05/2008"), "Nam Định"))
           listEmployee.add(Employee("Nguyễn Văn Hùng", dateFormat.parse("07/07/2004"), "Hưng Yên"))
           listEmployee.add(Employee("Hoàng Văn Triển", dateFormat.parse("12/03/2004"), "Thanh Hóa"))
           listEmployee.add(Employee("Hoàng Thế Hào", dateFormat.parse("27/02/2003"), "Nghệ An"))
           listEmployee.add(Employee("Nguyễn Hương Giang", dateFormat.parse("25/05/2006"), "Hải Dương"))
           listEmployee.add(Employee("Nguyễn Thị Hà", dateFormat.parse("04/06/2006"), "Nghệ An"))
           listEmployee.add(Employee("Trần Thị Trang", dateFormat.parse("14/08/2008"), "Hưng Yên"))
           listEmployee.add(Employee("Trần Thị Hoa", dateFormat.parse("18/03/1999"), "Hà Nội"))
           listEmployee.add(Employee("Hoàng Vn Thức", dateFormat.parse("16/02/2005"), "Hải Dương"))
           listEmployee.add(Employee("Nguyễn Xuân Dũng", dateFormat.parse("21/07/2004"), "Hà Nội"))
           listEmployee.add(Employee("Nguyễn Xuân Dương", dateFormat.parse("24/09/2003"), "Thanh Hóa"))
           listEmployee.add(Employee("Trần Thị Hải Yến", dateFormat.parse("27/11/2001"), "Hưng Yên"))
           listEmployee.add(Employee("Hoàng Văn Tuấn Anh", dateFormat.parse("23/12/2008"), "Nghệ An"))
           listEmployee.add(Employee("Hoàng Văn Hào", dateFormat.parse("22/10/2000"), "Bình Phước"))
           emit(listEmployee)
       }
    }

    fun getAddress(): List<String> {
        return listEmployee
            .map { it.address }
            .distinct()
    }

    fun getBirthYears(): List<Int> {
        return listEmployee
            .map { Calendar.getInstance().apply { time = it.dateOfBirth }.get(Calendar.YEAR) } // Lấy năm từ ngày sinh
            .distinct()
            .sorted()
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }
    fun onAddressChanged(newQuery: String) {
        _selectedAddress.value = newQuery
    }
    fun onDateChanged(newQuery: Int) {
        _selectedBirthYear.value = newQuery
    }
}