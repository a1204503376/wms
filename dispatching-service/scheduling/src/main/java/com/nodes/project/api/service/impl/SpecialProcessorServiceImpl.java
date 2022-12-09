package com.nodes.project.api.service.impl;

import com.nodes.processor.ProcessResultUtils;
import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.domain.SyncWtsExceptionLog;
import com.nodes.project.api.service.CallWmsService;
import com.nodes.project.api.service.SpecialProcessorService;
import com.nodes.project.api.service.SyncWtsExceptionLogService;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.log.OmsLogger;

import javax.annotation.Resource;

/**
 * 特殊处理器业务实现
 **/
@Component
public class SpecialProcessorServiceImpl implements SpecialProcessorService {

    @Resource
    private SyncWtsExceptionLogService service;
    @Resource
    private CallWmsService callWmsService;

    @Override
    public ProcessResult syncWmsTaskState(TaskContext context) {
        OmsLogger omsLogger = context.getOmsLogger();
        // 每次获取一条数据
        omsLogger.info("开始获取异常日志");
        SyncWtsExceptionLog exLog = service.getOneOrderCreateTime();
        omsLogger.info("获取到异常日志{}", exLog);
        if (exLog != null){
            // 重发wms
            JobQueue jobQueue = new JobQueue();
            jobQueue.setWmsTaskId(exLog.getTaskHeaderId());
            jobQueue.setWmsTaskDetailId(exLog.getTaskDetailId());
            jobQueue.setStatus(exLog.getState());
            omsLogger.debug("开始重新同步wms任务状态{}");
            boolean syncSuccess = callWmsService.syncFailedMsgToWms(jobQueue, exLog.getMsg());

            // 同步wms任务状态成功，删除该日志数据
            if (syncSuccess) {
                boolean remove = service.remove(exLog.getId());
            }
        }
        return ProcessResultUtils.success("同步wms任务状态成功");
    }
}
