package com.github.avpyanov.ideaplugin.forms;

import com.github.avpyanov.ideaplugin.Annotations;
import com.github.avpyanov.ideaplugin.settings.ExportSettings;
import com.github.avpyanov.ideaplugin.settings.ExportSettingsStorage;
import com.github.avpyanov.ideaplugin.settings.TestRunners;
import org.jdesktop.swingx.JXComboBox;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ExportSettingsForm extends JPanel {

    private final ExportSettingsStorage settings = ExportSettingsStorage.getInstance();
    private final JFrame frame;
    private JComboBox<JXComboBox> testRunner;

    private JPanel exportSettings;
    private JTextField epicField;
    private JTextField featureField;
    private JTextField storyField;
    private JTextField stepField;
    private JTextField tmsField;

    private JTextField testField;
    private JTextField testNameField;
    private JButton saveButton;
    private JButton resetButton;
    private JButton cancelButton;

    public ExportSettingsForm(JFrame frame) {
        this.frame = frame;
        setName("Export");
        setLayout(new BorderLayout());
        add(exportSettings);

        epicField.setText(Annotations.ALLURE_EPIC_ANNOTATION);
        epicField.setEnabled(false);
        featureField.setText(Annotations.ALLURE_FEATURE_ANNOTATION);
        featureField.setEnabled(false);
        storyField.setText(Annotations.ALLURE_STORY_ANNOTATION);
        storyField.setEnabled(false);
        stepField.setText(Annotations.ALLURE_STEP_ANNOTATION);
        stepField.setEnabled(false);

        tmsField.setText(Objects.requireNonNull(settings.getState()).getTmsLinkAnnotation());
        testRunner.setSelectedItem(settings.getState().getTestRunner());
        testField.setText(settings.getState().getTestAnnotation());
        testNameField.setText(settings.getState().getTestNameAnnotation());

        saveButton.addActionListener(e -> handleSaveButton());
        resetButton.addActionListener(e -> handleResetButton());
        cancelButton.addActionListener(e -> handleCancelButton());
        testRunner.addActionListener(e -> handleTestRunner());
    }

    private void handleTestRunner() {
        if (testRunner.getSelectedItem() == TestRunners.JUNIT5.value()) {
            testField.setText(Annotations.JUNIT5_TEST_ANNOTATION);
            testNameField.setText(Annotations.JUNIT5_DISPLAY_NAME_ANNOTATION);
            testNameField.setEnabled(true);
        }
        if (testRunner.getSelectedItem() == TestRunners.TESTNG.value()) {
            testField.setText(Annotations.TESTNG_TEST_ANNOTATION);
            testNameField.setText("");
            testNameField.setEnabled(false);
        }
    }

    private void handleResetButton() {
        testRunner.setSelectedItem(TestRunners.TESTNG.value());
        testField.setText(Annotations.TESTNG_TEST_ANNOTATION);
        tmsField.setText(Annotations.ALLURE_TMS_LINK_ANNOTATION);
        testNameField.setText("");
        testNameField.setEnabled(false);
        ExportSettings updatedSettings = new ExportSettings();
        updatedSettings.setTmsLinkAnnotation(tmsField.getText());
        updatedSettings.setTestRunner(String.valueOf(testRunner.getSelectedItem()));
        updatedSettings.setTestAnnotation(testField.getText());
        updatedSettings.setTestNameAnnotation(testNameField.getText());
        settings.loadState(updatedSettings);
    }

    private void handleCancelButton() {
        frame.setVisible(false);
    }

    private void handleSaveButton() {
        ExportSettings updatedSettings = new ExportSettings();
        updatedSettings.setTmsLinkAnnotation(tmsField.getText());
        updatedSettings.setTestRunner(String.valueOf(testRunner.getSelectedItem()));
        updatedSettings.setTestAnnotation(testField.getText());
        updatedSettings.setTestNameAnnotation(testNameField.getText());
        settings.loadState(updatedSettings);
        frame.setVisible(false);
    }
}