package br.com.microsservices.academy.auth;

import br.com.microsservices.academy.core.properties.JwtProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"br.com.microsservices.academy.core","br.com.microsservices.academy.auth"})
@EnableConfigurationProperties(value = JwtProperty.class) // É mais correto esse cara ficar em uma classe de configuração
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

}
