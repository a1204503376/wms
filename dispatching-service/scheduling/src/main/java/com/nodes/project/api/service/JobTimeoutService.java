package com.nodes.project.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nodes.project.api.domain.JobTimeout;

import java.util.List;

/**
 * 针对表【job_timeout】的数据库操作Service
 */
public interface JobTimeoutService extends IService<JobTimeout> {

    List<JobTimeout> listBySyncWms();
}
