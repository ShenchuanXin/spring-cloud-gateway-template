package com.scx.spring.cloud.gateway.template.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.scx.spring.cloud.gateway.template.router.NacosRouteDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author scx
 * @date 2020-07-29 14:01
 * @Description
 */
@Configuration
public class DynamicRouteConfig {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private NacosConfigManager configManager;

    @Bean
    public RouteDefinitionRepository routeDefinitionRepository(){
        return new NacosRouteDefinitionRepository(configManager, publisher);
    }
}
