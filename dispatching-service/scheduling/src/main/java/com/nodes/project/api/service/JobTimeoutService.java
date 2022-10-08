package com.nodes.project.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nodes.project.api.domain.JobTimeout;

import java.util.List;

public interface JobTimeoutService extends IService<JobTimeout> {

    List<JobTimeout> listBySyncWms();
}
