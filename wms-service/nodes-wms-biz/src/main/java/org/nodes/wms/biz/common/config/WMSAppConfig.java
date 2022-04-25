package org.nodes.wms.biz.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "wms")
public class WMSAppConfig {

	/**
	 * 项目名称，用来配置redis的前缀
	 */
	private String projectName;
}
