package com.nodes.project.api.controller;

import com.nodes.project.api.dto.PublishJobRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 供WMS调用
 *
 * @author dml
 */
@RestController
@Slf4j
@RequestMapping("wms")
public class WmsController {

    /**
     * 下发JOB
     * WMS提供明细集合
     */
    @PostMapping("publishJob")
    public void publishJob(@Validated @RequestBody List<PublishJobRequest> publishJobRequestList) {
        log.info("下发JOB 请求参数：{}", publishJobRequestList);
        
    }

    /**
     * 取消JOB
     */
    @PostMapping("cancelJob")
    public void cancelJob() {

    }

    /**
     * 终止JOB
     */
    @PostMapping("terminationJob")
    public void terminationJob() {

    }

    // wms提供:
    // 回传状态的接口
    // 任务开始
    // 任务结束(正常)
    // 任务异常：返回AGV名称


}
