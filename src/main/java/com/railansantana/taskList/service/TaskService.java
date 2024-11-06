package com.railansantana.taskList.service;

import com.railansantana.taskList.domain.Task;
import com.railansantana.taskList.records.TaskClientRecord;
import com.railansantana.taskList.records.TaskCreateRecord;
import com.railansantana.taskList.records.TaskReorderRecord;
import com.railansantana.taskList.records.TaskUpdateRecord;
import com.railansantana.taskList.repository.TaskRepository;
import com.railansantana.taskList.service.exceptions.AlreadyExistsException;
import com.railansantana.taskList.service.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<TaskClientRecord> findAll() {
        List<Task> tasks = taskRepository.findAllByOrderByTaskOrderAsc();

        return tasks.stream()
                .map(task -> new TaskClientRecord(task.getTaskId(), task.getTaskName(), task.getTaskCost(), task.getTaskTimeout()))
                .collect(Collectors.toList());
    }

    private Task findById(String id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            throw new ResourceNotFoundException("Task Not found");
        }
        return task;
    }

    public void delete(String id) {
        taskRepository.delete(findById(id));
    }

    public void update(String id, TaskUpdateRecord task_now) {
        Task task = findById(id);
        validationNameTask(task_now.task_name(), id);
        updateData(task, task_now);
        taskRepository.save(task);
    }

    private void updateData(Task task, TaskUpdateRecord taskUpdateRecord) {
        task.setTaskName(taskUpdateRecord.task_name());
        task.setTaskCost(taskUpdateRecord.task_cost());
        task.setTaskTimeout(taskUpdateRecord.task_timeout());
    }

    public TaskCreateRecord insert(Task task) {
        validationNameTask(task.getTaskName(), task.getTaskId());
        setTaskOrder(task);
        taskRepository.save(task);
        return new TaskCreateRecord(task.getTaskId(), task.getTaskName(), task.getTaskCost(), task.getTaskTimeout());
    }

    private void setTaskOrder(Task task) {
        Task maxOrderTask = taskRepository.findTopByOrderByTaskOrderDesc();
        int newOrder = (maxOrderTask != null) ? maxOrderTask.getTaskOrder() + 1 : 1;
        task.setTaskOrder(newOrder);
    }

    private void validationNameTask(String name, String id) {
        Optional<Task> existingTask = taskRepository.findByTaskName(name);
        if (existingTask.isPresent() && !existingTask.get().getTaskId().equals(id)) {
            throw new AlreadyExistsException("Nome de task já existe");
        }
    }
    @Transactional
    public void reorderTasks(List<TaskReorderRecord> taskIds) {
        for (int i = 0; i < taskIds.size(); i++) {
            TaskReorderRecord taskIdRecord = taskIds.get(i);
            Task task = findById(taskIdRecord.tasks_id());
            task.setTaskOrder(i + 1); // Ajusta a ordem com base na posição na lista
            taskRepository.save(task);
        }
    }
}
