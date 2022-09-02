package com.github.avpyanov.ideaplugin.actions;
import com.github.avpyanov.ideaplugin.model.TestCase;
import com.github.avpyanov.ideaplugin.utils.PsiUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.*;

import java.util.HashMap;
import java.util.Map;


public abstract class BaseExportAction extends AnAction {

    public abstract void exportTestCases(final AnActionEvent event, final Map<PsiMethod, TestCase> testCaseMap);

    @Override
    public void actionPerformed(final AnActionEvent event) {
        final PsiElement element = event.getData(CommonDataKeys.PSI_ELEMENT);
        if (element instanceof PsiDirectory) {
            Map<PsiMethod, TestCase> testCaseMap = new HashMap<>();
            PsiElement[] psiElements = element.getChildren();
            for (PsiElement psiElement : psiElements) {
                PsiJavaFile psiJavaFile = null;
                try {
                    psiJavaFile = (PsiJavaFile) psiElement;
                } catch (ClassCastException cce) {
                    cce.printStackTrace();
                }
                if (psiJavaFile != null) {
                    testCaseMap.putAll(PsiUtils.getTestsFromClass(psiJavaFile.getClasses()[0]));
                }
            }
            exportTestCases(event, testCaseMap);
        } else {
            if (element instanceof PsiClass) {
                final PsiClass psiClass = (PsiClass) element;
                final Map<PsiMethod, TestCase> testCaseMap = PsiUtils.getTestsFromClass(psiClass);
                exportTestCases(event, testCaseMap);
            }
        }
    }
}