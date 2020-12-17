package ru.job4j.shortcut.service;

import org.springframework.stereotype.Service;

public interface SequenceGenerator {
    String generate(String source);
    String generate();
}
