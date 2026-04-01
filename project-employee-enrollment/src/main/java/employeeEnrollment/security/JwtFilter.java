package employeeEnrollment.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService service;

    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService service) {
        this.jwtUtil = jwtUtil;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            try {
                String username = jwtUtil.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    if (jwtUtil.validateToken(token)) {

                        String role = jwtUtil.extractRole(token);

                        if (role != null) {

                            // Ensure consistent ROLE_ prefix
                            String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;

                            UsernamePasswordAuthenticationToken auth =
                                    new UsernamePasswordAuthenticationToken(
                                            username,
                                            null,
                                            List.of(new SimpleGrantedAuthority(authority))
                                    );

                            SecurityContextHolder.getContext().setAuthentication(auth);
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println("JWT Filter Error: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}