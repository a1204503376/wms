package com.nodes.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "shiro")
public class ShiroAnonConfig {

    private Map<String, String> filterChainDefinitionMap = new HashMap<>();
}
