package com.ros.lmsdesktopclient.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class TokenHandler {

    private static final String TOKEN_KEY = "token";
    private static final String AUTHORITIES_KEY = "authorities";
    private final Preferences preferences;
    private final RSAPublicKey publicKey;

    private TokenHandler() {
        // Initialize preferences for this class's package
        this.preferences = Preferences.userNodeForPackage(TokenHandler.class);
        this.publicKey = getPublicKey();
    }

    private static class SingletonHelper{
        private static final TokenHandler _tokenHandler = new TokenHandler();
    }

    public static TokenHandler getInstance(){
        return SingletonHelper._tokenHandler;
    }

    // Helper method to load an RSA public key from a string
    private RSAPublicKey getPublicKey() {
        try {
            String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" +
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtqOsQUo9mP1adcnRu0Id\n" +
                    "nIL3OyP4CsKKV4yVKy6haa+C53Sfsm87F40o/RuotShe7s/yj2dIWjflyxKbYLzP\n" +
                    "rnIfr+TivuIlQA0DKODNl3oR8tos0raHqOfuJmozBn/EZgZUFcdLXfbR0xhM0qIE\n" +
                    "JqtEzBgNusf2WXPzAOJeBzeN31jzfU9yZW1yMUFxkzQ7WfVT1+lYikr2ctaqZ89t\n" +
                    "wgCEVKiXzkSeJPK1w+FosVodI2gO1MymOK/VHiAL3g35nzrlmkUPX9YLeAnnosdY\n" +
                    "5QYTQdqqKD/Pab0jxE7PM2V/HzK6Fglx65pZoURjnL6KvwTns4g45+5m4MyUjGFe\n" +
                    "3wIDAQAB\n" +
                    "-----END PUBLIC KEY-----\n";

            publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);

            return (RSAPublicKey) keyFactory.generatePublic(keySpec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }


    }

    private String extractAuthoritiesFromToken(String token) {

        try {
            // Create the JWT verifier using the public key
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm).build();

            // Decode and verify the token
            DecodedJWT decodedJWT = verifier.verify(token);

            // Extract the "authorities" claim
            return decodedJWT.getClaim(AUTHORITIES_KEY).asString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to decode token and extract authorities", e);
        }
    }

    private Set<String> getAuthorities(String strAuthorities){
        return Arrays.stream(strAuthorities.split("\\s+")) // Split by whitespace (one or more spaces)
                .collect(Collectors.toSet());
    }

    // Save the token to Preferences
    public void saveToken(String token) {
        preferences.put(TOKEN_KEY, token);
        preferences.put(AUTHORITIES_KEY, extractAuthoritiesFromToken(token));
    }

    // Retrieve the token from Preferences
    public Optional<String> getToken() {
        return Optional.ofNullable(preferences.get(TOKEN_KEY, null));
    }

    // Retrieve authorities from Preferences
    public Set<String> getAuthorities() {
        return getAuthorities(preferences.get(AUTHORITIES_KEY, null));
    }

    // Clear all saved data
    public void clear() {
        preferences.remove(TOKEN_KEY);
        preferences.remove(AUTHORITIES_KEY);
    }

}
