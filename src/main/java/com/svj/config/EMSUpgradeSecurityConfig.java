package com.svj.config;

import com.svj.filter.JwtAuthFilter;
import com.svj.service.EmployeeUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class EMSUpgradeSecurityConfig {

    private JwtAuthFilter jwtAuthFilter;

    public EMSUpgradeSecurityConfig(JwtAuthFilter jwtAuthFilter){
        this.jwtAuthFilter= jwtAuthFilter;
    }

//    EmployeeUserDetailsService employeeUserDetailsService;
//
//    EMSUpgradeSecurityConfig(EmployeeUserDetailsService employeeUserDetailsService){
//        this.employeeUserDetailsService= employeeUserDetailsService;
//    }

    // authentication
    @Bean
    public UserDetailsService userDetailsService(){
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

    // authorization
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.authorizeRequests()
//                .antMatchers("/nonSecure").permitAll()
//                .and()
//                .authorizeRequests().antMatchers("/home", "/text")
//                .authenticated().and().httpBasic().and().build();
        return http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/employees/welcome", "/employees/create", "/employees/authenticate").permitAll()
                .and()
                .authorizeRequests().antMatchers("/employees/**")
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}
