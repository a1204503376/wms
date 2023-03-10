package com.nodes.project.api.service.impl;

import com.alibaba.fastjson2.JSON;
import com.nodes.common.constant.JobConstants;
import com.nodes.common.utils.DateUtils;
import com.nodes.common.utils.StringUtils;
import com.nodes.framework.config.NodesConfig;
import com.nodes.processor.ProcessResultUtils;
import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.domain.JobTimeout;
import com.nodes.project.api.dto.agv.AgvGlobalResponse;
import com.nodes.project.api.dto.agv.AgvResponse;
import com.nodes.project.api.dto.wms.WmsGlobalResponse;
import com.nodes.project.api.dto.wms.WmsResponse;
import com.nodes.project.api.enums.JobFlagSyncWmsEnum;
import com.nodes.project.api.enums.JobStatusEnum;
import com.nodes.project.api.enums.ProcessorEnum;
import com.nodes.project.api.service.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.WorkflowContext;
import tech.powerjob.worker.log.OmsLogger;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 */
@Service
public class ProcessorServiceImpl implements ProcessorService {

    @Resource
    private JobQueueService jobQueueService;
    @Resource
    private CallWmsService callWmsService;
    @Resource
    private CallAgvService callAgvService;
    @Resource
    private NodesConfig nodesConfig;
    @Resource
    private JobTimeoutService jobTimeoutService;

    @Override
    public ProcessResult selectJobQueue(TaskContext context, ProcessorEnum processorEnum) {
        OmsLogger omsLogger = context.getOmsLogger();

        omsLogger.debug("开始获取JOB");
        Optional<JobQueue> optionalJobQueue;
        switch (processorEnum) {
            case OUTBOUND_A:
                optionalJobQueue = jobQueueService.findOutboundA();
                break;
            case OUTBOUND_B:
                optionalJobQueue = jobQueueService.findOutboundB();
                break;
            case OUTBOUND_C:
                optionalJobQueue = jobQueueService.findOutboundC();
                break;
            case OUTBOUND_D:
                optionalJobQueue = jobQueueService.findOutboundD();
                break;
            case NON_OUTBOUND:
                // A箱上架优先级最高
                optionalJobQueue = jobQueueService.findNonOutbound();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + processorEnum);
        }

        if (!optionalJobQueue.isPresent()) {
            return ProcessResultUtils.failed("JOB队列为空，退出执行");
        }

        JobQueue jobQueue = optionalJobQueue.get();
        omsLogger.debug("获取JOB成功：{}", jobQueue);

        // 如果同一个JOB的执行时间超过配置的超时时间，记录日常，并由其他线程通知WMS
        Date now = DateUtils.getNowDate();
        if (hasJobTimeout(jobQueue, now)) {
            omsLogger.debug("JOB从开始({})到现在({})的分钟差超过允许的：{}", jobQueue.getBeginTime(), now, nodesConfig.getJobTimeout());
            jobQueueService.deleteJobCopyToTimeout(jobQueue);
            return ProcessResultUtils.failed("JOB执行超时，移动到超时表，退出执行");
        }

        WorkflowContext workflowContext = context.getWorkflowContext();
        String jsonString = JSON.toJSONString(jobQueue);
        omsLogger.debug("JOB to JSON：{}", jsonString);
        workflowContext.appendData2WfContext(JobConstants.WORKFLOW_JOB_QUEUE_KEY, jsonString);
        omsLogger.debug("推送JOB到工作流上下文：成功,{}", jobQueue);

        jobQueue.setStatus(JobStatusEnum.WORKFLOW_OCCUPY);
        jobQueue.setBeginTime(now);
        boolean flag = jobQueueService.updateStatusAndBeginTime(jobQueue);
        String msg = StringUtils.format("更新JOB状态为：{}，{}",
                JobStatusEnum.WORKFLOW_OCCUPY, flag ? "成功" : "失败");
        omsLogger.debug(msg);

        return ProcessResultUtils.common(flag, msg);
    }

    /**
     * 校验 workflow中的 是否存在 JobQueue
     */
    @Override
    public ProcessResult validWorkflowData(TaskContext context) {
        OmsLogger omsLogger = context.getOmsLogger();

        omsLogger.debug("从Workflow中获取JOB");
        WorkflowContext workflowContext = context.getWorkflowContext();
        Map<String, String> fetchWorkflowContext = workflowContext.fetchWorkflowContext();
        if (!fetchWorkflowContext.containsKey(JobConstants.WORKFLOW_JOB_QUEUE_KEY)) {
            omsLogger.debug("Workflow_data中({})不存在key：{}", fetchWorkflowContext, JobConstants.WORKFLOW_JOB_QUEUE_KEY);
            return ProcessResultUtils.failed("Workflow不存在JOB，退出执行");
        }
        return ProcessResultUtils.success();
    }

    @Override
    public ProcessResult outboundAllowed(TaskContext context) {
        OmsLogger omsLogger = context.getOmsLogger();

        ProcessResult processResult = validWorkflowData(context);
        if (!processResult.isSuccess()) {
            return processResult;
        }

        Optional<JobQueue> optionalJobQueue = findByWorkflowMap(context);
        if (!optionalJobQueue.isPresent()) {
            return ProcessResultUtils.failed("找不到JOB");
        }

        JobQueue jobQueue = optionalJobQueue.get();

        if (!StringUtils.isBlank(jobQueue.getLocationNameTo())) {
            return ProcessResultUtils.success();
        }

        // 目标库位为空时，调用WMS API 判定库位是否可用
        WmsGlobalResponse wmsGlobalResponse = callWmsService.queryAndFrozenEnableOutbound(jobQueue);
        if (wmsGlobalResponse.hasException() || wmsGlobalResponse.getWmsResponse().isFailed()) {
            omsLogger.debug("呼叫WMS失败：{}", wmsGlobalResponse.hasException()
                    ? wmsGlobalResponse.getMsg()
                    : wmsGlobalResponse.getWmsResponse().getMsg());

            jobQueue.setStatus(JobStatusEnum.NOT_STARTED);
            jobQueueService.updateStatus(jobQueue);

            return ProcessResultUtils.failed();
        }

        String toLocation = wmsGlobalResponse.getWmsResponse().getData().toString();
        jobQueueService.updateLocationNameToById(jobQueue.getId(), toLocation);
        jobQueue.setLocationNameTo(toLocation);

        WorkflowContext workflowContext = context.getWorkflowContext();
        String jsonString = JSON.toJSONString(jobQueue);
        omsLogger.debug("JOB to JSON：{}", jsonString);
        
        workflowContext.appendData2WfContext(JobConstants.WORKFLOW_JOB_QUEUE_KEY, jsonString);
        omsLogger.debug("推送JOB到工作流上下文：成功,{}", jobQueue);

        return ProcessResultUtils.success();
    }

    @Override
    public ProcessResult callAgv(TaskContext context) {
        ProcessResult processResult = validWorkflowData(context);
        if (!processResult.isSuccess()) {
            return processResult;
        }

        Optional<JobQueue> optionalJobQueue = findByWorkflowMap(context);
        if (!optionalJobQueue.isPresent()) {
            return ProcessResultUtils.failed("在工作流上下文内找不到JOB");
        }

        OmsLogger omsLogger = context.getOmsLogger();

        JobQueue jobQueue = optionalJobQueue.get();
        setStatusPrepareCallAgv(jobQueue, omsLogger);

        // 呼叫AGV API
        omsLogger.debug("开始呼叫AGV：{}", jobQueue);
        AgvGlobalResponse agvGlobalResponse = callAgvService.transportOrders(jobQueue);

        if (agvGlobalResponse.hasException()) {
            setStatusCallAgvException(jobQueue, omsLogger);
            callWmsService.syncExceptionMsgOfCallAgv(jobQueue, agvGlobalResponse.getMsg());
            omsLogger.debug("呼叫AGV异常：{}", agvGlobalResponse.getMsg());
            return ProcessResultUtils.failed("呼叫AGV异常：" + agvGlobalResponse.getMsg());
        }

        AgvResponse agvResponse = agvGlobalResponse.getAgvResponse();
        if (AgvResponse.isFailed(agvResponse)) {
            setStatusAgvReturnFailed(jobQueue, omsLogger);
            callWmsService.syncExceptionMsgOfCallAgv(jobQueue, agvResponse.getReason());
            omsLogger.debug("AGV返回失败，原因：{}", agvResponse.getReason());
            return ProcessResultUtils.failed("AGV返回失败，原因：{}" + agvResponse.getReason());
        }

        omsLogger.debug("AGV返回成功，原因：{}", agvResponse.getReason());
        setStatusAgvResultSuccessful(jobQueue, omsLogger);
        // 呼叫AGV成功同步给WMS，由WMS判断是否第一次成功
        callWmsService.syncSuccessfulMsgOfCallAgv(jobQueue, agvResponse.getReason());

        return ProcessResultUtils.success("呼叫AGV成功");
    }

    @Override
    public ProcessResult syncJobTimeout(TaskContext context) {
        OmsLogger omsLogger = context.getOmsLogger();

        omsLogger.debug("查询JOB超时表");
        List<JobTimeout> jobTimeoutList = jobTimeoutService.listBySyncWms();
        if (CollectionUtils.isEmpty(jobTimeoutList)) {
            omsLogger.debug("未找到需要同步的超时JOB信息，退出执行");
            return ProcessResultUtils.success();
        }

        omsLogger.debug("呼叫WMS接口");
        WmsGlobalResponse wmsGlobalResponse = callWmsService.syncTimoutMsg(jobTimeoutList);

        omsLogger.debug("保存同步消息");
        String syncMsg = "同步成功";
        JobFlagSyncWmsEnum jobFlagSyncWmsEnum = JobFlagSyncWmsEnum.SYNCHRONIZATION_SUCCESSFUL;
        if (wmsGlobalResponse.hasException()) {
            syncMsg = wmsGlobalResponse.getMsg();
            jobFlagSyncWmsEnum = JobFlagSyncWmsEnum.SYNCHRONIZATION_FAILED;
        } else {
            WmsResponse wmsResponse = wmsGlobalResponse.getWmsResponse();
            if (wmsResponse.isFailed()) {
                syncMsg = wmsResponse.getMsg();
                jobFlagSyncWmsEnum = JobFlagSyncWmsEnum.SYNCHRONIZATION_FAILED;
            }
        }
        for (JobTimeout jobTimeout : jobTimeoutList) {
            jobTimeout.setSyncMsg(syncMsg);
            jobTimeout.setFlagSyncWms(jobFlagSyncWmsEnum);
        }
        try {
            jobTimeoutService.updateBatchById(jobTimeoutList);
        } catch (Exception e) {
            omsLogger.debug("保存同步消息异常：{}", e);
            return ProcessResultUtils.failed("保存同步消息异常");
        }

        return jobFlagSyncWmsEnum != JobFlagSyncWmsEnum.SYNCHRONIZATION_SUCCESSFUL
                ? ProcessResultUtils.failed(syncMsg)
                : ProcessResultUtils.success(syncMsg);
    }

    private Optional<JobQueue> findByWorkflowMap(TaskContext context) {
        OmsLogger omsLogger = context.getOmsLogger();
        try {
            String jsonString = context.getWorkflowContext().fetchWorkflowContext().get(JobConstants.WORKFLOW_JOB_QUEUE_KEY);
            omsLogger.debug("从工作流上下文取得JSON：{}", jsonString);
            JobQueue jobQueue = JSON.parseObject(jsonString, JobQueue.class);
            if (jobQueue == null) {
                omsLogger.debug("JSON TO BEAN IS NULL");
                return Optional.empty();
            }
            omsLogger.debug("拿到JSON转为JOB后：{}", JSON.toJSONString(jobQueue));
            return Optional.of(jobQueue);
        } catch (Exception e) {
            omsLogger.error("转换JSON 字符串到 JobQueue对象异常：{}", e);
        }
        return Optional.empty();
    }

    private boolean hasJobTimeout(JobQueue jobQueue, Date now) {
        if (nodesConfig.getJobTimeout() <= 0) {
            return false;
        }

        return jobQueue.getBeginTime() != null
                && DateUtils.differenceMinutes(jobQueue.getBeginTime(), now) > nodesConfig.getJobTimeout();
    }

    private void setStatusAgvResultSuccessful(JobQueue jobQueue, OmsLogger omsLogger) {
        setJobStatus(jobQueue, JobStatusEnum.AGV_RETURN_SUCCESSFUL, omsLogger);
    }

    private void setStatusAgvReturnFailed(JobQueue jobQueue, OmsLogger omsLogger) {
        setJobStatus(jobQueue, JobStatusEnum.AGV_RETURN_FAILED, omsLogger);
    }

    private void setStatusCallAgvException(JobQueue jobQueue, OmsLogger omsLogger) {
        setJobStatus(jobQueue, JobStatusEnum.CALL_AGV_EXCEPTION, omsLogger);
    }

    private void setStatusPrepareCallAgv(JobQueue jobQueue, OmsLogger omsLogger) {
        setJobStatus(jobQueue, JobStatusEnum.PREPARE_CALL_AGV, omsLogger);
    }

    private void setJobStatus(JobQueue jobQueue, JobStatusEnum jobStatusEnum, OmsLogger omsLogger) {
        jobQueue.setStatus(jobStatusEnum);
        boolean flag = jobQueueService.updateStatus(jobQueue);
        omsLogger.debug("更新JBO状态为：{}，{}", jobStatusEnum,
                flag ? "成功" : "失败");
    }
}
