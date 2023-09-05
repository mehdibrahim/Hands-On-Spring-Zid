package com.zid.zid.security.utils;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.zid.zid.security.services.UserDetailsImpl;

import io.jsonwebtoken.*;

import java.util.*;
import java.util.function.Function;






@Service
public class JwtUtils {

    private static final String SECRET_KEY ="simple";


    public String generateToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		return Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() +1000*60*60*10))
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
				.compact();
	}
    /*
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims,userDetails.getUsername());
    }
   
    private String createToken(Map<String,Object>claims,String subject){
        Date now =new Date(System.currentTimeMillis());
        Date until=new Date(System.currentTimeMillis()+1000*60*60*10);
        String token=Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(now).setExpiration(until)
        .signWith(SignatureAlgorithm.HS256,SECRET_KEY).compact();
        return token;
    }*/
    public boolean validateToken(String token,UserDetails userDetails){
        String userName =extractUsername(token);
        return userName.equals(userDetails.getUsername())&& !isTokenExpired(token);
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims=extractAllClaims(token);
    return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }
    public Date extractExpiration(String token){
        return  extractClaim(token,Claims::getExpiration);
    }
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
}