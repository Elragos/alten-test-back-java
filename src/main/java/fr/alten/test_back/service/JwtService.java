package fr.alten.test_back.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * JWT token service.
 *
 * @author Amarechal
 */
@Service
public class JwtService {

    /**
     * Security key.
     */
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    /**
     * Configured expiration time (in milliseconds).
     */
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Extract user name from token.
     *
     * @param token User JWT token.
     * @return Token user name.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract field from JWT token.
     *
     * @param <T> Field data type.
     * @param token User JWT token.
     * @param claimsResolver Token resolver.
     * @return Found field.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generate jwt token.
     *
     * @param userDetails User info.
     * @return Generated JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generate jwt token.
     *
     * @param extraClaims Token extra info.
     * @param userDetails User info.
     * @return Generated JWT token.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Get token expiration time (in milliseconds).
     *
     * @return Token expiration time (in milliseconds).
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Build JWT token.
     *
     * @param extraClaims Token extra info.
     * @param userDetails User info.
     * @param expiration Token expiration time (in milliseconds).
     * @return Generated JWT token.
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        // Create new token builder
        return Jwts.builder()
            // Set extra info
            .setClaims(extraClaims)
            // Set user info
            .setSubject(userDetails.getUsername())
            // Set token creation date
            .setIssuedAt(new Date(System.currentTimeMillis()))
            // Set token expiration time
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            // Set token signature key
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            // Generate token
            .compact();
    }

    /**
     * Validate token.
     *
     * @param token Tested JWT token.
     * @param userDetails Compared User details.
     * @return <code>true</code> if token valid, <code>false</code> otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        // Get token user name
        final String username = extractUsername(token);
        // Compare it with user info, and check if token not expired
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Check if token is expired.
     *
     * @param token Test JWT token.
     * @return <code>true</code> if token expired, <code>false</code> otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extract expiration date from token.
     *
     * @param token JWT token
     * @return Token expiration date.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract all inforamtion from token
     *
     * @param token JWT token.
     * @return Token information.
     */
    private Claims extractAllClaims(String token) {
        // Create JWT parser
        return Jwts.parserBuilder()
            // Set used signature key
            .setSigningKey(getSignInKey())
            // Generate parser
            .build()
            // Parse token inforamtion
            .parseClaimsJws(token)
            // Get token content
            .getBody();
    }

    /**
     * Get token signature key.
     *
     * @return Token signature key.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
