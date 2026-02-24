package com.example.skala_ium.clazz.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ClassGroup {
    AI, CLOUD;

    private static final Map<String, ClassGroup> lookup =
            Arrays.stream(ClassGroup.values())
                    .collect(Collectors.toMap(g -> g.name().toUpperCase(), g -> g));

    @JsonCreator
    public static ClassGroup from(String value) {
        if (value == null) return null;
        ClassGroup group = lookup.get(value.toUpperCase());
        if (group == null) {
            throw new IllegalArgumentException("없는 반 타입입니다: " + value);
        }
        return group;
    }
}
