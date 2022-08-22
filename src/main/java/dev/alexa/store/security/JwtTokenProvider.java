package dev.alexa.store.security;

import dev.alexa.store.exception.ApiException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;


    public String generateToken(Authentication authentication)
    {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpirationInMs);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        return token;
    }

    public String getUsernameFromToken(String token)
    {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token)
    {
            try{
                Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
                return true;
            }
            catch (SignatureException ex)
            {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid jwt signature");

            }catch (MalformedJwtException ex)
            {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid jwt token");
            }
            catch (ExpiredJwtException ex)
            {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Jwt token has expired");
            }
            catch (UnsupportedJwtException ex)
            {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Unsupported jwt token");
            }
            catch (IllegalArgumentException ex)
            {
                throw new ApiException(HttpStatus.BAD_REQUEST, "jwt claims string is empty ");
            }
    }



}
