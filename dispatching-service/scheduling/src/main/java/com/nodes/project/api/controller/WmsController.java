package com.nodes.project.api.controller;

import com.nodes.common.constant.Constants;
import com.nodes.framework.aspectj.lang.annotation.Log;
import com.nodes.framework.web.domain.AjaxResult;
import com.nodes.project.api.dto.JobActionRequest;
import com.nodes.project.api.dto.PublishJobRequest;
import com.nodes.project.api.service.JobQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 供WMS调用
 *
 * @author dml
 */
@RestController
@Slf4j
@Validated
@RequestMapping(Constants.API_WMS)
public class WmsController {

    @Resource
    private JobQueueService jobQueueService;

    /**
     * 下发JOB
     */
    @Log(title = Constants.API_WMS_NAME)
    @PostMapping("publishJob")
    public AjaxResult publishJob(@Valid
                                 @RequestBody
                                 List<PublishJobRequest> publishJobRequestList) {
        return jobQueueService.publishJob(publishJobRequestList);
    }

    /**
     * 继续执行JOB
     */
    @Log(title = Constants.API_WMS_NAME)
    @PostMapping("continueJob")
    public AjaxResult continueJob(JobActionRequest jobActionRequest) {
        return jobQueueService.continueJob(jobActionRequest);
    }

    /**
     * 取消JOB
     */
    @Log(title = Constants.API_WMS_NAME)
    @PostMapping("cancelJob")
    public AjaxResult cancelJob(JobActionRequest jobActionRequest) {
        return jobQueueService.cancelJob(jobActionRequest);
    }

    /**
     * 终止JOB
     */
    @Log(title = Constants.API_WMS_NAME)
    @PostMapping("terminationJob")
    public AjaxResult terminationJob(JobActionRequest jobActionRequest) {
        return jobQueueService.terminationJob(jobActionRequest);
    }

}
