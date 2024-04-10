package com.esv.service;

import com.esv.dao.TaskDAO;
import com.esv.domain.Task;
import com.esv.domain.Status;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class TaskService {
    private final TaskDAO taskDAO;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }
    public List<Task> getAll(int offset, int limit){
        return taskDAO.getAll(offset, limit);
    }
    public int getAllCount(){
        return taskDAO.getAllCount();
    }
    @Transactional
    public Task edit(int id, String description, Status status){
        Task task = taskDAO.getById(id);
        if(isNull(task)){
            throw new RuntimeException("Not found");
        }
        task.setDescription(description);
        task.setStatus(status);
        taskDAO.saveOrUpdate(task);
        return task;
    }
    public Task create(String description, Status status){
        Task task = new Task();
        task.setDescription(description);
        task.setStatus(status);
        taskDAO.saveOrUpdate(task);
        return task;
    }
    @Transactional
    public void delete(int id){
        Task task = taskDAO.getById(id);
        if(isNull(task)){
            throw new RuntimeException("Not found");
        }
        taskDAO.delete(task);
    }
}
