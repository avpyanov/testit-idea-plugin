package com.github.avpyanov.ideaplugin.actions;

import com.github.avpyanov.ideaplugin.forms.TestItSettingsForm;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class TestItSettings extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final TestItSettingsForm form = new TestItSettingsForm();
        form.setVisible();
    }
}