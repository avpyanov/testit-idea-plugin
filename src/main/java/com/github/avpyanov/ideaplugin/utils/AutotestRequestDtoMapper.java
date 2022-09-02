package com.github.avpyanov.ideaplugin.utils;

import com.github.avpyanov.ideaplugin.model.TestCase;
import com.github.avpyanov.testit.client.dto.AutotestPostRequestDto;
import com.github.avpyanov.testit.client.dto.AutotestStep;
import com.intellij.psi.PsiMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AutotestRequestDtoMapper {


    public static AutotestPostRequestDto mapEntry(Map.Entry<PsiMethod, TestCase> entry) {
        AutotestPostRequestDto autotestPostRequestDto = new AutotestPostRequestDto();
        autotestPostRequestDto.setName(entry.getValue().getName());
        autotestPostRequestDto.setDescription(entry.getValue().getStory());
        List<AutotestStep> autotestSteps = new ArrayList<>();
        entry.getValue().getSteps().forEach(testStep -> {
            AutotestStep autoTestStepModel = new AutotestStep();
            autoTestStepModel.setTitle(testStep.getName());
            autotestSteps.add(autoTestStepModel);
        });
        autotestPostRequestDto.setSteps(autotestSteps);
        return autotestPostRequestDto;
    }

    private AutotestRequestDtoMapper() {
    }
}
