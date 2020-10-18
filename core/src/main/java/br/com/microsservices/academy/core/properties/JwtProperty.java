package br.com.microsservices.academy.core.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt.config")
@Getter
@Setter
public class JwtProperty {
    private String loginUrl ="/login/**";
    @NestedConfigurationProperty // Relacionado a interpretar como o objeto passado
    private Header header = new Header();
    private int expiration = 36000;
    private String privateKey = "SN3edLgFY82Kfq6txYERpIlo3pgiDta1"; // A chave precisa possuir 32 bytes
    private String type = "encrypted";

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Header{
        private String name = "Authorization";
        private String prefix = "Bearer ";
    }
}
