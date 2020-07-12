package com.ksampath.reactive.generator.service;

import com.ksampath.reactive.generator.command.GeneratorForm;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

public interface AlgoGenerator extends Function<GeneratorForm, Mono<List<Integer>>> {
}
