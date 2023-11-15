package com.example.saqayatask.Configuration.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
//extracting claims and checking if a token is valid
public class JwtService {
    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    // 1 first we extract all claims
    private Claims extractJwtClaims(String jwt) {

        return Jwts.
                parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmail(String jwt) {
        String email = extractClaim(jwt, Claims::getSubject);
        return email;
    }

    // 2 second we apply claims solver function to extract the needed claim ex username / expiration..ect .
    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        Claims claims = extractJwtClaims(jwt);
        return claimsResolver.apply(claims);
    }

    public String generateJwt(Map<String, List<String>> claims, UserDetails userDetails) {
        try {
            String token = Jwts
                    .builder()
                    .setClaims(claims)// here in claims you can add whatever claims or credentials you want.{"Role":["user","manager"]}
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 1)))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception ex) {
            return ex.getMessage();
        }

    }

    public String generateRefreshJWT(Map<String, List<String>> claims, UserDetails userDetails) {
        try {
            String token = Jwts
                    .builder()
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 5)))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public boolean isJwtValid(String jwt, UserDetails userDetails) {
        final String email = extractEmail(jwt);
        return email.equals(userDetails.getUsername()) && !isJwtExpired(jwt);
    }

    private boolean isJwtExpired(String jwt) {
        //to make sure that the time now is less than (time yesterday+1 day after )
        return extractJwtExpiration(jwt).before(new Date());
    }

    private Date extractJwtExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }


}
