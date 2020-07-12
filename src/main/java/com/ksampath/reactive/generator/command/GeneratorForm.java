package com.ksampath.reactive.generator.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GeneratorForm {
    @JsonProperty("Goal")
    int goal;
    @JsonProperty("Step")
    int step;
}
