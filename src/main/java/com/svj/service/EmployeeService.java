package com.svj.service;

import com.svj.entity.Employee;
import com.svj.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository= employeeRepository;
    }

    public Employee createNewEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    public List<Employee> getEmployees(){
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Integer id){
        return employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("Employee not found with id"+id));
    }
}
