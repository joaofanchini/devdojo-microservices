package br.com.microsservices.academy.security.configs;

import br.com.microsservices.academy.core.properties.JwtProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Description("Lembrar que a url por padrão com o Spring Security para o token é /login")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    protected final JwtProperty jwtProperty;

    @Override
    @Description("Configuração básica para Cors e modelos de autenticação")
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().configurationSource(httpServletRequest -> new CorsConfiguration().applyPermitDefaultValues())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(
                        ((httpServletRequest, httpServletResponse, ex) -> httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                )
                .and()
                .authorizeRequests()
                    .antMatchers(jwtProperty.getLoginUrl()).permitAll()
                    .antMatchers("/course/admin/**").hasRole("ADMIN") // Note que pela arquitetura, o path da requisição deverá conter course, graças ao gateway
                    .anyRequest().authenticated();
    }
}
