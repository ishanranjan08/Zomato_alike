package com.first.foodo.first_foodo.Security;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import org.springframework.stereotype.Service;

import static java.security.KeyRep.Type.SECRET;

@Service
public class JwtService {

    private static final long EXPIRATION_TIME = 15 * 60 * 1000;//for access token --> 15 minutes


    private static final long EXPIRATION_REFRESH_TIME = 24 * 60 * 60 * 1000; // for refresh token --> 24 hours

//   Use a strong secret. In production store it in environment or vault (NOT hard-coded).
    private final String SECRET = "replace_with_at_least_32_byte_long_secret_key_123456";






    // Generate JWT token for username
    public String generateToken(String username, boolean isAccessToken) {

        long expTime= isAccessToken?EXPIRATION_TIME : EXPIRATION_REFRESH_TIME;

        long now = System.currentTimeMillis();

        String tokenType= isAccessToken?"access_token":"refresh_Token";

        Map<String,Object> claim=new HashMap<>();
        claim.put("typ",tokenType);

        return Jwts.builder()
                .claims(claim)
                .setSubject(username) // subject (username)
                .setIssuedAt(new Date(now)) // issued at
                .setExpiration(new Date(now + expTime)) // expires in 1 hour
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256) // sign using the SecretKey and algorithm
                .compact(); // build token string
    }





    // Extract username (subject) from token
    public String getUserName(String token) {
        String username = Jwts.parser()
                .setSigningKey(SECRET.getBytes()).build()
                .parseClaimsJws(token).getBody().getSubject();
        return username;
    }




    // Check if token is expired
    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(SECRET.getBytes()).build()
                .parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }

    public boolean isAccessToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET.getBytes()).build()
                .parseClaimsJws(token).getBody();
        String tokenType = (String)claims.get("typ");

        return tokenType.equals("access_token");

    }

    public boolean isRefreshToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET.getBytes()).build()
                .parseClaimsJws(token).getBody();
        String tokenType = (String)claims.get("typ");

        return tokenType.equals("refresh_Token");

    }



    // Validate token for a given username
    public boolean validateToken(String token) {
        if (this.isTokenExpired(token)) {
            return false;
        }
        try {
            Jwts.parser().setSigningKey(SECRET.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        }
    }

}
