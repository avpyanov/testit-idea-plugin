package com.github.avpyanov.ideaplugin.utils;

import com.github.avpyanov.ideaplugin.model.TestCase;
import com.github.avpyanov.testit.client.dto.AutotestPostRequestDto;
import com.github.avpyanov.testit.client.dto.AutotestPutRequestDto;
import com.github.avpyanov.testit.client.dto.AutotestStep;
import com.intellij.psi.PsiMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AutotestDtoUtils {

    public static AutotestPostRequestDto mapEntry(String packageName, String className, Map.Entry<PsiMethod, TestCase> entry) {
        AutotestPostRequestDto autotestPostRequestDto = mapEntry(entry);
        autotestPostRequestDto.setExternalId(packageName + "." + className + "." + entry.getKey().getName());
        autotestPostRequestDto.setClassname(className);
        autotestPostRequestDto.setNamespace(packageName);
        return autotestPostRequestDto;
    }

    public static AutotestPostRequestDto mapEntry(Map.Entry<PsiMethod, TestCase> entry) {
        AutotestPostRequestDto autotestPostRequestDto = new AutotestPostRequestDto();
        autotestPostRequestDto.setName(entry.getValue().getName());
        autotestPostRequestDto.setDescription(entry.getValue().getStory());
        List<AutotestStep> autotestSteps = getSteps(entry);
        autotestPostRequestDto.setSteps(autotestSteps);
        return autotestPostRequestDto;
    }

    public static AutotestPutRequestDto getUpdateAutoTestDto(String packageName, String className, Map.Entry<PsiMethod, TestCase> entry) {
        AutotestPutRequestDto autotestPutRequestDto = new AutotestPutRequestDto();
        autotestPutRequestDto.setName(entry.getValue().getName());
        autotestPutRequestDto.setExternalId(packageName + "." + className + "." + entry.getKey().getName());
        autotestPutRequestDto.setClassname(className);
        autotestPutRequestDto.setNamespace(packageName);
        autotestPutRequestDto.setDescription(entry.getValue().getStory());
        List<AutotestStep> autotestSteps = getSteps(entry);
        autotestPutRequestDto.setSteps(autotestSteps);
        return autotestPutRequestDto;
    }

    private static List<AutotestStep> getSteps(Map.Entry<PsiMethod, TestCase> entry) {
        List<AutotestStep> autotestSteps = new ArrayList<>();
        entry.getValue().getSteps().forEach(testStep -> {
            AutotestStep autoTestStepModel = new AutotestStep();
            autoTestStepModel.setTitle(testStep.getName());
            autotestSteps.add(autoTestStepModel);
        });
        return autotestSteps;
    }

    private AutotestDtoUtils() {
    }
}