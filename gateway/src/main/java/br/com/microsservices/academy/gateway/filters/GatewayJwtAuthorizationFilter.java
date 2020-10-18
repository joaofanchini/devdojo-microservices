package br.com.microsservices.academy.gateway.filters;

import br.com.microsservices.academy.core.properties.JwtProperty;
import br.com.microsservices.academy.security.filters.JwtAuthorizationFilter;
import br.com.microsservices.academy.security.helpers.TokenParserHelper;
import br.com.microsservices.academy.security.utils.SecurityContextUtil;
import com.netflix.zuul.context.RequestContext;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.lang.NonNull;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class GatewayJwtAuthorizationFilter extends JwtAuthorizationFilter {
    public GatewayJwtAuthorizationFilter(JwtProperty jwtProperty, TokenParserHelper tokenParserHelper) {
        super(jwtProperty, tokenParserHelper);
    }

    @Description("O token que irá passar pelo gateway será sempre criptografado. Precisa passar o Security Context, pois é ele quem valida as ROLES, como definido pelo configure do Security Config")
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeaderValue = httpServletRequest.getHeader(jwtProperty.getHeader().getName());

        // A requisição pode estar indo para um endpoint cujo header não seja necessário
        if(authorizationHeaderValue == null || !authorizationHeaderValue.startsWith(jwtProperty.getHeader().getPrefix())){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String jweToken = authorizationHeaderValue.trim().replace(jwtProperty.getHeader().getPrefix(), "");

        String jwtToken = tokenParserHelper.decryptToken(jweToken);

        SignedJWT signedJWT = tokenParserHelper.validateToken(jwtToken);

        SecurityContextUtil.setSecurityContext(signedJWT);

        // Irá enviar o token assinado (JWT) ou o encriptado (JWE) para os serviços, sobrescrevendo o header Authorization. O token irá vir do client como JWE
        if(jwtProperty.getType().equalsIgnoreCase("signed"))
            RequestContext.getCurrentContext().addZuulRequestHeader(jwtProperty.getHeader().getName(),jwtProperty.getHeader().getPrefix().concat(jwtToken));

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
