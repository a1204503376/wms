package com.nodes.project.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nodes.project.api.domain.SyncWtsExceptionLog;
import com.nodes.project.api.dto.wms.WmsSyncTaskStateRequest;
import com.nodes.project.api.mapper.SyncWtsExceptionLogMapper;
import com.nodes.project.api.service.SyncWtsExceptionLogService;
import org.springframework.stereotype.Component;

/**
 * 同步wms任务状态异常日志 业务实现类
 **/
@Component
public class SyncWtsExceptionLogServiceImpl extends ServiceImpl<SyncWtsExceptionLogMapper, SyncWtsExceptionLog> implements SyncWtsExceptionLogService {

    @Override
    public boolean save(WmsSyncTaskStateRequest request) {
        SyncWtsExceptionLog log = new SyncWtsExceptionLog();
        log.setState(request.getState());
        log.setTaskDetailId(request.getTaskDetailId());
        log.setTaskHeaderId(request.getTaskHeaderId());
        log.setMsg(request.getMsg());
        return save(log);
    }

    @Override
    public SyncWtsExceptionLog getOneOrderCreateTime() {
        LambdaQueryWrapper<SyncWtsExceptionLog> queryWrapper = Wrappers.lambdaQuery(SyncWtsExceptionLog.class);
        queryWrapper.orderByDesc(SyncWtsExceptionLog::getCreateTime)
                .last("limit 1");
        return getOne(queryWrapper);
    }

    @Override
    public boolean remove(Long logId) {
        return removeById(logId);
    }

    @Override
    public SyncWtsExceptionLog getByTaskId(Long wmsTaskId) {
        LambdaQueryWrapper<SyncWtsExceptionLog> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SyncWtsExceptionLog::getTaskDetailId, wmsTaskId);
        return getOne(queryWrapper);
    }
}
