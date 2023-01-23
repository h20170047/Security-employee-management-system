package com.svj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class EMSUpgradeSecurityConfig {
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder){
        UserDetails user= User.withUsername("Swaraj")
                .password(passwordEncoder.encode("Pwd1"))
                .roles("USER").build();

        UserDetails admin= User.withUsername("Ram")
                .password(passwordEncoder.encode("Pwd2"))
                .roles("ADMIN").build();

        UserDetails userAdmin= User.withUsername("Amit")
                .password(passwordEncoder.encode("Pwd3"))
                .roles("USER", "ADMIN").build();

        return new InMemoryUserDetailsManager(user, admin, userAdmin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests()
                .antMatchers("/nonSecure").permitAll()
                .and()
                .authorizeRequests().antMatchers("/home", "/text")
                .authenticated().and().httpBasic().and().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
