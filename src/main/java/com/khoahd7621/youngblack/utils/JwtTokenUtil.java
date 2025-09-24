package com.khoahd7621.youngblack.utils;

import com.khoahd7621.youngblack.entities.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;
    @Value("${JWT_ACCESS_TOKEN_EXPIRED_DATE}")
    private long accessTokenExpiredDate;
    @Value("${JWT_REFRESH_TOKEN_EXPIRED_DATE}")
    private long refreshTokenExpiredDate;

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("name", "ACCESS_TOKEN")
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiredDate * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("name", "REFRESH_TOKEN")
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiredDate * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public boolean validateTokenHeader(JwsHeader header) {
        return header.getType().equals("JWT") &&
                header.get("name").equals("ACCESS_TOKEN") &&
                header.getAlgorithm().equals("HS256");
    }

    public Jws<Claims> getJwsClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }

    public long getUserIdFromClaims(Claims claims) {
        return Long.parseLong(claims.get("userId").toString());
    }

}
