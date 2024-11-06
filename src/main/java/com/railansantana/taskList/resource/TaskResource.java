package com.railansantana.taskList.resource;

import com.railansantana.taskList.domain.Task;
import com.railansantana.taskList.records.TaskClientRecord;
import com.railansantana.taskList.records.TaskCreateRecord;
import com.railansantana.taskList.records.TaskReorderRecord;
import com.railansantana.taskList.records.TaskUpdateRecord;
import com.railansantana.taskList.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskResource {
    @Autowired
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskClientRecord>> findAll(){
        return ResponseEntity.ok().body(taskService.findAll());
    }

    @PostMapping
    public ResponseEntity<Void> createTask(@RequestBody TaskUpdateRecord task){
        Task task_now = new Task(null, task.task_name(), task.task_cost(), task.task_timeout(), null);
        TaskCreateRecord res = taskService.insert(task_now);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(res.task_id()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Void> updateTask(@PathVariable String taskId, @RequestBody TaskUpdateRecord taskUpdateRecord){
        taskService.update(taskId, taskUpdateRecord);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reorder")
    public ResponseEntity<Void> reorderTasks(@RequestBody List<TaskReorderRecord> taskIds) {
        taskService.reorderTasks(taskIds);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId){
        taskService.delete(taskId);
        return ResponseEntity.noContent().build();
    }
}
