package com.nodes.project.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nodes.framework.web.domain.SimpleEntity;
import com.nodes.project.api.domain.JobTimeout;
import com.nodes.project.api.enums.JobFlagSyncWmsEnum;
import com.nodes.project.api.mapper.JobTimeoutMapper;
import com.nodes.project.api.service.JobTimeoutService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 针对表【job_timeout】的数据库操作Service实现
 */
@Service
public class JobTimeoutServiceImpl extends ServiceImpl<JobTimeoutMapper, JobTimeout>
        implements JobTimeoutService {

    @Override
    public List<JobTimeout> listBySyncWms() {
        return super.lambdaQuery()
                .in(JobTimeout::getFlagSyncWms, JobFlagSyncWmsEnum.NOT_SYNCHRONIZED, JobFlagSyncWmsEnum.SYNCHRONIZATION_FAILED)
                .orderByAsc(SimpleEntity::getCreateTime)
                .list();
    }

}




