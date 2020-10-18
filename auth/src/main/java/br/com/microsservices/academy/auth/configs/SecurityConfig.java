package br.com.microsservices.academy.auth.configs;

import br.com.microsservices.academy.auth.filters.AuthUsernamePasswordFilter;
import br.com.microsservices.academy.core.properties.JwtProperty;
import br.com.microsservices.academy.security.helpers.JwtHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity // Anotação necessária para aplicação do Spring Security
@Description("Lembrar que a url por padrão com o Spring Security para o token é /login")
public class SecurityConfig extends br.com.microsservices.academy.security.configs.SecurityConfig {
    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;

    // O Qualifier pode ser o nome da propria classe de implementação em cammel case
    public SecurityConfig(JwtProperty jwtProperty, JwtHelper jwtHelper, @Qualifier("userDetailsImplementation") UserDetailsService userDetailsService) {
        super(jwtProperty);
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Description("Configuração básica para Cors e modelos de autenticação")
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilter(new AuthUsernamePasswordFilter(jwtProperty, authenticationManager(), jwtHelper)); // O Filtro que irá fazer nossa authenticação para cada requisição

        super.configure(http);
    }

    @Override
    @Description("Responsável por fazer a authenticação chamando o método findByUserName pela implementação do UserDetailsService")
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}