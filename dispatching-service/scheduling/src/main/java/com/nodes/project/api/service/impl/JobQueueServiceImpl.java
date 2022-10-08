package com.nodes.project.api.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nodes.common.constant.JobConstants;
import com.nodes.common.exception.ServiceException;
import com.nodes.common.utils.StringUtils;
import com.nodes.framework.web.domain.AjaxResult;
import com.nodes.framework.web.domain.SimpleEntity;
import com.nodes.project.api.domain.JobHistory;
import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.domain.JobTimeout;
import com.nodes.project.api.dto.JobActionRequest;
import com.nodes.project.api.dto.PublishJobRequest;
import com.nodes.project.api.dto.agv.AgvGlobalResponse;
import com.nodes.project.api.dto.agv.AgvResponse;
import com.nodes.project.api.enums.JobFlagSyncWmsEnum;
import com.nodes.project.api.enums.JobStatusEnum;
import com.nodes.project.api.enums.JobTypeEnum;
import com.nodes.project.api.enums.WmsBoxTypeEnum;
import com.nodes.project.api.mapper.JobQueueMapper;
import com.nodes.project.api.service.CallAgvService;
import com.nodes.project.api.service.JobHistoryService;
import com.nodes.project.api.service.JobQueueService;
import com.nodes.project.api.service.JobTimeoutService;
import com.nodes.project.system.dict.utils.DictUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 针对表【job_queue(作业队列
 * 存放待处理的作业，处理完成移动到作业历史表)】的数据库操作Service实现
 */
@Slf4j
@Service
public class JobQueueServiceImpl extends ServiceImpl<JobQueueMapper, JobQueue>
        implements JobQueueService {

    @Resource
    private JobHistoryService jobHistoryService;
    @Resource
    private JobTimeoutService jobTimeoutService;
    @Resource
    private CallAgvService callAgvService;
    /**
     * 注入自己的代理类对象
     * 方便调用自身的事务方法
     */
    @Resource
    private JobQueueService jobQueueService;

    @Override
    public AjaxResult publishJob(List<PublishJobRequest> publishJobRequestList) {

        List<JobQueue> jobQueueList = new ArrayList<>();

        Map<String, String> schedulingJobPriorityMap = DictUtils.getSchedulingJobPriorityMap();
        Map<String, String> schedulingJobTypeMap = DictUtils.getSchedulingJobTypeMap();

        JobQueue jobQueue;
        for (PublishJobRequest publishJobRequest : publishJobRequestList) {

            jobQueue = new JobQueue();
            BeanUtils.copyProperties(publishJobRequest, jobQueue);

            if (ObjectUtils.isNotEmpty(publishJobRequest.getBoxCode())) {
                String boxCodePre = publishJobRequest.getBoxCode().substring(0, 1).toUpperCase();
                jobQueue.setWmsBoxType(WmsBoxTypeEnum.getByKey(boxCodePre));
            }

            // 2.优先级默认根据类型确定
            setPriority(schedulingJobPriorityMap, jobQueue, publishJobRequest);
            jobQueue.setTypeCode(JobTypeEnum.getByKey(publishJobRequest.getJobTypeCode()));
            jobQueue.setName(schedulingJobTypeMap.get(publishJobRequest.getJobTypeCode()));
            jobQueue.setStatus(JobStatusEnum.NOT_STARTED);
            jobQueue.setWmsCBifurcate(publishJobRequest.getCBifurcate());

            jobQueueList.add(jobQueue);
        }

        return saveOrUpdateBatch(jobQueueList)
                ? AjaxResult.success()
                : AjaxResult.warn("批量保存JOB失败");
    }

    private static void setPriority(Map<String, String> schedulingJobPriorityMap, JobQueue jobQueue, PublishJobRequest publishJobRequest) {
        int priority = JobConstants.DEFAULT_PRIORITY;
        if (schedulingJobPriorityMap.containsKey(publishJobRequest.getJobTypeCode())) {
            priority = NumberUtils.toInt(schedulingJobPriorityMap.get(publishJobRequest.getJobTypeCode()), priority);
        }
        // A箱上架优先级最高
        if (jobQueue.getWmsBoxType() == WmsBoxTypeEnum.A) {
            priority -= 10;
        }
        jobQueue.setPriority(priority);
    }

    @Override
    public AjaxResult continueJob(JobActionRequest jobActionRequest) {
        JobQueue jobQueue = findByWmsTaskId(jobActionRequest);
        // 只允许【AGV异常中断】的JOB 发起继续执行
        if (jobQueue.getStatus() != JobStatusEnum.AGV_EXCEPTION) {
            return AjaxResult.error(validStatusErrorMsg(jobQueue, jobActionRequest, "继续执行"));
        }

        // 更新JOB状态为：继续执行，优先级为：10
        jobQueue.setStatus(JobStatusEnum.CONTINUE_EXECUTE);
        jobQueue.setPriority(JobConstants.CONTINUE_JOB_PRIORITY);

        return updateStatus(jobQueue)
                ? AjaxResult.success()
                : AjaxResult.error(updateStatusErrorMsg(jobQueue));
    }

    @Override
    public AjaxResult cancelJob(JobActionRequest jobActionRequest) {
        JobQueue jobQueue = findByWmsTaskId(jobActionRequest);
        // 只允许【未开始】,【呼叫AGV成功】被取消
        boolean flagAgvReturnSuccessful = jobQueue.getStatus() == JobStatusEnum.AGV_RETURN_SUCCESSFUL;
        if (jobQueue.getStatus() != JobStatusEnum.NOT_STARTED
                && !flagAgvReturnSuccessful) {
            return AjaxResult.error(validStatusErrorMsg(jobQueue, jobActionRequest, "取消"));
        }
        if (flagAgvReturnSuccessful) {
            AgvGlobalResponse agvGlobalResponse = callAgvService.withdrawal(jobQueue);
            if (AgvGlobalResponse.isException(agvGlobalResponse)) {
                return AjaxResult.errorParams("呼叫AGV异常：{}", agvGlobalResponse.getMsg());
            }
            AgvResponse agvResponse = agvGlobalResponse.getAgvResponse();
            if (AgvResponse.isFailed(agvResponse)) {
                return AjaxResult.errorParams("AGV返回失败，原因：{}", agvResponse.getReason());
            }
        }

        jobQueue.setStatus(JobStatusEnum.CANCEL);
        jobQueueService.deleteJobCopyToHistory(jobQueue);

        return AjaxResult.success();
    }

    @Override
    public AjaxResult terminationJob(JobActionRequest jobActionRequest) {
        JobQueue jobQueue = findByWmsTaskId(jobActionRequest);
        // 只允许【AGV异常中断】的JOB被终止
        if (jobQueue.getStatus() != JobStatusEnum.AGV_EXCEPTION) {
            return AjaxResult.error(validStatusErrorMsg(jobQueue, jobActionRequest, "终止"));
        }

        jobQueue.setStatus(JobStatusEnum.END_EXCEPTION);
        jobQueueService.deleteJobCopyToHistory(jobQueue);

        return AjaxResult.success();
    }

    /**
     * 将当前JOB移出队列表，放入JOB历史表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobCopyToHistory(JobQueue jobQueue) {
        JobHistory jobHistory = new JobHistory();
        BeanUtils.copyProperties(jobQueue, jobHistory);

        boolean flag = removeById(jobQueue);
        if (!flag) {
            throw new ServiceException(StringUtils.format("删除JOB队列失败，参数：{}", jobQueue));
        }
        flag = jobHistoryService.save(jobHistory);
        if (!flag) {
            throw new ServiceException(StringUtils.format("保存JOB到历史表失败，参数：{}", jobQueue));
        }
    }

    /**
     * 批量将已结束的JOB移动到历史表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteJobCopyToHistory() {
        List<JobQueue> jobQueueList = super.lambdaQuery()
                .eq(JobQueue::getStatus, JobStatusEnum.AGV_END)
                .list();
        List<JobHistory> jobHistoryList = new ArrayList<>();
        for (JobQueue jobQueue : jobQueueList) {
            JobHistory jobHistory = new JobHistory();
            BeanUtils.copyProperties(jobQueue, jobHistory);
            jobHistoryList.add(jobHistory);
        }
        jobHistoryService.saveBatch(jobHistoryList);
        removeByIds(jobQueueList);
    }

    /**
     * 将当前JOB移出队列表，放入JOB 超时表
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteJobCopyToTimeout(JobQueue jobQueue) {
        JobTimeout jobTimeout = new JobTimeout();
        jobTimeout.setFlagSyncWms(JobFlagSyncWmsEnum.NOT_SYNCHRONIZED);
        BeanUtils.copyProperties(jobQueue, jobTimeout);

        if (removeById(jobQueue)) {
            jobTimeoutService.save(jobTimeout);
        }
    }

    @Override
    public Optional<JobQueue> findNonOutbound() {
        return setOnlyWrapper(getWrapperForStatus()
                .ne(JobQueue::getTypeCode, JobTypeEnum.OUTBOUND))
                .oneOpt();
    }

    private LambdaQueryChainWrapper<JobQueue> getWrapperForStandard() {
        return super.lambdaQuery()
                .orderByAsc(JobQueue::getPriority)
                .orderByAsc(SimpleEntity::getCreateTime);
    }

    private LambdaQueryChainWrapper<JobQueue> getWrapperForStatus(List<JobStatusEnum> jobStatusEnumList) {
        return getWrapperForStandard()
                .in(JobQueue::getStatus, jobStatusEnumList);
    }

    private LambdaQueryChainWrapper<JobQueue> getWrapperForStatus() {
        return getWrapperForStatus(JobStatusEnum.listByAllow());
    }

    @Override
    public Optional<JobQueue> findOutboundA() {
        return findOutbound(WmsBoxTypeEnum.A);
    }

    @Override
    public Optional<JobQueue> findOutboundB() {
        return findOutbound(WmsBoxTypeEnum.B);
    }

    @Override
    public Optional<JobQueue> findOutboundC() {
        return findOutbound(WmsBoxTypeEnum.C);
    }

    @Override
    public Optional<JobQueue> findOutboundD() {
        return findOutbound(WmsBoxTypeEnum.D);
    }


    private Optional<JobQueue> findOutbound(WmsBoxTypeEnum wmsBoxTypeEnum) {
        return setOnlyWrapper(getWrapperForStatus()
                .eq(JobQueue::getTypeCode, JobTypeEnum.OUTBOUND)
                .eq(JobQueue::getWmsBoxType, wmsBoxTypeEnum))
                .oneOpt();
    }


    @Override
    public boolean updateStatus(JobQueue jobQueue) {
        return super.lambdaUpdate()
                .set(JobQueue::getStatus, jobQueue.getStatus())
                .eq(JobQueue::getId, jobQueue.getId())
                .update();
    }

    @Override
    public boolean updateStatusAndBeginTime(JobQueue jobQueue) {
        return super.lambdaUpdate()
                .set(JobQueue::getStatus, jobQueue.getStatus())
                .set(JobQueue::getBeginTime, jobQueue.getBeginTime())
                .eq(JobQueue::getId, jobQueue.getId())
                .update();
    }

    @Override
    public void updateLocationNameToById(String id, String locationNameTo) {
        if (ObjectUtils.isEmpty(id)) {
            throw new ServiceException("更新任务目标库位失败,任务ID为空");
        }
        if (ObjectUtils.isEmpty(locationNameTo)) {
            throw new ServiceException("更新任务目标库位失败,目标库位为空");
        }
        LambdaUpdateChainWrapper<JobQueue> wrapper = super.lambdaUpdate()
                .set(JobQueue::getLocationNameTo, locationNameTo)
                .eq(JobQueue::getId, id);
        if (!wrapper.update()) {
            throw new ServiceException("更新任务目标库位失败");
        }
    }

    private String validStatusErrorMsg(JobQueue jobQueue, JobActionRequest jobActionRequest, String validMsg) {
        return StringUtils.format("当前JOB状态为：{},不允许{}，参数：{}",
                jobQueue.getStatus().getDesc(), validMsg, jobActionRequest);
    }

    private String updateStatusErrorMsg(JobQueue jobQueue) {
        return StringUtils.format("更新JOB状态失败，参数：{}", jobQueue);
    }

    /**
     * 根据WMS任务ID查询唯一JOB
     */
    private JobQueue findByWmsTaskId(JobActionRequest jobActionRequest) {
        Optional<JobQueue> optionalJobQueue = super.lambdaQuery()
                .eq(JobQueue::getWmsTaskId, jobActionRequest.getWmsTaskId())
                .eq(JobQueue::getWmsTaskDetailId, jobActionRequest.getWmsTaskDetailId())
                .oneOpt();
        if (!optionalJobQueue.isPresent()) {
            throw new ServiceException(nullJobErrorMsg(jobActionRequest));
        }
        return optionalJobQueue.get();
    }

    private String nullJobErrorMsg(Object object) {
        return StringUtils.format("查询JOB队列表为空，参数：{}", object);
    }

}




