package br.com.microsservices.academy.auth;

import br.com.microsservices.academy.core.properties.JwtProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"br.com.microsservices.academy.core","br.com.microsservices.academy.security","br.com.microsservices.academy.auth"}) // Com esta anotações, ele também entende os Beans criados em outros pacotes
@EnableConfigurationProperties(value = JwtProperty.class) // É mais correto esse cara ficar em uma classe de configuração
@EnableEurekaClient
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

}
