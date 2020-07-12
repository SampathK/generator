package com.ksampath.reactive.generator.service;

import reactor.core.publisher.Mono;

import java.util.function.Supplier;

public interface IdSupplier extends Supplier<Mono<String>> {

}
