package com.scx.spring.cloud.gateway.template.config;

import com.scx.spring.cloud.gateway.template.filter.JwtTokenFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author scx
 * @date 2020-07-30 16:53
 * @Description
 */
@Configuration
public class AuthConfig {

    @Bean
    @ConditionalOnProperty(prefix = "com.scx.auth", name = "jwt.isEnable", havingValue = "true")
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter();
    }
}
