package com.healthinsurance.Health.hepler;

import static org.mockito.ArgumentMatchers.nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.oauth2.login.UserInfoEndpointDsl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

//    private static final List<String> EXCLUDE_URLS = List.of(
//            "/swagger-ui/**", "/swagger-ui.html", 
//            "/v3/api-docs/**", "/swagger-resources/**", 
//            "/webjars/**", "/users/register", "/users/login"
//    );

//    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

//    private boolean isExcluded(String path) {
//        return EXCLUDE_URLS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

//        String path = request.getServletPath(); 

        
//        if (isExcluded(path)) {
//            chain.doFilter(request, response);
//            return; 
//        }

        String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        Integer userId = null;
        String email = null;
        String role = null;
        String jwt = null;
      


        System.err.println("token >>> "+authorizationHeader);
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);   
                userId = jwtUtil.extractUserId(jwt);
                email = jwtUtil.extractEmail(jwt);
                role = jwtUtil.extractRole(jwt);
                
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token expired\"}");
                return;  
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid token\"}"); 
                return; 
            }
        }
 
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.err.println("userDetails >>> "+userDetails); 
            System.err.println("true >>> "+jwtUtil.validateToken(jwt, userId, username,email, role)); 
            if (jwtUtil.validateToken(jwt,userId ,username,email, role)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }
}
