package com.nodes.project.api.service;

import com.nodes.project.api.domain.SyncWtsExceptionLog;
import com.nodes.project.api.dto.wms.WmsSyncTaskStateRequest;

/**
 * 同步wms任务状态异常日志接口
 **/
public interface SyncWtsExceptionLogService {

    /**
     * 保存 同步wms任务状态时的异常日志
     *
     * @param request 同步wms任务状态请求对象
     * @return true: 保存成功, false: 保存失败
     */
    boolean save(WmsSyncTaskStateRequest request);

    /**
     * 获取一个创建时间最长的异常日志
     *
     * @return 异常日志对象
     */
    SyncWtsExceptionLog getOneOrderCreateTime();

    /**
     * 根据日志id 删除异常日志
     *
     * @param logId 日志id
     * @return true: 保存成功, false: 保存失败
     */
    boolean remove(Long logId);

    /**
     * 根据任务id 获取异常日志
     *
     * @param wmsTaskId 任务id
     * @return 同步wms任务状态 异常日志
     */
    SyncWtsExceptionLog getByTaskId(Long wmsTaskId);
}
