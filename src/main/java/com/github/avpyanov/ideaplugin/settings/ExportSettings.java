package com.github.avpyanov.ideaplugin.settings;

import com.github.avpyanov.ideaplugin.Annotations;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExportSettings {

    private String tmsLinkAnnotation;
    private String testRunner;
    private String testAnnotation;
    private String testNameAnnotation;

    public ExportSettings() {
        tmsLinkAnnotation = Annotations.ALLURE_TMS_LINK_ANNOTATION;
        testRunner = TestRunners.TESTNG.value();
        testAnnotation = Annotations.TESTNG_TEST_ANNOTATION;
        testNameAnnotation = "";
    }
}