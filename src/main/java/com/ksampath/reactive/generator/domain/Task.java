package com.ksampath.reactive.generator.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task<T> {
    @Id
    @EqualsAndHashCode.Include
    private String _id;
    private T result;
    private Status status;

    public Task<T> update(Task<T> entity) {
        this.result = entity.getResult();
        this.status = entity.getStatus();
        return this;
    }

}
