package br.com.microsservices.academy.gateway;

import br.com.microsservices.academy.core.properties.JwtProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@EnableConfigurationProperties(value = JwtProperty.class)
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@ComponentScan({"br.com.microsservices.academy.core","br.com.microsservices.academy.security","br.com.microsservices.academy.auth","br.com.microsservices.academy.gateway"}) // Com esta anotações, ele também entende os Beans criados em outros pacotes
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

}
