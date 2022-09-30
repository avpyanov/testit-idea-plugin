package com.github.avpyanov.ideaplugin.actions;

import com.github.avpyanov.ideaplugin.forms.CreateAutotestForExistingWorkItemForm;
import com.github.avpyanov.ideaplugin.model.TestCase;
import com.github.avpyanov.ideaplugin.model.TestStep;
import com.github.avpyanov.ideaplugin.settings.ExportSettingsStorage;
import com.github.avpyanov.ideaplugin.utils.PsiUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreateAutotestForExistingWorkItem extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        final ExportSettingsStorage exportSettings = ExportSettingsStorage.getInstance();
        final PsiElement element = anActionEvent.getData(CommonDataKeys.PSI_FILE);
        if (element instanceof PsiJavaFile) {
            final PsiJavaFile psiJavaFile = (PsiJavaFile) element;
            final Map<PsiMethod, TestCase> testCaseMap = PsiUtils.getTests(psiJavaFile.getClasses()[0]);
            final var packageName = ((PsiJavaFile) element).getPackageName();
            final var className = ((PsiJavaFile) element).getClasses()[0].getName();
            for (Map.Entry<PsiMethod, TestCase> entry : testCaseMap.entrySet()) {
                if (!entry.getKey().hasAnnotation(Objects.requireNonNull(exportSettings.getState()).getTmsLinkAnnotation())) {
                    List<TestStep> steps = PsiUtils.getSteps(entry.getKey());
                    entry.getValue().setSteps(steps);
                    new CreateAutotestForExistingWorkItemForm(packageName, className, entry).setVisible(true);
                }
            }
        }
    }
}