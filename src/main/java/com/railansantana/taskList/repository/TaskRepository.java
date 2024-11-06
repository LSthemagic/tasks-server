package com.railansantana.taskList.repository;

import com.railansantana.taskList.domain.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    Optional<Task> findByTaskName(String name);
    List<Task> findAllByOrderByTaskOrderAsc();
    Task findTopByOrderByTaskOrderDesc();
}
