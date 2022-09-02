package com.github.avpyanov.ideaplugin.forms;

import com.github.avpyanov.ideaplugin.testit.TestItSettings;
import com.github.avpyanov.ideaplugin.testit.TestItSettingsStorage;
import com.github.avpyanov.testit.client.TestItApi;
import com.github.avpyanov.testit.client.dto.Attribute;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static com.github.avpyanov.ideaplugin.forms.PanelActions.createTextPanel;

class GeneralSettings {

    private final JFrame frame;
    private final JPanel generalSettingsPanel = new JPanel();
    private final TestItSettingsStorage settings = TestItSettingsStorage.getInstance();

    private final JTextField endpointField = new JTextField(25);
    private final JTextField tokenField = new JTextField(25);
    private final JTextField projectIdField = new JTextField(25);
    private final JTextField rootSectionIdField = new JTextField(25);
    private final JTextField featureAttributeField = new JTextField(25);
    private final JTextField storyAttributeField = new JTextField(25);
    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel");
    JButton fillFeatureStoryButton = new JButton("Add Feature/Story");

    public GeneralSettings(JFrame frame) {
        this.frame = frame;
        generalSettingsPanel.add(createTextPanel("endpoint", endpointField), BorderLayout.CENTER);
        generalSettingsPanel.add(createTextPanel("token", tokenField), BorderLayout.CENTER);
        generalSettingsPanel.add(createTextPanel("projectId", projectIdField), BorderLayout.CENTER);
        generalSettingsPanel.add(createTextPanel("sectionId", rootSectionIdField), BorderLayout.CENTER);
        generalSettingsPanel.add(createTextPanel("featureId", featureAttributeField), BorderLayout.CENTER);
        generalSettingsPanel.add(createTextPanel("storyId", storyAttributeField), BorderLayout.CENTER);
        generalSettingsPanel.add(fillFeatureStoryButton);
        generalSettingsPanel.add(saveButton);
        generalSettingsPanel.add(cancelButton);

        endpointField.setText(Objects.requireNonNull(settings.getState()).getEndpoint());
        tokenField.setText(settings.getState().getToken());
        projectIdField.setText(String.valueOf(settings.getState().getProjectId()));
        rootSectionIdField.setText(String.valueOf(settings.getState().getRootSectionId()));
        featureAttributeField.setText(String.valueOf(settings.getState().getFeatureAttributeId()));
        storyAttributeField.setText(String.valueOf(settings.getState().getStoryAttributeId()));

        cancelButton.addActionListener(e -> handleCancelButton());
        saveButton.addActionListener(e -> handleOkButton());
        fillFeatureStoryButton.addActionListener(e -> handleFillButton());

    }

    public JPanel getPanel() {
        return generalSettingsPanel;
    }

    private void handleCancelButton() {
        frame.setVisible(false);
    }

    private void handleOkButton() {
        TestItSettings updatedSettings = new TestItSettings();
        updatedSettings.setEndpoint(endpointField.getText());
        updatedSettings.setToken(tokenField.getText());
        updatedSettings.setProjectId(projectIdField.getText());
        updatedSettings.setRootSectionId(rootSectionIdField.getText());
        updatedSettings.setFeatureAttributeId(featureAttributeField.getText());
        updatedSettings.setStoryAttributeId(storyAttributeField.getText());
        settings.loadState(updatedSettings);
        frame.setVisible(false);
    }

    private void handleFillButton() {
        var testItApi = new TestItApi(Objects.requireNonNull(settings.getState()).getEndpoint(), settings.getState().getToken());
        var attributes = testItApi.getProjectsClient().getAttributes(settings.getState().getProjectId());
        var feature = attributes.stream()
                .filter(a -> a.getName().equals("Feature"))
                .findAny()
                .orElse(null);
        if (feature != null) {
            featureAttributeField.setText(feature.getId());
        } else {
            var newFeature = new Attribute();
            newFeature.setName("Feature");
            newFeature.setType("string");
            newFeature.setEnabled(true);
            newFeature.setRequired(false);
            var response = testItApi.getProjectsClient()
                    .createAttribute(settings.getState().getProjectId(), newFeature);
            featureAttributeField.setText(response.getId());
        }

        var story = attributes.stream()
                .filter(a -> a.getName().equals("Story"))
                .findAny()
                .orElse(null);
        if (story != null) {
            storyAttributeField.setText(story.getId());
        } else {
            var newStory = new Attribute();
            newStory.setName("Story");
            newStory.setType("string");
            newStory.setEnabled(true);
            newStory.setRequired(false);
            var response = testItApi.getProjectsClient()
                    .createAttribute(settings.getState().getProjectId(), newStory);
            storyAttributeField.setText(response.getId());
        }
    }
}