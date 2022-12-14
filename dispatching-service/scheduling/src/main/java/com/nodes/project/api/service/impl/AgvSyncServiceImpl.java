package com.nodes.project.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.interfaces.Func;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.nodes.common.exception.ServiceException;
import com.nodes.common.utils.StringUtils;
import com.nodes.framework.web.domain.AjaxResult;
import com.nodes.project.api.domain.AgvSync;
import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.dto.agv.AgvSyncOrderRequest;
import com.nodes.project.api.enums.AgvTypeEnum;
import com.nodes.project.api.enums.JobStatusEnum;
import com.nodes.project.api.events.AlreadyStorageEvent;
import com.nodes.project.api.mapper.AgvSyncMapper;
import com.nodes.project.api.mapper.JobQueueMapper;
import com.nodes.project.api.service.AgvDoubleService;
import com.nodes.project.api.service.AgvSyncService;
import com.nodes.project.api.service.CallWmsService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 针对表【agv_sync】的数据库操作Service实现
 */
@Service
public class AgvSyncServiceImpl extends ServiceImpl<AgvSyncMapper, AgvSync>
        implements AgvSyncService {
    @Resource
    private CallWmsService callWmsService;
    @Resource
    private JobQueueMapper jobQueueMapper;
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private AgvDoubleService agvDoubleService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult syncOrder(AgvSyncOrderRequest agvSyncOrderRequest) {
        JobQueue jobQueue = jobQueueMapper.selectById(agvSyncOrderRequest.getJobId());
        if (ObjectUtils.isEmpty(jobQueue)) {
            throw new ServiceException(StringUtils.format("JOB为空，参数：{}", agvSyncOrderRequest));
        }

        boolean flag;
        AgvTypeEnum agvType = agvSyncOrderRequest.getAgvType();
        switch (agvType) {
            case BEGIN:
                jobQueue.setStatus(JobStatusEnum.AGV_BEGIN);
                break;
            case DISTRIBUTION:
                jobQueue.setStatus(JobStatusEnum.AGV_ASSIGNED_CAR);
                break;
            case END:
                jobQueue.setStatus(JobStatusEnum.AGV_END);
                break;
            case EXCEPTION:
                jobQueue.setStatus(JobStatusEnum.AGV_EXCEPTION);
                AgvSync agvSync = new AgvSync();
                BeanUtils.copyProperties(agvSyncOrderRequest, agvSync);
                agvSync.setAgvType(agvType.getDesc());
                if (ObjectUtils.isEmpty(agvSync.getMsg())) {
                    agvSync.setMsg(agvSyncOrderRequest.getExceptionMsg());
                }
                flag = save(agvSync);
                if (!flag) {
                    throw new ServiceException(StringUtils.format("保存AGV_SYNC失败，参数：{}", agvSync));
                }
                break;
            case MANUAL_TERMINATION:
                jobQueue.setStatus(JobStatusEnum.CANCEL);
                break;
            case DOUBLE_WAREHOUSING:
                // AGV方可能重复发送
                boolean existsJobFlag = agvDoubleService.existsJob(jobQueue.getId());
                if (!existsJobFlag) {
                    agvDoubleService.saveJob(agvSyncOrderRequest);
                    AlreadyStorageEvent alreadyStorageEvent = new AlreadyStorageEvent(this, agvSyncOrderRequest);
                    applicationContext.publishEvent(alreadyStorageEvent);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + agvType);
        }

        if (agvType != AgvTypeEnum.DOUBLE_WAREHOUSING) {
            // 调度系统向WMS系统发送“AGV已分配任务给车辆”通知时需要新增agvName（用msg字段）
            String msg = agvType == AgvTypeEnum.DISTRIBUTION ? agvSyncOrderRequest.getAgvName() : agvSyncOrderRequest.getMsg();
            if (ObjectUtils.isEmpty(agvSyncOrderRequest.getMsg())) {
                msg = agvType == AgvTypeEnum.DISTRIBUTION ? agvSyncOrderRequest.getAgvName() : agvSyncOrderRequest.getExceptionMsg();
            }
            callWmsService.syncTaskState(jobQueue, msg);
        }

        flag = SqlHelper.retBool(jobQueueMapper.updateById(jobQueue));
        if (!flag) {
            throw new ServiceException(StringUtils.format("更新JOB失败，参数：{}", jobQueue));
        }

        return AjaxResult.success();
    }
}




