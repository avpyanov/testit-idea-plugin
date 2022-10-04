package com.github.avpyanov.ideaplugin.settings;

import com.github.avpyanov.ideaplugin.Annotations;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExportSettings {

    private String manualTestAnnotation;
    private String autotestAnnotation;
    private String testRunner;
    private String testAnnotation;
    private String testNameAnnotation;

    public ExportSettings() {
        manualTestAnnotation = Annotations.ALLURE_TMS_LINK_ANNOTATION;
        autotestAnnotation = Annotations.ALLURE_ID_ANNOTATION;
        testRunner = TestRunners.TESTNG.value();
        testAnnotation = Annotations.TESTNG_TEST_ANNOTATION;
        testNameAnnotation = "";
    }
}