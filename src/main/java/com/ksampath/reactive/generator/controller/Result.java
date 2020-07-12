package com.ksampath.reactive.generator.controller;

import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public final class Result {
    private final String result;

    public Result(List<Integer> result) {
        this.result = result.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}
