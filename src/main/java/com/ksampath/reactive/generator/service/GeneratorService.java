package com.ksampath.reactive.generator.service;

import com.ksampath.reactive.generator.command.GeneratorForm;
import com.ksampath.reactive.generator.domain.Status;
import com.ksampath.reactive.generator.domain.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@Slf4j
public class GeneratorService {

    private final Scheduler scheduler = Schedulers.newElastic("generator-thread");
    private final TaskService<List<Integer>> service;
    private final AlgoGenerator algo;

    @Autowired
    public GeneratorService(TaskService<List<Integer>> service, AlgoGenerator algo) {
        this.service = service;
        this.algo = algo;
    }

    public void generate(Mono<Task<List<Integer>>> task, GeneratorForm form) {
         Mono.fromSupplier(() -> {
             try {
                 Thread.sleep(1000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             return task.flatMap(t -> {
                if (form.getStep() <= 0 || Integer.compare(form.getGoal(), form.getStep()) < 0 || form.getGoal() % form.getStep() != 0) {
                    t.setStatus(Status.ERROR);
                } else {
                    algo.apply(form).subscribe(result -> {
                        t.setResult(result);
                        t.setStatus(Status.SUCCESS);
                    });
                }
                return service.update(t);
            });
        }).subscribeOn(scheduler).flatMap(result->result).subscribe(result-> log.info("Generated result {}",result));
    }
}
