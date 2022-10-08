package com.nodes.project.api.service;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nodes.framework.web.domain.AjaxResult;
import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.dto.JobActionRequest;
import com.nodes.project.api.dto.PublishJobRequest;

import java.util.List;
import java.util.Optional;

public interface JobQueueService extends IService<JobQueue> {

    /**
     * 仅有一条数据
     */
    default LambdaQueryChainWrapper<JobQueue> setOnlyWrapper(LambdaQueryChainWrapper<JobQueue> wrapper) {
        return wrapper.last("limit 1");
    }

    AjaxResult publishJob(List<PublishJobRequest> publishJobRequestList);

    AjaxResult continueJob(JobActionRequest jobActionRequest);

    AjaxResult cancelJob(JobActionRequest jobActionRequest);

    AjaxResult terminationJob(JobActionRequest jobActionRequest);

    void deleteJobCopyToHistory(JobQueue jobQueue);

    void batchDeleteJobCopyToHistory();

    void deleteJobCopyToTimeout(JobQueue jobQueue);

    Optional<JobQueue> findNonOutbound();

    Optional<JobQueue> findOutboundA();

    Optional<JobQueue> findOutboundB();

    Optional<JobQueue> findOutboundC();

    Optional<JobQueue> findOutboundD();

    boolean updateStatus(JobQueue jobQueue);

    boolean updateStatusAndBeginTime(JobQueue jobQueue);

    /**
     * 根据任务ID修改目标库位
     *
     * @param id             任务ID
     * @param locationNameTo 目标库位
     */
    void updateLocationNameToById(String id, String locationNameTo);
}
