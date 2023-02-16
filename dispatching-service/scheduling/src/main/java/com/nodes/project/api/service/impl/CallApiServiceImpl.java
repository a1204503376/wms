package com.nodes.project.api.service.impl;

import com.nodes.common.utils.StringUtils;
import com.nodes.framework.config.NodesConfig;
import com.nodes.project.api.dto.agv.*;
import com.nodes.project.api.dto.wms.WmsGlobalResponse;
import com.nodes.project.api.dto.wms.WmsResponse;
import com.nodes.project.api.service.CallApiDataService;
import com.nodes.project.api.service.CallApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tech.powerjob.common.serialize.JsonUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CallApiServiceImpl implements CallApiService {

    @Resource
    private NodesConfig nodesConfig;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private CallApiDataService callApiDataService;


    @Override
    public WmsGlobalResponse postWms(String url, Object request) {
        url = nodesConfig.getWmsUrl() + url;
        try {
            WmsResponse wmsResponse = restTemplate.postForObject(
                    url,
                    request,
                    WmsResponse.class);
            callApiDataService.saveCallApiLog(url, request, wmsResponse, null);
            return WmsGlobalResponse.success(wmsResponse);
        } catch (Exception e) {
            String msg = StringUtils.format("呼叫WMS异常，URL：{}", url);
            log.error(msg, e);
            callApiDataService.saveCallApiLog(url, request, null, e);
            return WmsGlobalResponse.error(e);
        }
    }

    @Override
    public AgvGlobalResponse postAgv(String url, Object request) {
        url = nodesConfig.getAgvUrl() + url;
        try {
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, request, String.class);
            String body = stringResponseEntity.getBody();
            AgvResponse agvResponse = JsonUtils.parseObject(body, AgvResponse.class);
            callApiDataService.saveCallApiLog(url, request, agvResponse, null);
            return AgvGlobalResponse.success(agvResponse);
        } catch (Exception e) {
            String msg = StringUtils.format("呼叫AGV异常，URL：{}", url);
            log.error(msg, e);
            callApiDataService.saveCallApiLog(url, request, null, e);
            return AgvGlobalResponse.error(e);
        }
    }

    @Override
    public AgvOtherGlobalResponse postOtherAgv(String url) {
        url = nodesConfig.getAgvUrl() + url;
        try {
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, null, String.class);
            String body = stringResponseEntity.getBody();
            AgvOtherResponse agvResponse = JsonUtils.parseObject(body, AgvOtherResponse.class);
            callApiDataService.saveCallApiLog(url, null, agvResponse, null);
            return AgvOtherGlobalResponse.success(agvResponse);
        } catch (Exception e) {
            String msg = StringUtils.format("呼叫AGV异常，URL：{}", url);
            log.error(msg, e);
            callApiDataService.saveCallApiLog(url, null, null, e);
            return AgvOtherGlobalResponse.error(e);
        }
    }

    @Override
    public List<AgvVehiclesResponse> getVehiclesAgv(String url) {
        url = nodesConfig.getAgvUrl() + url;
        try {
            ResponseEntity<String> stringResponseEntity = restTemplate.getForEntity(url, null, String.class);
            String body = stringResponseEntity.getBody();
            List<AgvVehiclesResponse> agvVehiclesList = new ArrayList<AgvVehiclesResponse>();
            agvVehiclesList = JsonUtils.parseObject(body, agvVehiclesList.getClass());

            callApiDataService.saveCallApiLog(url, null, agvVehiclesList, null);
            return agvVehiclesList;
        } catch (Exception e) {
            String msg = StringUtils.format("huoq异常，URL：{}", url);
            log.error(msg, e);
            callApiDataService.saveCallApiLog(url, null, null, e);
            return new ArrayList<>();
        }
    }
}
