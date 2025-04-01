package com.github.avpyanov.ideaplugin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TestCase {

    private String name;
    private String epic;
    private String feature;
    private String story;
    private List<TestStep> steps;

}