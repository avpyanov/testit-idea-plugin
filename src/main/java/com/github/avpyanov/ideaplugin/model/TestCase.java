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
public class TestCase {

    private String name;
    private String epic;
    private String feature;
    private String story;
    private List<TestStep> steps;

}