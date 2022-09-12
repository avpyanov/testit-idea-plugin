package com.github.avpyanov.ideaplugin.forms;

import com.github.avpyanov.ideaplugin.Annotations;
import com.github.avpyanov.ideaplugin.settings.ExportSettings;
import com.github.avpyanov.ideaplugin.settings.ExportSettingsStorage;
import com.github.avpyanov.ideaplugin.settings.TestRunners;
import org.jdesktop.swingx.JXComboBox;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

import static com.github.avpyanov.ideaplugin.forms.PanelActions.createPanel;
import static com.github.avpyanov.ideaplugin.forms.PanelActions.createTextPanel;


class ExportSettingsForm {

    private final JFrame frame;
    private final JPanel exportSettingsPanel = new JPanel();
    private final ExportSettingsStorage settings = ExportSettingsStorage.getInstance();

    private final String[] testRunnersList = Arrays.stream(TestRunners.values())
            .map(TestRunners::value)
            .toArray(String[]::new);

    private final JComboBox testRunner = new JXComboBox(testRunnersList);
    private final JTextField tmsAnnotation = new JTextField(20);
    private final JTextField testAnnotation = new JTextField(20);
    private final JTextField testNameAnnotation = new JTextField(20);


    public ExportSettingsForm(JFrame frame) {
        this.frame = frame;
        JTextField epicAnnotation = new JTextField(20);
        JTextField featureAnnotation = new JTextField(20);
        JTextField storyAnnotation = new JTextField(20);
        JTextField stepAnnotation = new JTextField(20);
        exportSettingsPanel.add(createTextPanel("@Epic", epicAnnotation), BorderLayout.CENTER);
        exportSettingsPanel.add(createTextPanel("@Feature", featureAnnotation), BorderLayout.CENTER);
        exportSettingsPanel.add(createTextPanel("@Story", storyAnnotation), BorderLayout.CENTER);
        exportSettingsPanel.add(createTextPanel("@Step", stepAnnotation), BorderLayout.CENTER);
        exportSettingsPanel.add(createTextPanel("@Tms", tmsAnnotation), BorderLayout.CENTER);
        exportSettingsPanel.add(createPanel("Test runner", testRunner), BorderLayout.CENTER);
        exportSettingsPanel.add(createTextPanel("@Test", testAnnotation), BorderLayout.CENTER);
        exportSettingsPanel.add(createTextPanel("Test name", testNameAnnotation), BorderLayout.CENTER);
        JButton saveButton = new JButton("Save");
        exportSettingsPanel.add(saveButton);
        JButton cancelButton = new JButton("Cancel");
        exportSettingsPanel.add(cancelButton);
        JButton resetButton = new JButton("Reset");
        exportSettingsPanel.add(resetButton);

        testRunner.addActionListener(e -> handleTestRunner());
        saveButton.addActionListener(e -> handleSaveButton());
        cancelButton.addActionListener(e -> handleCancelButton());
        resetButton.addActionListener(e -> handleResetButton());

        epicAnnotation.setText(Annotations.ALLURE_EPIC_ANNOTATION);
        epicAnnotation.setEnabled(false);
        featureAnnotation.setText(Annotations.ALLURE_FEATURE_ANNOTATION);
        featureAnnotation.setEnabled(false);
        storyAnnotation.setText(Annotations.ALLURE_STORY_ANNOTATION);
        storyAnnotation.setEnabled(false);
        stepAnnotation.setText(Annotations.ALLURE_STEP_ANNOTATION);
        stepAnnotation.setEnabled(false);

        tmsAnnotation.setText(Objects.requireNonNull(settings.getState()).getTmsLinkAnnotation());
        testRunner.setSelectedItem(settings.getState().getTestRunner());
        testAnnotation.setText(settings.getState().getTestAnnotation());
        testNameAnnotation.setText(settings.getState().getTestNameAnnotation());
    }

    public JPanel getPanel() {
        return exportSettingsPanel;
    }

    private void handleTestRunner() {
        if (testRunner.getSelectedItem() == TestRunners.JUNIT5.value()) {
            testAnnotation.setText(Annotations.JUNIT5_TEST_ANNOTATION);
            testNameAnnotation.setText(Annotations.JUNIT5_DISPLAY_NAME_ANNOTATION);
            testNameAnnotation.setEnabled(true);
        }
        if (testRunner.getSelectedItem() == TestRunners.TESTNG.value()) {
            testAnnotation.setText(Annotations.TESTNG_TEST_ANNOTATION);
            testNameAnnotation.setText("");
            testNameAnnotation.setEnabled(false);
        }
    }

    private void handleResetButton() {
        testRunner.setSelectedItem(TestRunners.TESTNG.value());
        testAnnotation.setText(Annotations.TESTNG_TEST_ANNOTATION);
        tmsAnnotation.setText(Annotations.ALLURE_TMS_LINK_ANNOTATION);
        testNameAnnotation.setText("");
        testNameAnnotation.setEnabled(false);
        ExportSettings updatedSettings = new ExportSettings();
        updatedSettings.setTmsLinkAnnotation(tmsAnnotation.getText());
        updatedSettings.setTestRunner(String.valueOf(testRunner.getSelectedItem()));
        updatedSettings.setTestAnnotation(testAnnotation.getText());
        updatedSettings.setTestNameAnnotation(testNameAnnotation.getText());
        settings.loadState(updatedSettings);
    }

    private void handleCancelButton() {
        frame.setVisible(false);
    }

    private void handleSaveButton() {
        ExportSettings updatedSettings = new ExportSettings();
        updatedSettings.setTmsLinkAnnotation(tmsAnnotation.getText());
        updatedSettings.setTestRunner(String.valueOf(testRunner.getSelectedItem()));
        updatedSettings.setTestAnnotation(testAnnotation.getText());
        updatedSettings.setTestNameAnnotation(testNameAnnotation.getText());
        settings.loadState(updatedSettings);
        frame.setVisible(false);
    }
}