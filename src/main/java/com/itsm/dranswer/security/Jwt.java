package com.itsm.dranswer.security;

/*
 * @package : com.itsm.dranswer.security
 * @name : Jwt.java
 * @date : 2021-05-20 오후 4:46
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

public final class Jwt {

    public static final String COOKIE_NAME = "login_token";

    private final String issuer;

    private final String clientSecret;

    private final int expirySeconds;

    private final Algorithm algorithm;

    private final JWTVerifier jwtVerifier;

    public Jwt(String issuer, String clientSecret, int expirySeconds) {
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.expirySeconds = expirySeconds;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    public String create(Claims claims) {
        Date now = new Date();
        JWTCreator.Builder builder = JWT.create();
        builder.withIssuer(issuer);
        builder.withIssuedAt(now);
        if (expirySeconds > 0) {
            builder.withExpiresAt(new Date(now.getTime() + expirySeconds * 1_000L));
        }
        builder.withClaim("userKey", claims.userKey);
        builder.withClaim("name", claims.name);
        builder.withArrayClaim("roles", claims.roles);
        return builder.sign(algorithm);
    }

    public Claims verify(String token) throws JWTVerificationException {
        return new Claims(jwtVerifier.verify(token));
    }

    public String getIssuer() {
        return issuer;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public int getExpirySeconds() {
        return expirySeconds;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public JWTVerifier getJwtVerifier() {
        return jwtVerifier;
    }

    @Getter
    static public class Claims {
        Long userKey;
        String name;
        String[] roles;
        Date iat;
        Date exp;

        private Claims() {/*empty*/}

        Claims(DecodedJWT decodedJWT) {
            Claim userKey = decodedJWT.getClaim("userKey");
            if (!userKey.isNull()) {
                this.userKey = userKey.asLong();
            }
            Claim name = decodedJWT.getClaim("name");
            if (!name.isNull()) {
                this.name = name.asString();
            }
            Claim roles = decodedJWT.getClaim("roles");
            if (!roles.isNull()) {
                this.roles = roles.asArray(String.class);
            }
            this.iat = decodedJWT.getIssuedAt();
            this.exp = decodedJWT.getExpiresAt();
        }

        public static Claims of(long userKey, String name, String[] roles) {
            Claims claims = new Claims();
            claims.userKey = userKey;
            claims.name = name;
            claims.roles = roles;
            return claims;
        }

        long iat() {
            return iat != null ? iat.getTime() : -1;
        }

        long exp() {
            return exp != null ? exp.getTime() : -1;
        }

        void eraseIat() {
            iat = null;
        }

        void eraseExp() {
            exp = null;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("userKey", userKey)
                    .append("name", name)
                    .append("roles", Arrays.toString(roles))
                    .append("iat", iat)
                    .append("exp", exp)
                    .toString();
        }
    }

    public String getToken(HttpServletRequest req){
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;

        Cookie cookie = Arrays.stream(cookies)
                .filter(c -> Jwt.COOKIE_NAME.equals(c.getName()))
                .findFirst().orElse(null);

        if (cookie == null) return null;

        return cookie.getValue();
    }
}