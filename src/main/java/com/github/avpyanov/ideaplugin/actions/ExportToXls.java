package com.github.avpyanov.ideaplugin.actions;

import com.github.avpyanov.ideaplugin.model.TestCase;
import com.github.avpyanov.ideaplugin.utils.XlsUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ExportToXls extends BaseExportAction {

    @Override
    public void exportTestCases(final AnActionEvent event, final Map<PsiMethod, TestCase> testCasesMap) {
        final Project project = event.getProject();
        final Path exportPath = Paths.get(project.getBasePath()).resolve("export.xlsx");
        XlsUtils.exportToXls(testCasesMap, exportPath.toString());
    }
}