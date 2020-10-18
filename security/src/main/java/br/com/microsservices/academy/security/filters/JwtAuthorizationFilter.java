package br.com.microsservices.academy.security.filters;

import br.com.microsservices.academy.core.properties.JwtProperty;
import br.com.microsservices.academy.security.helpers.TokenParserHelper;
import br.com.microsservices.academy.security.utils.SecurityContextUtil;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Description("Responsável por ser executado a cada nova requisição, apenas uma vez. Lembrar que em segurança, trabalhamos com dois filtros, um para autenticação e outro para autorização")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    protected final JwtProperty jwtProperty;
    protected final TokenParserHelper tokenParserHelper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeaderValue = httpServletRequest.getHeader(jwtProperty.getHeader().getName());

        // A requisição pode estar indo para um endpoint cujo header não seja necessário
        if(authorizationHeaderValue == null || !authorizationHeaderValue.startsWith(jwtProperty.getHeader().getPrefix())){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String jweToken = authorizationHeaderValue.trim().replace(jwtProperty.getHeader().getPrefix(), "");

        SecurityContextUtil.setSecurityContext(StringUtils.equalsIgnoreCase(jwtProperty.getType(),"signed") ? justValidate(jweToken) : decryptAndValidate(jweToken));

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    @Description("Método para tratar com o token criptografado")
    private SignedJWT decryptAndValidate(String jweToken){
        String jwtToken = tokenParserHelper.decryptToken(jweToken);
        return tokenParserHelper.validateToken(jwtToken);
    }

    @Description("Método para tratar com o token apenas assinado")
    private SignedJWT justValidate(String jwtToken){
        return tokenParserHelper.validateToken(jwtToken);
    }
}