package com.svj.service;

import com.svj.entity.Employee;
import com.svj.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeUserDetailsService implements UserDetailsService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> employee= employeeRepository.findByUserName(username);
        return employee
                .map(EmployeeUserDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException(username+" not found in system"));
    }
}
