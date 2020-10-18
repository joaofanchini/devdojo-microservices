package br.com.microsservices.academy.security.helpers;

import br.com.microsservices.academy.core.models.User;
import br.com.microsservices.academy.core.properties.JwtProperty;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtHelper {

    private final JwtProperty jwtProperty;

    @Description("Criptografar e assinar o token (Passa-se a chave publica no token e assina-se com a chave privada)")
    @SneakyThrows
    public SignedJWT createSignedJwt(Authentication auth){
        User principal = (User) auth.getPrincipal();
        JWTClaimsSet claims = createJWTClaimSet(auth, principal);
        KeyPair keyPair = generateKeys();

        JWK jwtPublicKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .keyID(UUID.randomUUID().toString())
                .build(); // Chave pública

        // Adicionando informações a serem enviadas ao header e body do token
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256)
                        .jwk(jwtPublicKey)
                        .type(JOSEObjectType.JWT)
                        .build(),claims);

        RSASSASigner signWithPrivateKey = new RSASSASigner(keyPair.getPrivate()); //Assinando com a chave privada
        signedJWT.sign(signWithPrivateKey);

        return signedJWT;
    }

    @Description("Criar os claims para o token")
    public JWTClaimsSet createJWTClaimSet(Authentication auth, User user){
        return  new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .claim("authorities",auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .issuer("https://github.com/joaofanchini/devdojo-microservices")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + (jwtProperty.getExpiration()*1000)))
                .build();
    }

    @Description("Gerar chave privada e pública")
    @SneakyThrows
    public KeyPair generateKeys(){
        KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA");
        rsa.initialize(2048);

        return rsa.genKeyPair();
    }

    @Description("Encriptar token criptografado com as propriedades definidas com JWT  (o que vira o JWE)")
    @SneakyThrows
    public String encryptTokenToJwe(SignedJWT signedJWT) {
        DirectEncrypter directEncrypter = new DirectEncrypter(jwtProperty.getPrivateKey().getBytes()); // Modelo de encriptação direta

        JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256)
                .contentType("JWT")
                .build(), new Payload(signedJWT));

        jweObject.encrypt(directEncrypter);

        return jweObject.serialize();
    }
}