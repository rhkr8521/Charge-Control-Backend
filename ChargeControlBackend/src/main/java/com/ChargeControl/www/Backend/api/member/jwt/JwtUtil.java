package com.ChargeControl.www.Backend.api.member.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    public static String getUserEmail(String token, String secretKey){
        String res = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("userEmail", String.class);
        System.out.println("res : " + res);
        return res;
    }

    public static boolean isExpired(String token, String secretKey){
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());
    }

    public static String createJwt(String username, String secretKey, Long expiredMs){
        Claims claims = Jwts.claims();
        claims.put("userName", username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

    }
}

