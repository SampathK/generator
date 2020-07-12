package com.ksampath.reactive.generator.service;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.UUID;

@Service
public class UuidSupplier implements IdSupplier {
    @Override
    public Mono<String> get() {
       return Mono.fromSupplier(()-> {
            UUID uuid = UUID.randomUUID();
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
            return Base64.encodeBase64URLSafeString(bb.array());
        });
    }
}
