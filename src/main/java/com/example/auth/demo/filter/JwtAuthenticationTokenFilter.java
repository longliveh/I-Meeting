package com.example.auth.demo.filter;

import com.example.auth.demo.entity.user.UserDetail;
import com.example.auth.demo.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * token校验
 *
 * @author: JoeTao
 * createAt: 2018/9/14
 */


//如果添加了@Component或@Configuration，又添加了@WebFilter（）
// 那么会初始化两次Filter，并且会过滤所有路径+自己指定的路径 ，便会出现对没有指定的URL也会进行过滤


@WebFilter(urlPatterns = "/api/v1/user/*")
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.tokenHead}")
    private String token_header;

    @Resource
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        Cookie cookie = getCookieByName(request, header);
        String auth_token;
        if (cookie == null) {
            auth_token = null;
        } else {
            auth_token = cookie.getValue();
        }
        final String auth_token_start = token_header;
        if (StringUtils.isNotEmpty(auth_token) && auth_token.startsWith(auth_token_start)) {
            auth_token = auth_token.substring(auth_token_start.length());
        } else {
            // 不按规范,不允许通过验证
            auth_token = null;
        }

        String username = jwtUtils.getUsernameFromToken(auth_token);
        long user_id = jwtUtils.getUserIdFromToken(auth_token);
        request.setAttribute("user_id", user_id);

        //logger.info(String.format("Checking authentication for userDetail %s.", username));

        if (
            //jwtUtils.containToken(username, auth_token) &&
                username != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetail userDetail = jwtUtils.getUserFromToken(auth_token);
            if (jwtUtils.validateToken(auth_token, userDetail)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //logger.info(String.format("Authenticated userDetail %s, setting security context", username));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
//        System.out.println(request.getRequestURI());
//        BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
//        StringBuilder responseStrBuilder = new StringBuilder();
//        String inputStr;
//        while ((inputStr = streamReader.readLine()) != null)
//            responseStrBuilder.append(inputStr);
//        System.out.println(responseStrBuilder.toString());
        chain.doFilter(request, response);
    }


    private Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie;
        } else {
            return null;
        }
    }

    /**
     * 将cookie封装到Map里面
     *
     * @param request
     * @return
     */
    private Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}
