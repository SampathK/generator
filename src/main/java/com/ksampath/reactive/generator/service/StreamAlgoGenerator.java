package com.ksampath.reactive.generator.service;

import com.ksampath.reactive.generator.command.GeneratorForm;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class StreamAlgoGenerator implements AlgoGenerator {
    @Override
    public Mono<List<Integer>> apply(GeneratorForm form) {
        return  Mono.fromSupplier((()-> {
            return  IntStream.rangeClosed(0, form.getGoal())
                    .map(i -> form.getGoal()-i).filter(i->i%form.getStep()==0).boxed().collect(Collectors.toList());
        }));
    }
}
