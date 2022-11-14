package com.github.avpyanov.ideaplugin.actions;

import com.github.avpyanov.ideaplugin.PluginException;
import com.github.avpyanov.ideaplugin.model.TestCase;
import com.github.avpyanov.ideaplugin.model.TestStep;
import com.github.avpyanov.ideaplugin.settings.ExportSettingsStorage;
import com.github.avpyanov.ideaplugin.testit.TestItSettingsStorage;
import com.github.avpyanov.ideaplugin.utils.PsiUtils;
import com.github.avpyanov.testit.client.TestItApiClient;
import com.github.avpyanov.testit.client.dto.WorkItem;
import com.github.avpyanov.testit.client.dto.WorkItemPutDto;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.avpyanov.ideaplugin.utils.WorkItemDtoUtils.getPutRequestDto;

public class UpdateWorkItem extends AnAction {

    private final ExportSettingsStorage exportSettings = ExportSettingsStorage.getInstance();
    private final TestItSettingsStorage testItSettings = TestItSettingsStorage.getInstance();

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        final PsiElement element = anActionEvent.getData(CommonDataKeys.PSI_FILE);
        if (element instanceof PsiJavaFile) {
            final PsiJavaFile psiJavaFile = (PsiJavaFile) element;
            final Map<PsiMethod, TestCase> testCaseMap = PsiUtils.getTests(psiJavaFile.getClasses()[0]);
            List<String> idList = new ArrayList<>();
            for (Map.Entry<PsiMethod, TestCase> entry : testCaseMap.entrySet()) {
                if (entry.getKey().hasAnnotation(Objects.requireNonNull(exportSettings.getState()).getManualTestAnnotation())) {
                    String globalId;
                    try {
                        TestItApiClient testItApi = new TestItApiClient(testItSettings.getState().getEndpoint(),
                                testItSettings.getState().getToken());
                        List<TestStep> steps = PsiUtils.getSteps(entry.getKey());
                        entry.getValue().setSteps(steps);
                        globalId = entry.getKey()
                                .getAnnotation(exportSettings.getState().getManualTestAnnotation())
                                .findAttributeValue("value")
                                .getText()
                                .replace("\"", "");
                        WorkItem workItem = testItApi.workItemsApi().getWorkItem(globalId);
                        WorkItemPutDto putRequestDto = getPutRequestDto(workItem, entry);
                        testItApi.workItemsApi().updateWorkItem(putRequestDto);
                    } catch (Exception e) {
                        throw new PluginException("Failed to update WorkItem " + entry.getKey().getName(), e);
                    }
                    idList.add(globalId);
                }
            }
            if (idList.isEmpty()) {
                Messages.showInfoMessage("No WorkItems to update", "Update WorkItem");
            } else {
                Messages.showInfoMessage("Updated WorkItems: " + idList,
                        "Success!");
            }

        }
    }
}
