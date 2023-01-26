package com.svj.config;

import com.svj.service.EmployeeUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class EMSUpgradeSecurityConfig {

//    EmployeeUserDetailsService employeeUserDetailsService;
//
//    EMSUpgradeSecurityConfig(EmployeeUserDetailsService employeeUserDetailsService){
//        this.employeeUserDetailsService= employeeUserDetailsService;
//    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder){
//        UserDetails employee= User.withUsername("Swaraj")
//                .password(passwordEncoder.encode("Pwd1"))
//                .roles("EMPLOYEE").build();
//
//        UserDetails hr= User.withUsername("Ram")
//                .password(passwordEncoder.encode("Pwd2"))
//                .roles("HR").build();
//
//        UserDetails admin= User.withUsername("Amit")
//                .password(passwordEncoder.encode("Pwd3"))
//                .roles("MANAGER", "HR").build();
//
//        return new InMemoryUserDetailsManager(employee, admin, hr);
        return new EmployeeUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.authorizeRequests()
//                .antMatchers("/nonSecure").permitAll()
//                .and()
//                .authorizeRequests().antMatchers("/home", "/text")
//                .authenticated().and().httpBasic().and().build();
        return http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/employees/welcome", "/employees/create").permitAll()
                .and()
                .authorizeRequests().antMatchers("/employees/**")
                .authenticated().and().httpBasic().and().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
