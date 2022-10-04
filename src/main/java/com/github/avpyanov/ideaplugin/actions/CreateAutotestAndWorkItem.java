package com.github.avpyanov.ideaplugin.actions;

import com.github.avpyanov.ideaplugin.PluginException;
import com.github.avpyanov.ideaplugin.model.TestCase;
import com.github.avpyanov.ideaplugin.settings.ExportSettingsStorage;
import com.github.avpyanov.ideaplugin.testit.TestItSettingsStorage;
import com.github.avpyanov.ideaplugin.utils.AnnotationUtils;
import com.github.avpyanov.ideaplugin.utils.AutotestDtoUtils;
import com.github.avpyanov.ideaplugin.utils.PsiUtils;
import com.github.avpyanov.testit.client.TestItApi;
import com.github.avpyanov.testit.client.dto.AutotestDto;
import com.github.avpyanov.testit.client.dto.AutotestPostRequestDto;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class CreateAutotestAndWorkItem extends AnAction {

    @Override
    public void actionPerformed(final @NotNull AnActionEvent anActionEvent) {
        final TestItSettingsStorage settings = TestItSettingsStorage.getInstance();
        final ExportSettingsStorage exportSettings = ExportSettingsStorage.getInstance();
        final PsiElement element = anActionEvent.getData(CommonDataKeys.PSI_FILE);
        if (element instanceof PsiJavaFile) {
            final PsiJavaFile psiJavaFile = (PsiJavaFile) element;
            final Map<PsiMethod, TestCase> testCaseMap = PsiUtils.getTestsFromClass(psiJavaFile.getClasses()[0]);
            final var packageName = ((PsiJavaFile) element).getPackageName();
            final var className = ((PsiJavaFile) element).getClasses()[0].getName();
            for (Map.Entry<PsiMethod, TestCase> entry : testCaseMap.entrySet()) {
                if (!entry.getKey().hasAnnotation(Objects.requireNonNull(exportSettings.getState()).getAutotestAnnotation())) {
                    TestItApi testItApi = new TestItApi(Objects.requireNonNull(settings.getState()).getEndpoint(), settings.getState().getToken());
                    AutotestPostRequestDto autotestToCreate = AutotestDtoUtils.mapEntry(packageName, className, entry);
                    autotestToCreate.setProjectId(settings.getState().getProjectId());
                    autotestToCreate.setShouldCreateWorkItem(true);
                    try {
                        AutotestDto autotest = testItApi.getAutotestsClient().createAutotest(autotestToCreate);
                        String workItemGlobalId = testItApi.getAutotestsClient().
                                getLinkedWorkItems(autotest.getGlobalId(), false)
                                .get(0)
                                .getGlobalId();
                        AnnotationUtils.addAutotestAndWorkItemAnnotations(entry.getKey(), autotest.getGlobalId(), workItemGlobalId);
                    } catch (Exception e) {
                        throw new PluginException("Failed to create autotest and work item", e);
                    }
                }
            }
        }
    }
}