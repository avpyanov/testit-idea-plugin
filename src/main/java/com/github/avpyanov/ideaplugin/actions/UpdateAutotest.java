package com.github.avpyanov.ideaplugin.actions;

import com.github.avpyanov.ideaplugin.PluginException;
import com.github.avpyanov.ideaplugin.model.TestCase;
import com.github.avpyanov.ideaplugin.model.TestStep;
import com.github.avpyanov.ideaplugin.settings.ExportSettingsStorage;
import com.github.avpyanov.ideaplugin.testit.TestItSettingsStorage;
import com.github.avpyanov.ideaplugin.utils.AutotestDtoUtils;
import com.github.avpyanov.ideaplugin.utils.PsiUtils;
import com.github.avpyanov.testit.client.TestItApi;
import com.github.avpyanov.testit.client.dto.AutotestPutRequestDto;
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

public class UpdateAutotest extends AnAction {

    private final ExportSettingsStorage exportSettings = ExportSettingsStorage.getInstance();
    private final TestItSettingsStorage testItSettings = TestItSettingsStorage.getInstance();

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        final PsiElement element = anActionEvent.getData(CommonDataKeys.PSI_FILE);
        if (element instanceof PsiJavaFile) {
            final PsiJavaFile psiJavaFile = (PsiJavaFile) element;
            final Map<PsiMethod, TestCase> testCaseMap = PsiUtils.getTests(psiJavaFile.getClasses()[0]);
            final var packageName = ((PsiJavaFile) element).getPackageName();
            final var className = ((PsiJavaFile) element).getClasses()[0].getName();
            List<String> idList = new ArrayList<>();
            for (Map.Entry<PsiMethod, TestCase> entry : testCaseMap.entrySet()) {
                if (entry.getKey().hasAnnotation(Objects.requireNonNull(exportSettings.getState()).getTmsLinkAnnotation())) {
                    String globalId;
                    try {
                        TestItApi testItApi = new TestItApi(testItSettings.getState().getEndpoint(), testItSettings.getState().getToken());
                        List<TestStep> steps = PsiUtils.getSteps(entry.getKey());
                        globalId = entry.getKey()
                                .getAnnotation(exportSettings.getState().getTmsLinkAnnotation())
                                .findAttributeValue("value")
                                .getText()
                                .replace("\"", "");
                        String id = testItApi.getAutotestsClient().getAutoTest(globalId).getId();
                        entry.getValue().setSteps(steps);
                        AutotestPutRequestDto updateAutoTestDto = AutotestDtoUtils.getUpdateAutoTestDto(packageName, className, entry);
                        updateAutoTestDto.setId(id);
                        updateAutoTestDto.setGlobalId(globalId);
                        updateAutoTestDto.setProjectId(testItSettings.getState().getProjectId());

                        testItApi.getAutotestsClient().updateAutotest(updateAutoTestDto);
                    } catch (Exception e) {
                        throw new PluginException("Failed to update autotest " + entry.getKey().getName(), e);
                    }
                    idList.add(globalId);
                }
            }
            Messages.showMessageDialog("Updated tests: " + idList,
                    "Success!",
                    Messages.getInformationIcon());
        }
    }
}
