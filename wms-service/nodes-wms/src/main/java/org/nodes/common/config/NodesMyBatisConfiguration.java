package org.nodes.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"org.springblade", "org.nodes"})
@MapperScan({"org.springblade.**.mapper.**", "org.nodes.**.mapper.**"})
public class NodesMyBatisConfiguration {
}
