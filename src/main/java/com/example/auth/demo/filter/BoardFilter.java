package com.example.auth.demo.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//
//@WebFilter(urlPatterns = "/api/v1/boardroom/*")
//public class BoardFilter extends OncePerRequestFilter {
//    @Override
//    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//
//        System.out.println("123");
//        filterChain.doFilter(httpServletRequest,httpServletResponse);
//    }
//}
