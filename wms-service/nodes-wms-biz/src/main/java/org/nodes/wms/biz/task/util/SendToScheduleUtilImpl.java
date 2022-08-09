package org.nodes.wms.biz.task.util;

import lombok.extern.slf4j.Slf4j;
import org.nodes.wms.dao.application.dto.scheduling.SchedulingGlobalResponse;
import org.nodes.wms.dao.application.dto.scheduling.SchedulingResponse;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 请求调度系统API 接口实现
 **/
@Slf4j
@Service
public class SendToScheduleUtilImpl implements SendToScheduleUtil{

	@Resource
	private RestTemplate restTemplate;

	@Override
	public SchedulingGlobalResponse sendPost(String url, Object request){
		try {
			return SchedulingGlobalResponse.success(restTemplate.postForObject(url, request , SchedulingResponse.class));
		}catch (Exception e){
			log.error(StringUtil.format("请求调度系统异常，URL：{}", url));
			return SchedulingGlobalResponse.error(e);
		}
	}
}
