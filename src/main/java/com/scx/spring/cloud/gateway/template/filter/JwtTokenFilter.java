package com.scx.spring.cloud.gateway.template.filter;

import com.scx.spring.cloud.gateway.template.utils.JwtUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * @author scx
 * @date 2020-07-30 16:24
 * @Description 描述: JwtToken 过滤器 ==》认证
 */
public class JwtTokenFilter implements GlobalFilter, Ordered {

    private String[] excludeUrl;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String url = exchange.getRequest().getPath().contextPath().value();
        //跳过排除的url
        if(Arrays.asList(excludeUrl).contains(url)){
            return chain.filter(exchange);
        }

        ServerHttpResponse response = exchange.getResponse();
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(StringUtils.isBlank(token) || StringUtils.isEmpty(token)) {
            //不存在token
            return null;
        }else{
            //判断token有效性
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }

}
