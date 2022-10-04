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

    private final JFrame frame;
    private JComboBox<JXComboBox> testRunner;

    private JPanel exportSettings;
    private JTextField epicField;
    private JTextField featureField;
    private JTextField storyField;
    private JTextField stepField;
    private JTextField manualTestAnnotation;
    private JTextField autotestAnnotationField;
    private JTextField testField;
    private JTextField testNameField;
    private JButton saveButton;
    private JButton resetButton;
    private JButton cancelButton;


    public ExportSettingsForm(JFrame frame) {
        ExportSettings settings = ExportSettingsStorage.getInstance().getState();
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

        manualTestAnnotation.setText(Objects.requireNonNull(settings).getManualTestAnnotation());
        autotestAnnotationField.setText(settings.getAutotestAnnotation());
        testRunner.setSelectedItem(settings.getTestRunner());
        testField.setText(settings.getTestAnnotation());
        testNameField.setText(settings.getTestNameAnnotation());

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
        ExportSettingsStorage settings = ExportSettingsStorage.getInstance();
        testRunner.setSelectedItem(TestRunners.TESTNG.value());
        testField.setText(Annotations.TESTNG_TEST_ANNOTATION);
        manualTestAnnotation.setText(Annotations.ALLURE_TMS_LINK_ANNOTATION);
        autotestAnnotationField.setText(Annotations.ALLURE_ID_ANNOTATION);
        testNameField.setText("");
        testNameField.setEnabled(false);
        ExportSettings updatedSettings = new ExportSettings();
        updatedSettings.setManualTestAnnotation(manualTestAnnotation.getText());
        updatedSettings.setTestRunner(String.valueOf(testRunner.getSelectedItem()));
        updatedSettings.setTestAnnotation(testField.getText());
        updatedSettings.setTestNameAnnotation(testNameField.getText());
        settings.loadState(updatedSettings);
    }

    private void handleCancelButton() {
        frame.setVisible(false);
    }

    private void handleSaveButton() {
        ExportSettingsStorage settings = ExportSettingsStorage.getInstance();
        ExportSettings updatedSettings = new ExportSettings();
        updatedSettings.setManualTestAnnotation(manualTestAnnotation.getText());
        updatedSettings.setAutotestAnnotation(autotestAnnotationField.getText());
        updatedSettings.setTestRunner(String.valueOf(testRunner.getSelectedItem()));
        updatedSettings.setTestAnnotation(testField.getText());
        updatedSettings.setTestNameAnnotation(testNameField.getText());
        settings.loadState(updatedSettings);
        frame.setVisible(false);
    }
}