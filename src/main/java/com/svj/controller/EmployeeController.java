package com.svj.controller;

import com.svj.entity.Employee;
import com.svj.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    public static final String AUTHORITY_ROLE_HR = "hasAuthority('ROLE_HR')";
    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService){
        this.employeeService= employeeService;
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to the portal! Your credentials have been shared over the mail";
    }

    @PostMapping("/create")
    public Employee onboardNewEmployee(@RequestBody Employee employee){
        return employeeService.createNewEmployee(employee);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_HR') or hasAuthority('ROLE_MANAGER')")
    public List<Employee> getAll(){
        return employeeService.getEmployees();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    public Employee getEmployeeById(@PathVariable Integer id){
        return employeeService.getEmployee(id);
    }

    @PutMapping("/update")
    @PreAuthorize(AUTHORITY_ROLE_HR)
    public Employee updateRoles(@RequestBody Employee employee){
        return employeeService.changeEmployeeRole(employee);
    }
}
