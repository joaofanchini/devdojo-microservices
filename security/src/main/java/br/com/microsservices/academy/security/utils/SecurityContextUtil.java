package br.com.microsservices.academy.security.utils;

import br.com.microsservices.academy.core.models.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jdk.internal.joptsimple.internal.Strings;
import jdk.jfr.Description;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Description("O token JWT é um token apenas assinado, e não criptografado")
public class SecurityContextUtil {
    public static void setSecurityContext(SignedJWT signedJWT){
        try{
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            String username = claims.getSubject();
            if(username == null)
                throw new JOSEException("Missing 'username' from token JWT");

            List<String> authorities = claims.getStringListClaim("authorities");
            User user = new User(
                    claims.getLongClaim("userId"),
                    username,
                    Strings.join(authorities, ",")
            );

            // Precisamos adicionar essas roles aqui, pois o filtro irá atuar juntamente com o filtro, que faz a validação pelas Roles
            // Não passamos as credenciais, pois não estaremos validando as credenciais e sim as roles
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, createAuthorities(authorities));
            auth.setDetails(signedJWT.serialize());

            // Acessado em qualquer lugar durante o percurso da requisição
            SecurityContextHolder.getContext().setAuthentication(auth);

        }catch (Exception e){
            log.error("Error while setting security context ", e);
            SecurityContextHolder.clearContext();
        }
    }

    private static List<SimpleGrantedAuthority> createAuthorities(List<String> authorities){
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
