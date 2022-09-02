package com.github.avpyanov.ideaplugin.settings;

public enum TestRunners {

    TESTNG("TestNG"),
    JUNIT5("Junit 5");

    private final String value;

    TestRunners(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
