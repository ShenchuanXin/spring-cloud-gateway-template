package com.scx.spring.cloud.gateway.template.router;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;


/**
 * @author scx
 * @date 2020-07-29 15:57
 * @Description
 */
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {

    private static Logger log = LoggerFactory.getLogger(NacosRouteDefinitionRepository.class);

    private NacosConfigManager configManager;

    private ApplicationEventPublisher publisher;

    public NacosRouteDefinitionRepository(NacosConfigManager configManager, ApplicationEventPublisher publisher) {
        this.configManager = configManager;
        this.publisher = publisher;
        addListener();
    }

    /**
     * 添加Nacos监听
     */
    private void addListener() {
        try {
            configManager.getConfigService().addListener("spring-cloud-gateway-template.yaml", "DEFAULT_GROUP", new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    publisher.publishEvent(new RefreshRoutesEvent(this));
                }
            });
        } catch (NacosException e) {
            log.error("nacos-addListener-error", e);
        }
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        try {
            String content = this.configManager.getConfigService().getConfig("spring-cloud-gateway-template.yaml", "DEFAULT_GROUP", 5000);
            log.info("nacos config route info: {}", content);
            if (Strings.isNotEmpty(content)) {
                routeDefinitions = JSONObject.parseArray(content, RouteDefinition.class);
            }
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }

    private List<RouteDefinition> getListByStr(String content) {
        if (Strings.isNotEmpty(content)) {
            return JSONObject.parseArray(content, RouteDefinition.class);
        }
        return new ArrayList<>(0);
    }
}
