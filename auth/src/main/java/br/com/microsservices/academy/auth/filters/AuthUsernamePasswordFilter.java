package br.com.microsservices.academy.auth.filters;

import br.com.microsservices.academy.core.models.User;
import br.com.microsservices.academy.core.properties.JwtProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Description("Com o spring, basta extender o UserNamePasswordAuthenticationFilter para criar um filtro de autenticação" +
        "Essa classe possui muitas responsabilidades que podem ser listadas como: realizar a autenticacao, " +
        "gerar o token, Criptografar o token, assinar o token e retorna-lo." +
        "Poderíamos dividir algumas dessas funcionalidades em diferentes endpoints")
public class AuthUsernamePasswordFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProperty jwtProperty;

    @Description("Método responsável por tentar realizar a autenticação do usuário. a anotação @SneakyThrows excapsula qualquer tipo de exceção em um" +
            "try catch, lançando um runtime." +
            "Por conceito, o principal pode ser o username ou um objeto que possui as informações do usuário (Como o objetivo é o aprendizado, será feito apenas como o username)." +
            "No final o retorno é obtido pelo autehntication manager, utilizando o método authenticate().")
    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        log.info("Authenticating...");
        User user = new ObjectMapper().readValue(request.getInputStream(), User.class);

        if(user == null)
            throw new UsernameNotFoundException("Unable to retrive username or password");

        log.info("Creating authentication object for the user with username: {}", user.getUsername());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getUsername(), Collections.emptyList());
        authenticationToken.setDetails(user);

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Description("Método executado quando a autenticação é bem sucedida. Em todo token primeiro você assina e depois criptografa")
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("Authentication successful for the user {}. Generating token",authResult.getPrincipal());

    }

    @Description("Criptografar e assinar o token (Passa-se a chave publica no token e assina-se com a chave privada)")
    @SneakyThrows
    private SignedJWT createSignedJwt(Authentication auth){
        User principal = (User) auth.getPrincipal();
        JWTClaimsSet claims = createJWTClaimSet(auth, principal);
        KeyPair keyPair = generateKeys();

        JWK jwtPublicKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .keyID(UUID.randomUUID().toString())
                .build(); // Chave pública

        // Adicionando informações a serem enviadas ao header e body do token
        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
                .jwk(jwtPublicKey)
                .type(JOSEObjectType.JWT)
                .build(), claims);

        RSASSASigner signWithPrivateKey = new RSASSASigner(keyPair.getPrivate()); //Assinando com a chave privada

        signedJWT.sign(signWithPrivateKey);

        log.info("Serialized token {}", signedJWT.serialize());

        return signedJWT;
    }

    @Description("Criar os claims para o token")
    private JWTClaimsSet createJWTClaimSet(Authentication auth, User user){
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
    private KeyPair generateKeys(){
        KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA");
        rsa.initialize(2048);

        return rsa.genKeyPair();
    }

    @Description("Encriptar token criptografado com as propriedades definidas com JWT  (o que vira o JWE)")
    private String encryptTokenToJwe(SignedJWT signedJWT) throws JOSEException {
        DirectEncrypter directEncrypter = new DirectEncrypter(jwtProperty.getPrivateKey().getBytes()); // Modelo de encriptação direta

        JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256)
                .contentType("JWT")
                .build(), new Payload(signedJWT));

        jweObject.encrypt(directEncrypter);

        return jweObject.serialize();
    }
}
