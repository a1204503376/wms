package com.nodes.job.executor.service;

import com.nodes.job.executor.pojo.entity.Task;

import java.util.List;

/**
 * 任务服务类
 * @author 邓茂林
 */
public interface TaskService {

    Task addTask(Task task);

    void removeTask(Long id);

    Task revisedTask(Task task);

    List<Task> getAllTask();

    Task getTaskById(Long id);

    Task findByName(String name);
}
