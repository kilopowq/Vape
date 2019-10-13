package com.kilopo.vape;

import com.kilopo.vape.configuration.external.LoginConfig;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@SpringBootApplication
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties({LoginConfig.class})
public class VapeApplication {


    @Bean("identityServer")
    Key wso2Key(@Value("${identity-server.public-key}") final String plainTextKey) {
        try {
            final byte[] keyBytes = Base64.getMimeDecoder().decode(plainTextKey);

            final X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            final KeyFactory kf = KeyFactory.getInstance("RSA");

            return kf.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    JwtParser jwtParser(@Qualifier("identityServer") Key identityServerKey) {
        return Jwts.parser().setSigningKey(identityServerKey);
    }

    public static void main(String[] args) {
        SpringApplication.run(VapeApplication.class, args);
    }


}
