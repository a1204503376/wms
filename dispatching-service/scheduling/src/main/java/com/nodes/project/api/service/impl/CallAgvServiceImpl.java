package com.nodes.project.api.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nodes.common.constant.JobConstants;
import com.nodes.common.exception.ServiceException;
import com.nodes.common.utils.StringUtils;
import com.nodes.processor.ProcessResultUtils;
import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.dto.agv.*;
import com.nodes.project.api.service.CallAgvService;
import com.nodes.project.api.service.CallApiService;
import com.nodes.project.api.service.IEmailService;
import com.nodes.project.system.user.domain.User;
import com.nodes.project.system.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import tech.powerjob.worker.core.processor.ProcessResult;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CallAgvServiceImpl implements CallAgvService {
    private static final String URL_TRANSPORT_ORDERS = "/transportOrders/{}";
    private static final String URL_WITHDRAWAL = URL_TRANSPORT_ORDERS + "/withdrawal";
    private static final String url_vehicleWithdrawal = "/vehiclesWithdrawal/{}/unload/{}";
    private static final String URL_VEHICLES = "/vehicles";
    /**
     * 天宜仓库部门ID
     */
    private static final Long DEPT_ID = 101L;

    @Resource
    private CallApiService callApiService;
    @Resource
    private IEmailService emailService;
    @Resource
    private IUserService userService;

    private static void setBoxTypeProperty(JobQueue jobQueue, List<Property> properties) {
        Property property = new Property();
        property.setKey(JobConstants.AGV_C_BIFURCATE);
        // 创建C箱的job时，在properties属性中新增一个key：boxType，value:C1或C2
        boolean cBifurcateFlag = ObjectUtils.isEmpty(jobQueue.getWmsCBifurcate()) || jobQueue.getWmsCBifurcate().equals(0);
        String value;
        if (cBifurcateFlag) {
            // A,B,D也需要传给AGV
            value = jobQueue.getWmsBoxType().getCode();
        } else {
            value = jobQueue.getWmsCBifurcate().equals(1) ? JobConstants.AGV_C1 : JobConstants.AGV_C2;
        }
        property.setValue(value);
        properties.add(property);
    }

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

        setBoxTypeProperty(jobQueue, properties);

        agvTransportOrderRequest.setProperties(properties);

        return agvTransportOrderRequest;
    }

    @Override
    public AgvGlobalResponse withdrawal(JobQueue jobQueue) {
        AgvWithdrawalRequest agvWithdrawalRequest = new AgvWithdrawalRequest();
        agvWithdrawalRequest.setName(jobQueue.getId());
        return callApiService.postAgv(StringUtils.format(URL_WITHDRAWAL, jobQueue.getId()), agvWithdrawalRequest);
    }

    @Override
    public AgvOtherGlobalResponse vehicleWithdrawal(String vehicle, String location) {
        String url = StringUtils.format(url_vehicleWithdrawal, vehicle, location);
        return callApiService.postOtherAgv(url);
    }

    @Override
    public ProcessResult agvVehicles() {
        List<AgvVehiclesResponse> vehiclesAgvList = callApiService.getVehiclesAgv(URL_VEHICLES);
        if (ObjectUtils.isEmpty(vehiclesAgvList) || vehiclesAgvList.size() == 0) {
            throw new ServiceException("发送邮件失败;获取AGV状态失败");
        }

        List<User> userList = userService.selectUserListByDeptId(DEPT_ID);
        if (ObjectUtils.isEmpty(userList) || userList.size() == 0) {
            throw new ServiceException("发送邮件失败;未配置天宜仓库管理员的邮箱");
        }

        for (AgvVehiclesResponse agvVehicle : vehiclesAgvList) {
            if (agvVehicle.getEnergyLevel().intValue() <= 20) {
                for (User user : userList) {
                    emailService.sendSimpleMail(user.getEmail(),
                            "AGV电量预警提示",
                            String.format("系统在[%s]检测到[%s]电量为[%s][%s]低于预期设置的阀值，请检查AGV是否在充电",
                                    LocalDateTime.now(), agvVehicle.getType(), agvVehicle.getEnergyLevel().intValue(), "%"));
                }
            }
        }
        return ProcessResultUtils.success("发送邮件成功");
    }
}
