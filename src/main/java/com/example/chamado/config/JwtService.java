package com.example.chamado.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.chamado.user.model.Role;
import com.example.chamado.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET = "segreto-super-seguro";
    private static final long EXPIRATION = 1000 * 60 * 60;

    private Algorithm getSignAlgorithm() {
        return Algorithm.HMAC256(SECRET);
    }

    public String generateToken(User user) {

        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("id", user.getId())
                .withClaim("nome", user.getNome())
                .withClaim("role", user.getRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .sign(getSignAlgorithm());
    }

    public String extractEmail(String token) {
        try {
            return JWT.require(getSignAlgorithm())
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }


    public Role extractRole(String token) {
        try {
            String roleString = JWT.require(getSignAlgorithm())
                    .build()
                    .verify(token)
                    .getClaim("role")
                    .asString();

            return Role.valueOf(roleString);
        } catch (Exception e) {
            return null;
        }
    }

    private Date extractExpiration(String token) {
        try {
            return JWT.require(getSignAlgorithm())
                    .build()
                    .verify(token)
                    .getExpiresAt();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {

        String email = extractEmail(token);

        if (email == null) return false;
        if (!email.equals(userDetails.getUsername())) return false;

        return !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);

        if (expiration == null) return true;

        return expiration.before(new Date());
    }
}
