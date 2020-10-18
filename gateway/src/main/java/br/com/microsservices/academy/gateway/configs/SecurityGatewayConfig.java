package br.com.microsservices.academy.gateway.configs;

import br.com.microsservices.academy.core.properties.JwtProperty;
import br.com.microsservices.academy.gateway.filters.GatewayJwtAuthorizationFilter;
import br.com.microsservices.academy.security.configs.SecurityConfig;
import br.com.microsservices.academy.security.helpers.TokenParserHelper;
import org.springframework.context.annotation.Description;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Description("O Gateway que vai repassar o token, identificando se o gateway é apenas assinado ou criptografado ao enviar. Por isso o modo como ele utiliza os filtros é diferente")
public class SecurityGatewayConfig extends SecurityConfig {
    private final TokenParserHelper tokenParserHelper;
    private final JwtProperty jwtProperty;

    public SecurityGatewayConfig(JwtProperty jwtProperty, TokenParserHelper tokenParserHelper) {
        super(jwtProperty);
        this.tokenParserHelper = tokenParserHelper;
        this.jwtProperty = jwtProperty;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(new GatewayJwtAuthorizationFilter(jwtProperty, tokenParserHelper), UsernamePasswordAuthenticationFilter.class);
        super.configure(http);
    }
}
