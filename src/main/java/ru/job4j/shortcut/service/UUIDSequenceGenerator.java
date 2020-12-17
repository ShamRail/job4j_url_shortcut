package ru.job4j.shortcut.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UUIDSequenceGenerator implements SequenceGenerator {
    @Override
    public String generate(String source) {
        return String.format("%s-%s", source, generate());
    }

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
