package com.railansantana.taskList.records;

import org.bson.types.ObjectId;

import java.time.LocalDate;

public record TaskClientRecord(String task_id, String task_name, Double task_cost, LocalDate task_timeout) {
}
