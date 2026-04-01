package employeeEnrollment.security;

import java.util.Date;
import java.security.Key;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final String SECRET = "mysecretkeymysecretkeymysecretkey"; // min 32 chars

    // ✅ FIXED METHOD
    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // 🔹 Generate Token
    public String generateToken(String username, String role) {

        // Ensure no ROLE_ prefix stored in token
        String normalizedRole = role.replace("ROLE_", "");

        return Jwts.builder()
                .setSubject(username)
                .claim("role", normalizedRole)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getKey())
                .compact();
    }

    // 🔹 Extract Username
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 🔹 Validate Token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}