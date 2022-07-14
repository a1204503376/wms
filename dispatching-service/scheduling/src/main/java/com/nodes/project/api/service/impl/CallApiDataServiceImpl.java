package com.nodes.project.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nodes.common.utils.DateUtils;
import com.nodes.common.utils.ExceptionUtil;
import com.nodes.project.api.domain.CallApiLog;
import com.nodes.project.api.mapper.CallApiLogMapper;
import com.nodes.project.api.service.CallApiDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.powerjob.common.serialize.JsonUtils;

@Slf4j
@Service
public class CallApiDataServiceImpl
        extends ServiceImpl<CallApiLogMapper, CallApiLog>
        implements CallApiDataService {

    @Async
    @Override
    public void saveCallApiLog(String url, Object request, Object response, Exception ex) {
        try {
            CallApiLog callApiLog = new CallApiLog();
            callApiLog.setUrl(url);
            callApiLog.setRequestData(JsonUtils.toJSONString(request));
            callApiLog.setResponseData(JsonUtils.toJSONString(response));
            callApiLog.setExceptionMsg(ExceptionUtil.getExceptionMessage(ex));
            callApiLog.setCreateTime(DateUtils.getNowDate());
            save(callApiLog);
        } catch (Exception e) {
            log.error("异步保存CALL API LOG 异常", e);
        }
    }

}