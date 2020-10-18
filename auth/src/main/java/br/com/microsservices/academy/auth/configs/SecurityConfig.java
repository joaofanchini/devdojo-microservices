package br.com.microsservices.academy.auth.configs;

import br.com.microsservices.academy.auth.filters.AuthUsernamePasswordFilter;
import br.com.microsservices.academy.core.properties.JwtProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity // Anotação necessária para aplicação do Spring Security
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtProperty jwtProperty;
    private final UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

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
                .addFilter(new AuthUsernamePasswordFilter(jwtProperty)) // O Filtro que irá fazer nossa authenticação para cada requisição
                .authorizeRequests()
                    .antMatchers(jwtProperty.getLoginUrl()).permitAll()
                    .antMatchers("/course/admin/**").hasRole("ADMIN") // Note que pela arquitetura, o path da requisição deverá conter course, graças ao gateway
                    .anyRequest().authenticated();
    }

    @Override
    @Description("Responsável por fazer a authenticação chamando o método findByUserName pela implementação do UserDetailsService")
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

}
