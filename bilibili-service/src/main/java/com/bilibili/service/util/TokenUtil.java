package com.bilibili.service.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bilibili.domain.exception.ConditionException;

import java.util.Calendar;
import java.util.Date;

public class TokenUtil {

    private static final String ISSUER = "charles";

    public static String generateToken(Long userId) throws Exception{
        // use RSA256 to encrypt
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
        // it can last 30 seconds
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, 500);
        return JWT.create().withKeyId(String.valueOf(userId))
                .withIssuer(ISSUER)
                .withExpiresAt(calendar.getTime())
                .sign(algorithm);
    }

    public static Long verifyToken(String token) {
        try{
            // decode the token using jwt verifier
            Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String userId = jwt.getKeyId();
            return Long.valueOf(userId);
        } catch (TokenExpiredException e) {
            throw new ConditionException("555", "Token Expired!");
        } catch (Exception e) {
            throw new ConditionException("Invalid Token");
        }


    }
}
