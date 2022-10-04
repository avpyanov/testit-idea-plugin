package com.github.avpyanov.ideaplugin.actions;

import com.github.avpyanov.ideaplugin.PluginException;
import com.github.avpyanov.ideaplugin.model.TestCase;
import com.github.avpyanov.ideaplugin.settings.ExportSettingsStorage;
import com.github.avpyanov.ideaplugin.testit.TestItSettingsStorage;
import com.github.avpyanov.ideaplugin.utils.PsiUtils;
import com.github.avpyanov.testit.client.TestItApi;
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
        final ExportSettingsStorage exportSettings = ExportSettingsStorage.getInstance();
        final PsiElement element = anActionEvent.getData(CommonDataKeys.PSI_FILE);
        if (element instanceof PsiJavaFile) {
            final PsiJavaFile psiJavaFile = (PsiJavaFile) element;
            final Map<PsiMethod, TestCase> testCaseMap = PsiUtils.getTests(psiJavaFile.getClasses()[0]);
            final TestItApi testItApi = new TestItApi(Objects.requireNonNull(settings.getState()).getEndpoint(),
                    settings.getState().getToken());
            for (Map.Entry<PsiMethod, TestCase> entry : testCaseMap.entrySet()) {
                final boolean hasAutotestAnnotation = entry.getKey()
                        .hasAnnotation(Objects.requireNonNull(exportSettings.getState()).getAutotestAnnotation());

                final boolean hasWorkItemAnnotation = entry.getKey()
                        .hasAnnotation(Objects.requireNonNull(exportSettings.getState()).getManualTestAnnotation());

                if (hasWorkItemAnnotation && hasAutotestAnnotation) {
                    String autotestId = PsiUtils.getAutotestId(entry.getKey());
                    String workItemId = PsiUtils.getManualTestId(entry.getKey());
                    try {
                        UUID id = testItApi.getWorkItemsClient().getWorkItem(workItemId).getId();
                        IdDto idDto = new IdDto();
                        idDto.setId(id);
                        testItApi.getAutotestsClient().linkAutotestAndWorkItem(autotestId, idDto);
                        Messages.showInfoMessage(autotestId + " and " + workItemId + "linked", "Success!");
                    } catch (Exception e) {
                        throw new PluginException("Failed to link autotest and workitem", e);
                    }
                } else {
                    Messages.showErrorDialog("Autotest or manual test annotations missing",
                            "Failed!");
                }
            }
        }
    }
}
