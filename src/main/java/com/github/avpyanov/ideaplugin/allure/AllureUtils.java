package com.github.avpyanov.ideaplugin.allure;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.avpyanov.ideaplugin.PluginException;
import com.github.avpyanov.testit.client.dto.AutotestStep;
import com.github.avpyanov.testit.client.dto.WorkItemStep;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.model.TestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AllureUtils {

    public static List<String> getAllureResultsFiles(String projectDir) {
        try (Stream<Path> stream = Files.list(Paths.get(projectDir +
                Objects.requireNonNull(AllureSettingsStorage.getInstance().getState()).getAllureResultsFolder()))) {
            return stream
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(f -> f.contains("result.json"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new PluginException("Unable to get allure-result files", e);
        }
    }

    public static String getAllureResultsFolder() {
        return Objects.requireNonNull(AllureSettingsStorage.getInstance().getState()).getAllureResultsFolder();
    }

    public static TestResult getResultsFromFile(final String filePath) {
        JsonMapper jsonMapper = JsonMapper.builder()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
                .build();
        try {
            return jsonMapper.readValue(Paths.get(filePath).toFile(), TestResult.class);
        } catch (IOException e) {
            throw new PluginException("Error at attempt to read allure results file", e);
        }
    }

    public static List<StepResult> flattenSteps(final List<StepResult> steps) {
        final List<StepResult> flattenSteps = new ArrayList<>();
        for (StepResult step : steps) {
            if (step.getSteps().isEmpty()) {
                flattenSteps.add(step);
            } else {
                flattenSteps.add(step);
                flattenSteps.addAll(flattenSteps(step.getSteps()));
            }
        }
        return flattenSteps;
    }

    public static List<AutotestStep> convertAllureSteps(List<StepResult> steps) {
        List<AutotestStep> autotestSteps = new ArrayList<>();
        for (StepResult step : steps) {
            AutotestStep autotestStep = new AutotestStep();
            autotestStep.setTitle(step.getName());
            autotestSteps.add(autotestStep);
        }
        return autotestSteps;
    }

    public static List<WorkItemStep> convertAllureStepsToWorkItemSteps(List<StepResult> steps) {
        List<WorkItemStep> workItemSteps = new ArrayList<>();
        for (StepResult step : steps) {
            WorkItemStep workItemStep = new WorkItemStep();
            workItemStep.action(step.getName());
            workItemSteps.add(workItemStep);
        }
        return workItemSteps;
    }

    private AllureUtils() {
    }
}
