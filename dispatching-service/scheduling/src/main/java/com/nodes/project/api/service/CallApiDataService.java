package com.nodes.project.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nodes.project.api.domain.CallApiLog;
import org.springframework.scheduling.annotation.Async;

public interface CallApiDataService extends IService<CallApiLog> {

    @Async
    void saveCallApiLog(String url, Object request, Object response, Exception ex);
}
