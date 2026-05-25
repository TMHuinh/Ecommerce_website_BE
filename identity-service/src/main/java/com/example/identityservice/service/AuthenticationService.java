package com.example.identityservice.service;

import com.example.identityservice.dto.request.AuthenticationRequest;
import com.example.identityservice.dto.request.IntrospectRequest;
import com.example.identityservice.dto.request.LogoutRequest;
import com.example.identityservice.dto.request.RefreshTokenRequest;
import com.example.identityservice.dto.response.AuthenticationResponse;
import com.example.identityservice.dto.response.IntrospectResponse;
import com.example.identityservice.entity.Account;
import com.example.identityservice.entity.InvalidToken;
import com.example.identityservice.enums.ErrorCode;
import com.example.identityservice.exception.AppException;
import com.example.identityservice.repository.AccountRepository;
import com.example.identityservice.repository.InvalidTokenRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService {
    @Value("${spring.security.oauth2.resourceserver.jwt.public-key-location}")
    @NonFinal
    String SECRET_KEY;
    AccountRepository accountRepository;
    InvalidTokenRepository invalidTokenRepository;
    PasswordEncoder passwordEncoder= new BCryptPasswordEncoder(10);
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
      var account =accountRepository.findByUsername(request.getUsername()).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND));
      boolean authenticated = passwordEncoder.matches(request.getPassword(), account.getPassword());
      if (!authenticated) {
          throw new AppException(ErrorCode.LOGIN_FAIL);
      }
      String token = generateToken(account);
      return AuthenticationResponse.builder()
              .token(token)
              .isValid(true)
              .build();
    }
    private String generateToken(Account account) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimNames = new JWTClaimsSet.Builder()
                .subject(account.getUsername())
                .issuer("Dyy.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope",buildScope(account))
                .build();
        Payload payload = new Payload(jwtClaimNames.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
    private String buildScope(Account account){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(account.getRoles())){
            account.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_"+role.getTypeOfRole().getRoleName());
                if(!CollectionUtils.isEmpty(role.getPermissions())){
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName().getPermissionName());
                    });
                }
            });
        }
        return stringJoiner.toString();
    }
    public IntrospectResponse introspect(IntrospectRequest resquest) throws JOSEException, ParseException {
        var token = resquest.getToken();
        boolean valid=true;
        try {
            verifyToken(token);
        }catch (AppException e){
            valid=false;
        }
        return  IntrospectResponse.builder()
                .isValid(valid)
                .build();
    }
    private SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        JWSVerifier jwsVerifier = new MACVerifier(SECRET_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verify = signedJWT.verify(jwsVerifier);
        if(!(verify&&expirationDate.after(new Date()))){
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        if(invalidTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        return signedJWT;
    }
    public void log0ut(LogoutRequest resquest) throws ParseException, JOSEException {
        var signedJWT = verifyToken(resquest.getToken());
        String jid = signedJWT.getJWTClaimsSet().getJWTID();
        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        InvalidToken invalidToken=InvalidToken.builder()
                .id(jid)
                .expiryTime(expirationDate)
                .build();
        invalidTokenRepository.save(invalidToken);
    }
    public AuthenticationResponse refreshToken (RefreshTokenRequest resquest) throws ParseException, JOSEException {
        var signedJWT = verifyToken(resquest.getToken());
        var jid = signedJWT.getJWTClaimsSet().getJWTID();
        var expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        InvalidToken invalidToken = InvalidToken.builder()
                .id(jid)
                .expiryTime(expirationDate)
                .build();
        invalidTokenRepository.save(invalidToken);
        var userName = signedJWT.getJWTClaimsSet().getSubject();
        Account account = accountRepository.findByUsername(userName).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND));
        String token = generateToken(account);
        return AuthenticationResponse.builder()
                .token(token)
                .isValid(true)
                .build();
    }
}
