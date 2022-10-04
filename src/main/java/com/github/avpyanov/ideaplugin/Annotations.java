package com.github.avpyanov.ideaplugin;

public final class Annotations {

    public static final String TESTNG_TEST_ANNOTATION = "org.testng.annotations.Test";
    public static final String JUNIT5_TEST_ANNOTATION = "org.junit.jupiter.api.Test";
    public static final String JUNIT5_DISPLAY_NAME_ANNOTATION = "org.junit.jupiter.api.DisplayName";

    public static final String ALLURE_TMS_LINK_ANNOTATION = "io.qameta.allure.TmsLink";
    public static final String ALLURE_ID_ANNOTATION = "io.qameta.allure.AllureId";


    public static final String ALLURE_STEP_ANNOTATION = "io.qameta.allure.Step";
    public static final String ALLURE_EPIC_ANNOTATION = "io.qameta.allure.Epic";
    public static final String ALLURE_FEATURE_ANNOTATION = "io.qameta.allure.Feature";
    public static final String ALLURE_STORY_ANNOTATION = "io.qameta.allure.Story";

    private Annotations() {
    }
}