package com.github.avpyanov.ideaplugin.forms;

import com.github.avpyanov.ideaplugin.testit.TestItSettings;
import com.github.avpyanov.ideaplugin.testit.TestItSettingsStorage;
import com.github.avpyanov.testit.client.TestItApi;
import com.github.avpyanov.testit.client.dto.Attribute;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GeneralSettingsForm extends JPanel {

    private final TestItSettingsStorage settings = TestItSettingsStorage.getInstance();
    private final JFrame frame;

    private JPanel generalSettings;
    private JTextField endpointField;
    private JTextField tokenField;
    private JTextField projectIdField;
    private JTextField sectionIdField;
    private JTextField featureIdField;
    private JTextField storyIdField;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton addFeatureStoryButton;


    public GeneralSettingsForm(JFrame frame) {
        this.frame = frame;
        setName("General");
        setLayout(new BorderLayout());
        add(generalSettings);
        endpointField.setText(Objects.requireNonNull(settings.getState()).getEndpoint());
        tokenField.setText(settings.getState().getToken());
        projectIdField.setText(String.valueOf(settings.getState().getProjectId()));
        sectionIdField.setText(String.valueOf(settings.getState().getRootSectionId()));
        featureIdField.setText(String.valueOf(settings.getState().getFeatureAttributeId()));
        storyIdField.setText(String.valueOf(settings.getState().getStoryAttributeId()));

        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> handleCancel());
        addFeatureStoryButton.addActionListener(e -> handleAddFeature());
    }

    private void handleCancel() {
        frame.setVisible(false);
    }

    private void handleSave() {
        TestItSettings updatedSettings = new TestItSettings();
        updatedSettings.setEndpoint(endpointField.getText());
        updatedSettings.setToken(tokenField.getText());
        updatedSettings.setProjectId(projectIdField.getText());
        updatedSettings.setRootSectionId(sectionIdField.getText());
        updatedSettings.setFeatureAttributeId(featureIdField.getText());
        updatedSettings.setStoryAttributeId(storyIdField.getText());
        settings.loadState(updatedSettings);
        frame.setVisible(false);
    }

    private void handleAddFeature() {
        var testItApi = new TestItApi(Objects.requireNonNull(settings.getState()).getEndpoint(), settings.getState().getToken());
        var attributes = testItApi.getProjectsClient().getAttributes(settings.getState().getProjectId());
        var feature = attributes.stream()
                .filter(a -> a.getName().equals("Feature"))
                .findAny()
                .orElse(null);
        if (feature != null) {
            featureIdField.setText(feature.getId());
        } else {
            var newFeature = new Attribute();
            newFeature.setName("Feature");
            newFeature.setType("string");
            newFeature.setEnabled(true);
            newFeature.setRequired(false);
            var response = testItApi.getProjectsClient()
                    .createAttribute(settings.getState().getProjectId(), newFeature);
            featureIdField.setText(response.getId());
        }

        var story = attributes.stream()
                .filter(a -> a.getName().equals("Story"))
                .findAny()
                .orElse(null);
        if (story != null) {
            storyIdField.setText(story.getId());
        } else {
            var newStory = new Attribute();
            newStory.setName("Story");
            newStory.setType("string");
            newStory.setEnabled(true);
            newStory.setRequired(false);
            var response = testItApi.getProjectsClient()
                    .createAttribute(settings.getState().getProjectId(), newStory);
            storyIdField.setText(response.getId());
        }
    }
}