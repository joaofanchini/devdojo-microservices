package br.com.microsservices.academy.security.helpers;

import br.com.microsservices.academy.core.properties.JwtProperty;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenParserHelper {
    private final JwtProperty jwtProperty;

    @Description("Decriptando token JWE e obtendo token JWT")
    @SneakyThrows
    public String decryptToken(String encryptToken){
        JWEObject jweTokenParsed = JWEObject.parse(encryptToken);

        DirectDecrypter directDecrypter = new DirectDecrypter(jwtProperty.getPrivateKey().getBytes());

        jweTokenParsed.decrypt(directDecrypter);

        log.info("Decrypting token JWE");

        return jweTokenParsed.getPayload().toSignedJWT().serialize();
    }

    @Description("Validar token de acordo com a chave pública enviada")
    @SneakyThrows
    public SignedJWT validateToken(String jwtToken){
        log.info("Validating JWT token");

        SignedJWT jwtTokenParsed = SignedJWT.parse(jwtToken);

        RSAKey publicKey = RSAKey.parse(jwtTokenParsed.getHeader().getJWK().toJSONObject());

        if(!jwtTokenParsed.verify(new RSASSAVerifier(publicKey)))
            throw new AccessDeniedException("Invalid token signature");

        return jwtTokenParsed;
    }
}