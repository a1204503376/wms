package com.nodes.project.api.controller;

import com.nodes.common.constant.Constants;
import com.nodes.framework.aspectj.lang.annotation.Log;
import com.nodes.framework.web.domain.AjaxResult;
import com.nodes.project.api.dto.agv.AgvSyncExceptionRequest;
import com.nodes.project.api.dto.agv.AgvSyncOrderRequest;
import com.nodes.project.api.service.AgvExceptionService;
import com.nodes.project.api.service.AgvSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 本系统提供给AGV的接口
 */
@RestController
@Slf4j
@Validated
@RequestMapping(Constants.API_AGV)
public class AgvController {

    @Resource
    private AgvSyncService agvSyncService;
    @Resource
    private AgvExceptionService agvExceptionService;

    /**
     * AGV同步订单信息
     */
    @Log(title = Constants.API_AGV_NAME)
    @PostMapping("syncOrder")
    public AjaxResult syncOrder(@Valid
                                @RequestBody
                                AgvSyncOrderRequest agvSyncOrderRequest) {
        return agvSyncService.syncOrder(agvSyncOrderRequest);
    }

    /**
     * AGV同步非运输过程中的异常
     */
    @Log(title = Constants.API_AGV_NAME)
    @PostMapping("syncException")
    public AjaxResult syncException(@Valid
                                    @RequestBody
                                    AgvSyncExceptionRequest agvSyncExceptionRequest) {

        return agvExceptionService.syncException(agvSyncExceptionRequest);
    }

}
