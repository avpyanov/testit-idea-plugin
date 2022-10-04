package com.github.avpyanov.ideaplugin.utils;

import com.github.avpyanov.ideaplugin.settings.ExportSettingsStorage;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;

import java.util.Objects;

public class AnnotationUtils {

    private static final ExportSettingsStorage exportSettings = ExportSettingsStorage.getInstance();

    public static void addAutotestAnnotation(PsiMethod method, String autotestId) {
        final Project project = method.getProject();
        CommandProcessor.getInstance().executeCommand(project,
                () -> ApplicationManager.getApplication().runWriteAction(() -> {
                    PsiUtils.addImport(method.getContainingFile(),
                            Objects.requireNonNull(exportSettings.getState()).getAutotestAnnotation());
                    PsiAnnotation autoTesLink = PsiUtils.createAutotestAnnotation(method, autotestId);
                    method.getModifierList().addBefore(autoTesLink, method.getAnnotation(exportSettings.getState().getTestAnnotation()));
                    PsiUtils.optimizeImports((PsiJavaFile) method.getContainingFile());
                }), "Add Import", null);
    }

    public static void addAutotestAndWorkItemAnnotations(PsiMethod method, String autotestId, String workItemId) {
        final Project project = method.getProject();
        CommandProcessor.getInstance().executeCommand(project,
                () -> ApplicationManager.getApplication().runWriteAction(() -> {
                    PsiUtils.addImport(method.getContainingFile(),
                            Objects.requireNonNull(exportSettings.getState()).getAutotestAnnotation());
                    PsiUtils.addImport(method.getContainingFile(),
                            Objects.requireNonNull(exportSettings.getState()).getManualTestAnnotation());
                    PsiAnnotation autoTesLink = PsiUtils.createAutotestAnnotation(method, autotestId);
                    PsiAnnotation workItemLink = PsiUtils.createManualTestAnnotation(method, workItemId);
                    method.getModifierList().addBefore(autoTesLink, method.getAnnotation(exportSettings.getState().getTestAnnotation()));
                    method.getModifierList().addBefore(workItemLink, method.getAnnotation(exportSettings.getState().getAutotestAnnotation()));
                    PsiUtils.optimizeImports((PsiJavaFile) method.getContainingFile());
                }), "Add Import", null);
    }

    private AnnotationUtils() {
    }
}
