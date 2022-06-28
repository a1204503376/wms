package com.nodes.job.executor.service.impl;

import com.nodes.job.executor.pojo.entity.Task;
import com.nodes.job.executor.repository.TaskRepository;
import com.nodes.job.executor.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task addTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void removeTask(Long id) {
        taskRepository.deleteAllById(Collections.singleton(id));
    }

    @Override
    public Task revisedTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTask() {
        return (List<Task>) taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public Task findByName(String name) {
        return taskRepository.findByName(name);
    }
}
