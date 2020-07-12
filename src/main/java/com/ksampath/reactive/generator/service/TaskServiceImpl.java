package com.ksampath.reactive.generator.service;

import com.ksampath.reactive.generator.domain.Status;
import com.ksampath.reactive.generator.domain.Task;
import com.ksampath.reactive.generator.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TaskServiceImpl<T> implements TaskService<T> {
    private  final TaskRepository<T> repository;
    private  final  IdSupplier supplier;

    @Autowired
    public TaskServiceImpl(TaskRepository<T> repository,IdSupplier idSupplier){
        this.repository = repository;
        this.supplier = idSupplier;
    }

    @Override
    public Mono<Status> findStatus(String id) {
        return repository.findStatusById(id).switchIfEmpty(Mono.empty()).map(Task::getStatus);
    }

    @Override
    public Mono<T> findResult(String id) {
        return repository.findResultById(id).switchIfEmpty(Mono.empty()).map(task -> task.getResult());
    }

    @Override
    public Mono<Task<T>> create(T initial) {
        return supplier.get().map(id-> {
                    final Task<T> task = new Task<>();
                    task.set_id(id);
                    task.setResult(initial);
                    log.info("Generator task is {}",id);
                    task.setStatus(Status.IN_PROGRESS);
                    return  task;
        }).flatMap(repository::save).cache();
    }

    @Override
    public Mono<Task<T>> update(Task<T> task) {
        return repository.findById(task.get_id())
                .flatMap(
                        taskDo -> repository.save(taskDo.update(task))
                );
    }
}
