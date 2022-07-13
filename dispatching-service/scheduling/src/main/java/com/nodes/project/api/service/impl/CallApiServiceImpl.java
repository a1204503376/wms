package com.nodes.project.api.service.impl;

import com.nodes.common.utils.DateUtils;
import com.nodes.common.utils.ExceptionUtil;
import com.nodes.common.utils.StringUtils;
import com.nodes.project.api.domain.CallApiLog;
import com.nodes.project.api.dto.agv.AgvGlobalResponse;
import com.nodes.project.api.dto.agv.AgvResponse;
import com.nodes.project.api.dto.wms.WmsGlobalResponse;
import com.nodes.project.api.dto.wms.WmsResponse;
import com.nodes.project.api.mapper.CallApiLogMapper;
import com.nodes.project.api.service.CallApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tech.powerjob.common.serialize.JsonUtils;

import javax.annotation.Resource;

@Slf4j
@Service
public class CallApiServiceImpl implements CallApiService {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private CallApiLogMapper callApiLogMapper;
    @Resource
    private CallApiService callApiService;

    @Override
    public WmsGlobalResponse postWms(String url, Object request) {
        try {
            WmsResponse wmsResponse = restTemplate.postForObject(
                    url,
                    request,
                    WmsResponse.class);
            callApiService.saveCallApiLog(url, request, wmsResponse, null);
            return WmsGlobalResponse.success(wmsResponse);
        } catch (Exception e) {
            String msg = StringUtils.format("呼叫WMS异常，URL：{}", url);
            log.error(msg, e);
            callApiService.saveCallApiLog(url, request, null, e);
            return WmsGlobalResponse.error(e);
        }
    }

    @Override
    public AgvGlobalResponse postAgv(String url, Object request) {
        try {
            AgvResponse agvResponse = restTemplate.postForObject(
                    url,
                    request,
                    AgvResponse.class);
            callApiService.saveCallApiLog(url, request, agvResponse, null);
            return AgvGlobalResponse.success(agvResponse);
        } catch (Exception e) {
            String msg = StringUtils.format("呼叫AGV异常，URL：{}", url);
            log.error(msg, e);
            callApiService.saveCallApiLog(url, request, null, e);
            return AgvGlobalResponse.error(e);
        }
    }

    @Async
    public void saveCallApiLog(String url, Object request, Object response, Exception ex) {
        CallApiLog callApiLog = new CallApiLog();
        callApiLog.setUrl(url);
        callApiLog.setRequestData(JsonUtils.toJSONString(request));
        callApiLog.setResponseData(JsonUtils.toJSONString(response));
        callApiLog.setExceptionMsg(ExceptionUtil.getExceptionMessage(ex));
        callApiLog.setCreateTime(DateUtils.getNowDate());

        callApiLogMapper.insert(callApiLog);
    }
}
