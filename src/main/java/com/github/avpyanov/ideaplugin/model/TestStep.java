package com.github.avpyanov.ideaplugin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TestStep {

    private String name;

    private List<TestStep> steps;
}