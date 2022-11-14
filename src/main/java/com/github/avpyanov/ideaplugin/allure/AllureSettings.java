package com.github.avpyanov.ideaplugin.allure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AllureSettings {

    private String allureResultsFolder;

    public AllureSettings() {
        this.allureResultsFolder = "/target/allure-results";
    }
}