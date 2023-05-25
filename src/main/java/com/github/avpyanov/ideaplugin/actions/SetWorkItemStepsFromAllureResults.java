package com.github.avpyanov.ideaplugin.actions;

import com.github.avpyanov.ideaplugin.allure.AllureUtils;
import com.github.avpyanov.ideaplugin.forms.UpdateWorkItemFromAllureForm;
import com.github.avpyanov.ideaplugin.model.TestCase;
import com.github.avpyanov.ideaplugin.utils.PsiUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import io.qameta.allure.model.TestResult;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetWorkItemStepsFromAllureResults extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        final PsiElement element = anActionEvent.getData(CommonDataKeys.PSI_FILE);
        final String projectDir = anActionEvent.getProject().getBasePath();
        final String allurePath = projectDir + AllureUtils.getAllureResultsFolder();
        if (element instanceof PsiJavaFile) {
            final PsiJavaFile psiJavaFile = (PsiJavaFile) element;
            final Map<PsiMethod, TestCase> testCaseMap = PsiUtils.getTests(psiJavaFile.getClasses()[0]);
            List<String> workItemIds = new ArrayList<>();
            List<String> allureResultsFiles = AllureUtils.getAllureResultsFiles(projectDir);
            Map<String,TestResult> allureResultsMap = new HashMap<>();

            for (String allureFile : allureResultsFiles) {
                TestResult testResult = AllureUtils.getResultsFromFile(allurePath + allureFile);
                allureResultsMap.put(testResult.getName(),testResult);
            }

            for (Map.Entry<PsiMethod, TestCase> entry : testCaseMap.entrySet()) {
                if (PsiUtils.hasManualTestAnnotation(entry.getKey())) {
                    workItemIds.add(PsiUtils.getManualTestId(entry.getKey()));
                }
            }
            if (!workItemIds.isEmpty()&& !allureResultsMap.isEmpty()) {
                new UpdateWorkItemFromAllureForm(workItemIds, allureResultsMap).setVisible(true);
            }
        }
    }
}
