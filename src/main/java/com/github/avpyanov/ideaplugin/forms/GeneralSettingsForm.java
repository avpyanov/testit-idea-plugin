package com.github.avpyanov.ideaplugin.forms;

import com.github.avpyanov.ideaplugin.testit.TestItSettings;
import com.github.avpyanov.ideaplugin.testit.TestItSettingsStorage;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GeneralSettingsForm extends JPanel {

    private final JFrame frame;
    private JPanel generalSettings;
    private JTextField endpointField;
    private JTextField tokenField;
    private JTextField projectIdField;
    private JTextField sectionIdField;
    private JButton saveButton;
    private JButton cancelButton;

    public GeneralSettingsForm(JFrame frame) {
        TestItSettingsStorage settings = TestItSettingsStorage.getInstance();
        this.frame = frame;
        setName("General");
        setLayout(new BorderLayout());
        add(generalSettings);
        endpointField.setText(Objects.requireNonNull(settings.getState()).getEndpoint());
        tokenField.setText(settings.getState().getToken());
        projectIdField.setText(String.valueOf(settings.getState().getProjectId()));
        sectionIdField.setText(String.valueOf(settings.getState().getRootSectionId()));
        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> handleCancel());
    }

    private void handleCancel() {
        frame.setVisible(false);
    }

    private void handleSave() {
        TestItSettingsStorage settings = TestItSettingsStorage.getInstance();
        TestItSettings updatedSettings = new TestItSettings();
        updatedSettings.setEndpoint(endpointField.getText());
        updatedSettings.setToken(tokenField.getText());
        updatedSettings.setProjectId(projectIdField.getText());
        updatedSettings.setRootSectionId(sectionIdField.getText());
        settings.loadState(updatedSettings);
        frame.setVisible(false);
    }
}