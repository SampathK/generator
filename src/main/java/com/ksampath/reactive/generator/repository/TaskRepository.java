package com.ksampath.reactive.generator.repository;

import com.ksampath.reactive.generator.domain.Task;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TaskRepository<T> extends ReactiveMongoRepository<Task<T>,String> {
    @Query(value="{ _id : ?0}", fields="{ status : 1 }")
    Mono<Task<T>> findStatusById(String id);
    @Query(value="{ _id : ?0}", fields="{ result : 1 }")
    Mono<Task<T>> findResultById(String id);
}
