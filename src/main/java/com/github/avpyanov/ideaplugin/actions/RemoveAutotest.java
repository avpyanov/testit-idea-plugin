package com.github.avpyanov.ideaplugin.actions;

import com.github.avpyanov.ideaplugin.forms.RemoveAutotestForm;
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
import java.util.stream.Collectors;

public class RemoveAutotest extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        final PsiElement element = anActionEvent.getData(CommonDataKeys.PSI_FILE);
        if (element instanceof PsiJavaFile) {
            final PsiJavaFile psiJavaFile = (PsiJavaFile) element;
            final List<PsiMethod> testsMethods = PsiUtils.getTestsMethods(psiJavaFile.getClasses()[0]);
            Map<PsiMethod, String> annotatedMethods = testsMethods.stream()
                    .filter(PsiUtils::hasAutotestAnnotation)
                    .collect(Collectors.toMap(m -> m, PsiUtils::getAutotestId));

            if (!annotatedMethods.isEmpty()){
                new RemoveAutotestForm(annotatedMethods).setVisible(true);
            }
        }
    }
}
