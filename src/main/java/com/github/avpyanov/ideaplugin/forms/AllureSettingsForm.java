package com.github.avpyanov.ideaplugin.forms;

import com.github.avpyanov.ideaplugin.allure.AllureSettings;
import com.github.avpyanov.ideaplugin.allure.AllureSettingsStorage;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AllureSettingsForm extends JPanel {

    private final JFrame frame;
    private JPanel allureSettingsPanel;
    private JTextField allureResultsField;
    private JButton saveButton;
    private JButton cancelButton;

    public AllureSettingsForm(JFrame frame) {
        AllureSettings settings = AllureSettingsStorage.getInstance().getState();
        this.frame = frame;
        setName("Allure");
        setLayout(new BorderLayout());
        add(allureSettingsPanel);
        allureResultsField.setText(Objects.requireNonNull(settings).getAllureResultsFolder());
        cancelButton.addActionListener(e -> handleCancel());
        saveButton.addActionListener(e -> handleSave());
    }

    private void handleSave() {
        AllureSettingsStorage settings = AllureSettingsStorage.getInstance();
        AllureSettings updatedSettings = new AllureSettings();
        if (!allureResultsField.getText().isEmpty()) {
            updatedSettings.setAllureResultsFolder(allureResultsField.getText());
            settings.loadState(updatedSettings);
            frame.setVisible(false);
        } else Messages.showErrorDialog("allure-results field is empty", "Save failed");
    }

    private void handleCancel() {
        frame.setVisible(false);
    }
}
