package br.com.microsservices.academy.auth.filters;

import br.com.microsservices.academy.core.models.User;
import br.com.microsservices.academy.core.properties.JwtProperty;
import br.com.microsservices.academy.security.helpers.JwtHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Description("Com o spring, basta extender o UserNamePasswordAuthenticationFilter para criar um filtro de autenticação" +
        "Essa classe possui muitas responsabilidades que podem ser listadas como: realizar a autenticacao, " +
        "gerar o token, Criptografar o token, assinar o token e retorna-lo." +
        "Poderíamos dividir algumas dessas funcionalidades em diferentes endpoints")
public class AuthUsernamePasswordFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProperty jwtProperty;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;

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

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), Collections.emptyList());
        authenticationToken.setDetails(user);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Description("Método executado quando a autenticação é bem sucedida. Em todo token primeiro você assina e depois criptografa")
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) {
        log.info("Authentication successful for the user {}. Generating token",auth.getPrincipal());

        SignedJWT signedJwt = jwtHelper.createSignedJwt(auth);

        String tokenEncrypted = jwtHelper.encryptTokenToJwe(signedJwt);

        response.addHeader("Access-Control-Expose-Headers","XSRF-TOKEN, ".concat(jwtProperty.getHeader().getName())); // Headers para o browser permitir a requisição

        response.addHeader(jwtProperty.getHeader().getName(), jwtProperty.getHeader().getPrefix().concat(tokenEncrypted)); // Retornando o token da requisição no header

    }
}
