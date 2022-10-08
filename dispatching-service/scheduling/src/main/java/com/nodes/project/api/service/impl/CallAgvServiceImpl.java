package com.nodes.project.api.service.impl;

import com.nodes.common.constant.JobConstants;
import com.nodes.common.utils.StringUtils;
import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.dto.agv.*;
import com.nodes.project.api.service.CallAgvService;
import com.nodes.project.api.service.CallApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CallAgvServiceImpl implements CallAgvService {
    private static final String URL_TRANSPORT_ORDERS = "/transportOrders/{}";
    private static final String URL_WITHDRAWAL = URL_TRANSPORT_ORDERS + "/withdrawal";

    @Resource
    private CallApiService callApiService;

    @Override
    public AgvGlobalResponse transportOrders(JobQueue jobQueue) {
        AgvTransportOrderRequest agvTransportOrderRequest = getAgvTransportOrderRequest(jobQueue);
        return callApiService.postAgv(StringUtils.format(URL_TRANSPORT_ORDERS, jobQueue.getId()), agvTransportOrderRequest);
    }

    @NotNull
    private AgvTransportOrderRequest getAgvTransportOrderRequest(JobQueue jobQueue) {
        AgvTransportOrderRequest agvTransportOrderRequest = new AgvTransportOrderRequest();
        List<Destination> destinations = new ArrayList<>();
        // 起点
        Destination fromDestination = new Destination();
        fromDestination.setLocationName(jobQueue.getLocationNameFrom());
        destinations.add(fromDestination);
        // 终点
        Destination toDestination = new Destination();
        toDestination.setLocationName(jobQueue.getLocationNameTo());
        destinations.add(toDestination);
        agvTransportOrderRequest.setDestinations(destinations);

        List<Property> properties = new ArrayList<>();
        Property property = new Property();
        property.setKey(JobConstants.AGV_PROPERTY_JOB_ID);
        property.setValue(jobQueue.getId());
        properties.add(property);

        setBifurcate(jobQueue, properties);

        agvTransportOrderRequest.setProperties(properties);

        return agvTransportOrderRequest;
    }

    private static void setBifurcate(JobQueue jobQueue, List<Property> properties) {
        // 创建C箱的job时，在properties属性中新增一个key：boxType，value:C1或C2
        boolean cBifurcateFlag = ObjectUtils.isEmpty(jobQueue.getWmsCBifurcate());
        if (cBifurcateFlag) {
            return;
        }
        boolean c1Flag = jobQueue.getWmsCBifurcate().equals(1);
        Property property = new Property();
        property.setKey(JobConstants.AGV_C_BIFURCATE);
        property.setValue(c1Flag ? JobConstants.AGV_C1 : JobConstants.AGV_C2);
        properties.add(property);
    }

    @Override
    public AgvGlobalResponse withdrawal(JobQueue jobQueue) {
        AgvWithdrawalRequest agvWithdrawalRequest = new AgvWithdrawalRequest();
        agvWithdrawalRequest.setName(jobQueue.getId());
        return callApiService.postAgv(StringUtils.format(URL_WITHDRAWAL, jobQueue.getId()), agvWithdrawalRequest);
    }
}
