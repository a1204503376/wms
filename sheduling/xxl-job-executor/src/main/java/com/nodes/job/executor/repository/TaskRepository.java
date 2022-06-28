package com.nodes.job.executor.repository;

import com.nodes.job.executor.pojo.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

/**
 * 任务仓储
 */
public interface TaskRepository extends CrudRepository<Task,Long> {

    /**
     * 根据名称查询任务
     * @return Task
     */
    Task findByName(String name);
}
