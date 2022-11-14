package com.github.avpyanov.ideaplugin.actions;

import com.github.avpyanov.ideaplugin.PluginException;
import com.github.avpyanov.ideaplugin.model.TestCase;
import com.github.avpyanov.ideaplugin.testit.TestItSettingsStorage;
import com.github.avpyanov.ideaplugin.utils.PsiUtils;
import com.github.avpyanov.testit.client.TestItApiClient;
import com.github.avpyanov.testit.client.dto.IdDto;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class LinkAutotestAndWorkItem extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        final TestItSettingsStorage settings = TestItSettingsStorage.getInstance();
        final PsiElement element = anActionEvent.getData(CommonDataKeys.PSI_FILE);
        if (element instanceof PsiJavaFile) {
            final PsiJavaFile psiJavaFile = (PsiJavaFile) element;
            final Map<PsiMethod, TestCase> testCaseMap = PsiUtils.getTests(psiJavaFile.getClasses()[0]);
            final TestItApiClient testItApi = new TestItApiClient(Objects.requireNonNull(settings.getState()).getEndpoint(),
                    settings.getState().getToken());
            for (Map.Entry<PsiMethod, TestCase> entry : testCaseMap.entrySet()) {
                if (PsiUtils.hasManualTestAnnotation(entry.getKey()) && PsiUtils.hasAutotestAnnotation(entry.getKey())) {
                    String autotestId = PsiUtils.getAutotestId(entry.getKey());
                    String workItemId = PsiUtils.getManualTestId(entry.getKey());
                    try {
                        UUID id = testItApi.workItemsApi().getWorkItem(workItemId).getId();
                        IdDto idDto = new IdDto(id);
                        testItApi.autotestsApi().linkAutotestAndWorkItem(autotestId, idDto);
                        Messages.showInfoMessage(autotestId + " and " + workItemId + "linked", "Success!");
                    } catch (Exception e) {
                        throw new PluginException("Failed to link Autotest and WorkItem", e);
                    }
                } else {
                    Messages.showErrorDialog("Autotest or manual test annotations missing",
                            "Failed!");
                }
            }
        }
    }
}
