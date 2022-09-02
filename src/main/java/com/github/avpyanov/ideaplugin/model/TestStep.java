package com.github.avpyanov.ideaplugin.model;

import java.util.List;
import java.util.StringJoiner;

public class TestStep {

    private String name;

    private List<TestStep> steps;

    public String getName() {
        return name;
    }

    public TestStep setName(String name) {
        this.name = name;
        return this;
    }

    public List<TestStep> getSteps() {
        return steps;
    }

    public TestStep setSteps(List<TestStep> steps) {
        this.steps = steps;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TestStep.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("steps=" + steps)
                .toString();
    }
}