package com.github.avpyanov.ideaplugin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class TestStep {

    private String name;

    private List<TestStep> steps;
}