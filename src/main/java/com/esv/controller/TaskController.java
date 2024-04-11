package com.esv.controller;

import com.esv.domain.Task;
import com.esv.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;


@Controller
@RequestMapping("/")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping("/")
    public String tasks(Model model,
                        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                        @RequestParam(value = "limit", required = false, defaultValue = "10") int limit){
        List<Task> taskList = taskService.getAll((page-1)*limit, limit);
        model.addAttribute("tasks", taskList);
        model.addAttribute("current_page", page);
        int totalPage = (int) Math.ceil(1.0 * taskService.getAllCount() / limit);
        if (totalPage > 1){
            List<Integer> pageNumber = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("page_numbers", pageNumber);
        }
        return "tasks";
    }
    @PostMapping("/{id}")
    public String edit(Model model, @PathVariable Integer id, @RequestBody TaskInfo info){
        if(isNull(id) || id <= 0){
            throw new RuntimeException("Invalid id");
        }
        Task task = taskService.edit(id, info.getDescription(), info.getStatus());
        return tasks(model,1,10);
    }
    @PostMapping("/")
    public String  add(Model model, @RequestBody TaskInfo info){
        Task task = taskService.create(info.getDescription(), info.getStatus());
        return tasks(model,1,10);
    }
    @DeleteMapping("/{id}")
    public String delete(Model model, @PathVariable Integer id){
        if(isNull(id) || id <= 0){
            throw new RuntimeException("Invalid id");
        }
        taskService.delete(id);
        return tasks(model,1,10);
    }
}
