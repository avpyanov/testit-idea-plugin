package com.github.avpyanov.ideaplugin.model;

import java.util.List;
import java.util.StringJoiner;

public class TestCase {

    private String name;
    private String epic;
    private String feature;
    private String story;
    private List<TestStep> steps;

    public String getName() {
        return name;
    }

    public TestCase setName(String name) {
        this.name = name;
        return this;
    }

    public String getEpic() {
        return epic;
    }

    public TestCase setEpic(String epic) {
        this.epic = epic;
        return this;
    }

    public String getFeature() {
        return feature;
    }

    public TestCase setFeature(String feature) {
        this.feature = feature;
        return this;
    }

    public String getStory() {
        return story;
    }

    public TestCase setStory(String story) {
        this.story = story;
        return this;
    }

    public List<TestStep> getSteps() {
        return steps;
    }

    public TestCase setSteps(List<TestStep> steps) {
        this.steps = steps;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TestCase.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("epic='" + epic + "'")
                .add("feature='" + feature + "'")
                .add("story='" + story + "'")
                .add("steps=" + steps)
                .toString();
    }
}
