package com.nodes.job.executor.mvc.controller;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
@Data
public class TaskDto implements Serializable {
    private final Long id;
    private final String name;
    private final Long objectVersion;
    private final String createdBy;
    private final Date createdDate;
    private final String lastUpdatedBy;
    private final Date lastUpdatedDate;
}
