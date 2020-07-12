package com.ksampath.reactive.generator.controller;

import com.ksampath.reactive.generator.command.GeneratorForm;
import com.ksampath.reactive.generator.domain.Status;
import com.ksampath.reactive.generator.domain.Task;
import com.ksampath.reactive.generator.service.GeneratorService;
import com.ksampath.reactive.generator.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class GeneratorController {
    private final GeneratorService generator;
    private final TaskService<List<Integer>> taskService;

    @Autowired
    public GeneratorController(GeneratorService generator, TaskService<List<Integer>> taskService) {
        this.generator = generator;
        this.taskService = taskService;
    }

    @PostMapping("/generate")
    public Mono<ResponseEntity<Task<List<Integer>>>> generate(@RequestBody GeneratorForm form) {
        Mono<Task<List<Integer>>> task = taskService.create(Collections.emptyList());
        generator.generate(task,form);
        return task.map(t-> new ResponseEntity(t, HttpStatus.ACCEPTED));
    }

    @GetMapping(path = "/tasks/{id}/status")
    public Mono<ResponseEntity<Status>> status(@PathVariable String id){
        return taskService.findStatus(id).map(status -> new ResponseEntity<>(status,HttpStatus.OK)).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/tasks/{id}/result")
    public Mono<ResponseEntity<Result>> result(@PathVariable String id,@RequestParam ("action") String action){
        if("get_numlist".equalsIgnoreCase(action)) {
            return taskService.findResult(id).map(result -> new ResponseEntity<>(new Result(result), HttpStatus.OK)).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

}
