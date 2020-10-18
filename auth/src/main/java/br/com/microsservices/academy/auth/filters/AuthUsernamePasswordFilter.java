package br.com.microsservices.academy.auth.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Description("Com o spring, basta extender o UserNamePasswordAuthenticationFilter para criar um filtro de autenticação")
public class AuthUsernamePasswordFilter extends UsernamePasswordAuthenticationFilter {
}
