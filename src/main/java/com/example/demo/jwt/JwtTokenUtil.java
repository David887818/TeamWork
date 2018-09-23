package com.example.demo.jwt;

import com.example.demo.service.CurrentUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken (token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken (token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken (token, Claims::getExpiration);
    }

    public String getAudienceFromToken(String token) {
        return getClaimFromToken (token, Claims::getAudience);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken (token);
        return claimsResolver.apply (claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser ()
                .setSigningKey (secret)
                .parseClaimsJws (token)
                .getBody ();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken (token);
        return expiration.before (new Date ());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before (lastPasswordReset));
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<> ();
        return doGenerateToken (claims, userDetails.getUsername ());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date ();
        final Date expirationDate = calculateExpirationDate (createdDate);

        System.out.println ("doGenerateToken " + createdDate);

        return Jwts.builder ()
                .setClaims (claims)
                .setSubject (subject)
                .setIssuedAt (createdDate)
                .setExpiration (expirationDate)
                .signWith (SignatureAlgorithm.HS512, secret)
                .compact ();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getIssuedAtDateFromToken (token);
        return !isCreatedBeforeLastPasswordReset (created, lastPasswordReset)
                && (!isTokenExpired (token));
    }

    public String refreshToken(String token) {
        final Date createdDate = new Date ();
        final Date expirationDate = calculateExpirationDate (createdDate);

        final Claims claims = getAllClaimsFromToken (token);
        claims.setIssuedAt (createdDate);
        claims.setExpiration (expirationDate);

        return Jwts.builder ()
                .setClaims (claims)
                .signWith (SignatureAlgorithm.HS512, secret)
                .compact ();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        CurrentUser user = (CurrentUser) userDetails;
        final String username = getUsernameFromToken (token);
        final Date created = getIssuedAtDateFromToken (token);
        //final Date expiration = getExpirationDateFromToken(token);
        return (
                username.equals (user.getUsername ())
                        && !isTokenExpired (token));
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date (createdDate.getTime () + expiration * 1000);
    }
}
