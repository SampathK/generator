package com.ksampath.reactive.generator.service;

import com.ksampath.reactive.generator.domain.Status;
import com.ksampath.reactive.generator.domain.Task;
import reactor.core.publisher.Mono;

public interface TaskService<T> {
    Mono<Status> findStatus(String id);
    Mono<T> findResult(String  id);
    Mono<Task<T>> create(T initial);
    Mono<Task<T>> update(Task<T> task);
}
