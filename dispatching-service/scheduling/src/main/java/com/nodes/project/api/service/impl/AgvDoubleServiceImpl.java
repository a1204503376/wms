package com.nodes.project.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nodes.common.utils.bean.BeanUtils;
import com.nodes.project.api.domain.AgvDouble;
import com.nodes.project.api.dto.agv.AgvSyncOrderRequest;
import com.nodes.project.api.mapper.AgvDoubleMapper;
import com.nodes.project.api.service.AgvDoubleService;

import java.util.Optional;

/**
 *
 */
public class AgvDoubleServiceImpl extends ServiceImpl<AgvDoubleMapper, AgvDouble>
        implements AgvDoubleService {

    @Override
    public boolean existsJob(String jobId) {
        Optional<AgvDouble> optional = super.lambdaQuery()
                .eq(AgvDouble::getJobId, jobId)
                .oneOpt();
        return optional.isPresent();
    }

    @Override
    public void saveJob(AgvSyncOrderRequest agvSyncOrderRequest) {
        AgvDouble agvDouble = new AgvDouble();
        BeanUtils.copyProperties(agvSyncOrderRequest, agvDouble);
        super.save(agvDouble);
    }
}
