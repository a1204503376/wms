package com.nodes.project.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.domain.JobTimeout;
import com.nodes.project.api.dto.wms.WmsGlobalResponse;

/**
 * 针对表【job_timeout】的数据库操作Service
 */
public interface JobTimeoutService extends IService<JobTimeout> {

    WmsGlobalResponse syncToWms(JobQueue jobQueue);
}
