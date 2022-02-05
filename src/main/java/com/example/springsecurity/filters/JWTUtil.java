package com.example.springsecurity.filters;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JWTUtil {
	private final String SECRET_KEY;

	private final JWTDataSource jwtDataSource;

	public JWTUtil(@Autowired JWTDataSource jwtDataSource) {
		this.jwtDataSource = jwtDataSource;
		this.SECRET_KEY = jwtDataSource.getSecretKey();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * jwtDataSource.getExpirationDate()))
			.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		log.info("in validate token: username -> "  + username);
		log.info("is token expired? " + isTokenExpired(token));
		log.info("userDetails: " + userDetails.getUsername());
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
}