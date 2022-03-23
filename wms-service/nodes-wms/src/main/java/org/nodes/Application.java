package org.nodes;

import lombok.extern.slf4j.Slf4j;
import org.nodes.wms.core.basedata.cache.BillTypeCache;
import org.springblade.core.launch.BladeApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 启动器
 *
 * @author Nodes
 */
@Slf4j
@EnableScheduling
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		BladeApplication.run("WMS v3.2", Application.class, args);
	}
}

