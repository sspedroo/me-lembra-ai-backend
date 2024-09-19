package org.remind.melembraai.infra.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.remind.melembraai.utils.BRDatesUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final long TOKEN_VALIDITY = Duration.ofMinutes(1).toMillis();
    private static final long REFRESH_TOKEN_VALIDITY = Duration.ofMinutes(15).toMillis();

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrai uma reivindicação específica do token JWT fornecido usando a função de resolução de reivindicações fornecida.
     *
     * @param <T> O tipo da reivindicação a ser extraída.
     * @param token O token JWT do qual a reivindicação será extraída.
     * @param claimsResolvers Uma função que recebe Claims como entrada e retorna a reivindicação desejada do tipo T.
     * @return A reivindicação extraída do tipo T.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isRefreshToken(String token) {
        return extractClaim(token, claims -> (boolean) claims.getOrDefault("refresh_token", false));
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String generateToken(UserDetails userDetails) {
        final Instant now = Instant.now(Clock.system(BRDatesUtils.getBrazilZoneId()));
        final Instant tokenExpiration = now.plusMillis(TOKEN_VALIDITY);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(getDefaultClaims(userDetails))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(tokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        final Instant now = Instant.now(Clock.system(BRDatesUtils.getBrazilZoneId()));
        final Instant tokenExpiration = now.plusMillis(REFRESH_TOKEN_VALIDITY);

        return Jwts.builder()
                .setHeaderParam("typ",  "JWT")
                .setClaims(Map.of("refresh_token", true))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(tokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Map<String, Object> getDefaultClaims(UserDetails userDetails){
        return Map.of(
                "user.email", userDetails.getUsername(),
                "user.roles", AuthorityUtils.authorityListToSet(userDetails.getAuthorities())
        );
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
