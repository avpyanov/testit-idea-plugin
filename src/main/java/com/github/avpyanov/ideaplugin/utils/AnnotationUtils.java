package com.github.avpyanov.ideaplugin.utils;

import com.github.avpyanov.ideaplugin.settings.ExportSettingsStorage;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;

public class AnnotationUtils {

    private static final ExportSettingsStorage exportSettings = ExportSettingsStorage.getInstance();

    public static void addTmsAnnotation(PsiMethod method, String autotestId) {
        final Project project = method.getProject();
        CommandProcessor.getInstance().executeCommand(project,
                () -> ApplicationManager.getApplication().runWriteAction(() -> {
                    PsiUtils.addImport(method.getContainingFile(), exportSettings.getState().getTmsLinkAnnotation());
                    PsiAnnotation autoTesLink = PsiUtils.createTmsAnnotation(method, autotestId);
                    method.getModifierList().addAfter(autoTesLink, method.getAnnotation(exportSettings.getState().getTestAnnotation()));
                    PsiUtils.optimizeImports((PsiJavaFile) method.getContainingFile());
                }), "Add Import", null);
    }

    private AnnotationUtils() {
    }
}
