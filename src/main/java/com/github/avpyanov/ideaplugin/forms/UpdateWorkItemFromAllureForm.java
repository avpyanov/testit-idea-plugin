package com.github.avpyanov.ideaplugin.forms;

import com.github.avpyanov.ideaplugin.allure.AllureUtils;
import com.github.avpyanov.ideaplugin.testit.TestItClient;
import com.github.avpyanov.ideaplugin.utils.WorkItemDtoUtils;
import com.github.avpyanov.testit.client.dto.WorkItem;
import com.github.avpyanov.testit.client.dto.WorkItemPutDto;
import com.github.avpyanov.testit.client.dto.WorkItemStep;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.CollectionComboBoxModel;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.model.TestResult;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UpdateWorkItemFromAllureForm extends JFrame {

    private final List<String> workItemIds;
    private final List<String> allureResultsList;
    private final Map<String, TestResult> allureResultsMap;
    private JPanel updateWorkItemStepsFromAllurePanel;
    private JButton updateButton;
    private JButton cancelButton;
    private JComboBox<String> workItemIdField;
    private JComboBox<String> allureResultsField;

    public UpdateWorkItemFromAllureForm(List<String> workItemIds, Map<String, TestResult> allureResultsMap) {
        this.workItemIds = workItemIds;
        this.allureResultsMap = allureResultsMap;
        this.allureResultsList = new ArrayList<>(allureResultsMap.keySet());
        setTitle("Update work item from allure results");
        setSize(640, 150);
        setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
        add(updateWorkItemStepsFromAllurePanel);
        workItemIdField.setEditable(false);
        workItemIdField.setEnabled(true);
        Collections.sort(workItemIds);
        Collections.sort(allureResultsList);
        workItemIdField.setModel(new CollectionComboBoxModel<>(workItemIds));
        allureResultsField.setEditable(false);
        allureResultsField.setEnabled(true);
        allureResultsField.setModel(new CollectionComboBoxModel<>(allureResultsList));
        updateButton.addActionListener(e -> handleUpdate());
        cancelButton.addActionListener(e -> handleCancel());
    }

    private void handleCancel() {
        setVisible(false);
    }

    private void handleUpdate() {
        if (workItemIdField.getSelectedItem() != null && allureResultsField.getSelectedItem() != null) {
            TestResult testResult = allureResultsMap.get(allureResultsField.getSelectedItem().toString());
            List<StepResult> flattenSteps = AllureUtils.flattenSteps(testResult.getSteps());
            WorkItem workItem = TestItClient.getClient().workItemsApi().getWorkItem(workItemIdField.getSelectedItem().toString());
            WorkItemPutDto workItemPutDto = WorkItemDtoUtils.getPutRequestDto(workItem);
            List<WorkItemStep> workItemSteps = AllureUtils.convertAllureStepsToWorkItemSteps(flattenSteps);
            workItemPutDto.setSteps(workItemSteps);
            TestItClient.getClient().workItemsApi().updateWorkItem(workItemPutDto);
            workItemIds.remove(workItemIdField.getSelectedItem().toString());
            allureResultsList.remove(allureResultsField.getSelectedItem().toString());
            if (workItemIds.isEmpty()) {
                setVisible(false);
            } else {
                workItemIdField.setModel(new CollectionComboBoxModel<>(workItemIds));
                allureResultsField.setModel(new CollectionComboBoxModel<>(allureResultsList));
            }
        } else {
            Messages.showInfoMessage("Select autotest and allure results file first",
                    "Update steps from allure-results");
        }
    }
}