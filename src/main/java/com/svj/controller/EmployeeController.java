package com.svj.controller;

import com.svj.dto.AuthRequest;
import com.svj.entity.Employee;
import com.svj.service.EmployeeService;
import com.svj.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    public static final String AUTHORITY_ROLE_HR = "hasAuthority('ROLE_HR')";
    private EmployeeService employeeService;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    public EmployeeController(EmployeeService employeeService, JwtService jwtService, AuthenticationManager authenticationManager){
        this.employeeService= employeeService;
        this.jwtService= jwtService;
        this.authenticationManager= authenticationManager;
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

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody AuthRequest authRequest){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        if(authenticate.isAuthenticated())
            return jwtService.generateToken(authRequest.getUserName());
        else
            throw new UsernameNotFoundException("Authentication failed!");
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
