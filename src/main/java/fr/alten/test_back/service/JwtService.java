package fr.alten.test_back.service;

import fr.alten.test_back.helper.JwtToken;
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
    private final String secretKey;

    /**
     * Configured expiration time (in milliseconds).
     */
    private final long jwtExpiration;

    public JwtService(
        @Value("${security.jwt.secret-key}")
        String secretKey,
        @Value("${security.jwt.expiration-time}")
        long jwtExpiration
    ){
        this.secretKey = secretKey;
        this.jwtExpiration = jwtExpiration;
    }

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
     * @param <T>            Field data type.
     * @param token          User JWT token.
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
    public JwtToken generateToken(UserDetails userDetails) {
        return this.generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generate jwt token.
     *
     * @param extraClaims Token extra info.
     * @param userDetails User info.
     * @return Generated JWT token.
     */
    public JwtToken generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return this.buildToken(extraClaims, userDetails, this.jwtExpiration);
    }

    /**
     * Build JWT token.
     *
     * @param extraClaims Token extra info.
     * @param userDetails User info.
     * @param expiration  Token expiration time (in milliseconds).
     * @return Generated JWT token.
     */
    private JwtToken buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        // Create new token builder
        Date now = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        String tokenValue = Jwts.builder()
                // Set extra info
                .setClaims(extraClaims)
                // Set user info
                .setSubject(userDetails.getUsername())
                // Set token creation date
                .setIssuedAt(now)
                // Set token expiration time
                .setExpiration(expirationDate)
                // Set token signature key
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                // Generate token
                .compact();

        return new JwtToken()
                .setValue(tokenValue)
                .setExpirationDate(expirationDate);
    }

    /**
     * Validate token.
     *
     * @param token       Tested JWT token.
     * @param userDetails Compared User details.
     * @return <code>true</code> if token valid, <code>false</code> otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        // Get token user name
        final String username = this.extractUsername(token);
        // Compare it with user info, and check if token not expired
        return (username.equals(userDetails.getUsername())) && !this.isTokenExpired(token);
    }

    /**
     * Check if token is expired.
     *
     * @param token Test JWT token.
     * @return <code>true</code> if token expired, <code>false</code> otherwise.
     */
    private boolean isTokenExpired(String token) {
        return this.extractExpiration(token).before(new Date());
    }

    /**
     * Extract expiration date from token.
     *
     * @param token JWT token
     * @return Token expiration date.
     */
    private Date extractExpiration(String token) {
        return this.extractClaim(token, Claims::getExpiration);
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
                .setSigningKey(this.getSignInKey())
                // Generate parser
                .build()
                // Parse token information
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
