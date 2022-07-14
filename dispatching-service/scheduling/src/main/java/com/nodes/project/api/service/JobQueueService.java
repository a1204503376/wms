package com.nodes.project.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nodes.framework.web.domain.AjaxResult;
import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.dto.JobActionRequest;
import com.nodes.project.api.dto.PublishJobRequest;

import java.util.List;
import java.util.Optional;

/**
 * 针对表【job_queue(作业队列
 * 存放待处理的作业，处理完成移动到作业历史表)】的数据库操作Service
 */
public interface JobQueueService extends IService<JobQueue> {

    AjaxResult publishJob(List<PublishJobRequest> publishJobRequestList);

    AjaxResult continueJob(JobActionRequest jobActionRequest);

    AjaxResult cancelJob(JobActionRequest jobActionRequest);

    AjaxResult terminationJob(JobActionRequest jobActionRequest);

    void deleteJobCopyToHistory(JobQueue jobQueue);

    boolean deleteJobCopyToTimeout(JobQueue jobQueue);

    Optional<JobQueue> findNonOutbound();

    Optional<JobQueue> findOutboundA();

    Optional<JobQueue> findOutboundB();

    Optional<JobQueue> findOutboundC();

    Optional<JobQueue> findOutboundD();

    boolean updateStatus(JobQueue jobQueue);

    boolean updateStatusAndBeginTime(JobQueue jobQueue);
}
