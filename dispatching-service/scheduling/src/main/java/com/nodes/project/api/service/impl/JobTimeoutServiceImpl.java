package com.nodes.project.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nodes.project.api.domain.JobQueue;
import com.nodes.project.api.domain.JobTimeout;
import com.nodes.project.api.dto.wms.WmsGlobalResponse;
import com.nodes.project.api.mapper.JobTimeoutMapper;
import com.nodes.project.api.service.JobTimeoutService;
import org.springframework.stereotype.Service;

/**
 * 针对表【job_timeout】的数据库操作Service实现
 */
@Service
public class JobTimeoutServiceImpl extends ServiceImpl<JobTimeoutMapper, JobTimeout>
        implements JobTimeoutService {

    @Override
    public WmsGlobalResponse syncToWms(JobQueue jobQueue) {

        return null;
    }
}




