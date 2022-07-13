package com.nodes.project.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nodes.project.api.domain.JobHistory;
import com.nodes.project.api.mapper.JobHistoryMapper;
import com.nodes.project.api.service.JobHistoryService;
import org.springframework.stereotype.Service;

/**
 * 针对表【job_history】的数据库操作Service实现
 */
@Service
public class JobHistoryServiceImpl extends ServiceImpl<JobHistoryMapper, JobHistory>
        implements JobHistoryService {


}




