package com.example.library_management.auth;

import com.example.library_management.entity.User;
import com.example.library_management.repo.UserRepo;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("JWT Filter triggered for URI: {}", request.getRequestURI());

        Claims claims;
        try {
            claims = jwtUtil.resolveClaims(request);
            log.debug("Claims extracted: {}", claims);
        } catch (Exception ex) {
            log.warn("Token invalid or missing: {}", ex.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (claims != null && jwtUtil.validateClaims(claims)) {
            String username = jwtUtil.getUsername(claims);
            log.debug("Extracted username: {}", username);

            User user = userRepo.findByUsername(username).orElse(null);
            if (user == null) {
                log.warn("No user found with username: {}", username);
            } else {
                log.info("User authenticated: {} with role {}", user.getUsername(), user.getRole().getName());

                var authorities = List.of(new SimpleGrantedAuthority(user.getRole().getName()));
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.debug("Authentication set with authorities: {}", authorities);
            }
        } else {
            log.warn("Claims are invalid or expired.");
        }

        filterChain.doFilter(request, response);
    }
}
