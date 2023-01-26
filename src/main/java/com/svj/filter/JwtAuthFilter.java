package com.svj.filter;

import com.svj.service.EmployeeUserDetails;
import com.svj.service.EmployeeUserDetailsService;
import com.svj.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private EmployeeUserDetailsService userDetailsService;
    public JwtAuthFilter(JwtService jwtService, EmployeeUserDetailsService userDetailsService){
        this.jwtService= jwtService;
        this.userDetailsService= userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader= request.getHeader("Authorization");
        String token= null;
        String userName= null;
        if(authHeader!= null && authHeader.startsWith("Bearer ")){
            token= authHeader.substring(7);
            userName= jwtService.extractUsername(token);
        }
        if(userName!= null && SecurityContextHolder.getContext().getAuthentication()== null){
            UserDetails userDetails= userDetailsService.loadUserByUsername(userName);
            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken=
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}
