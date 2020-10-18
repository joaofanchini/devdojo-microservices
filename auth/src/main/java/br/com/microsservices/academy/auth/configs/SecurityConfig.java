package br.com.microsservices.academy.auth.configs;

import br.com.microsservices.academy.auth.filters.AuthUsernamePasswordFilter;
import br.com.microsservices.academy.core.properties.JwtProperty;
import br.com.microsservices.academy.security.filters.JwtAuthorizationFilter;
import br.com.microsservices.academy.security.helpers.TokenCreatorHelper;
import br.com.microsservices.academy.security.helpers.TokenParserHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity // Anotação necessária para aplicação do Spring Security
@Description("Lembrar que a url por padrão com o Spring Security para o token é /login")
public class SecurityConfig extends br.com.microsservices.academy.security.configs.SecurityConfig {
    private final TokenCreatorHelper tokenCreatorHelper;
    private final TokenParserHelper tokenParserHelper;
    private final UserDetailsService userDetailsService;

    // O Qualifier pode ser o nome da propria classe de implementação em cammel case
    public SecurityConfig(JwtProperty jwtProperty, TokenCreatorHelper tokenCreatorHelper,
                          @Qualifier("userDetailsImplementation") UserDetailsService userDetailsService,
                          TokenParserHelper tokenParserHelper) {
        super(jwtProperty);
        this.tokenCreatorHelper = tokenCreatorHelper;
        this.userDetailsService = userDetailsService;
        this.tokenParserHelper = tokenParserHelper;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Description("Configuração básica para Cors e modelos de autenticação")
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilter(new AuthUsernamePasswordFilter(jwtProperty, authenticationManager(), tokenCreatorHelper)) // O Filtro que irá fazer nossa authenticação para cada requisição
                .addFilterAfter(new JwtAuthorizationFilter(jwtProperty,tokenParserHelper), UsernamePasswordAuthenticationFilter.class); // Filtro que será executado após o filtro indicado. Este filtro é o de Autorização
        super.configure(http);
    }

    @Override
    @Description("Responsável por fazer a authenticação chamando o método findByUserName pela implementação do UserDetailsService")
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}