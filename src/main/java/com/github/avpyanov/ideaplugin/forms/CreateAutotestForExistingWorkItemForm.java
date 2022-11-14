package com.github.avpyanov.ideaplugin.forms;

import com.github.avpyanov.ideaplugin.PluginException;
import com.github.avpyanov.ideaplugin.model.TestCase;
import com.github.avpyanov.ideaplugin.testit.TestItSettingsStorage;
import com.github.avpyanov.ideaplugin.utils.AnnotationUtils;
import com.github.avpyanov.ideaplugin.utils.AutotestDtoUtils;
import com.github.avpyanov.testit.client.TestItApiClient;
import com.github.avpyanov.testit.client.dto.AutotestDto;
import com.github.avpyanov.testit.client.dto.AutotestPostRequestDto;
import com.github.avpyanov.testit.client.dto.WorkItem;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiMethod;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class CreateAutotestForExistingWorkItemForm extends JFrame {

    private JPanel createAutotestForWorkItem;
    private JTextField methodNameField;
    private JTextField workItemIdField;
    private JButton createButton;
    private JButton cancelButton;

    public CreateAutotestForExistingWorkItemForm(String packageName, String className, Map.Entry<PsiMethod, TestCase> entry) {
        setTitle("Create Autotest for existing Work Item");
        setSize(450, 150);
        setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
        add(createAutotestForWorkItem);
        methodNameField.setText(entry.getValue().getName());
        methodNameField.setEnabled(false);
        createButton.addActionListener(e -> handleCreate(entry, packageName, className));
        cancelButton.addActionListener(e -> handleCancel());
    }

    private void handleCancel() {
        setVisible(false);
    }

    private void handleCreate(Map.Entry<PsiMethod, TestCase> entry, String packageName, String className) {
        if (workItemIdField.getText().isEmpty()) {
            Messages.showErrorDialog("WorkItemId is not set", "Error");
        } else {
            try {
                TestItSettingsStorage testItSettingsStorage = TestItSettingsStorage.getInstance();
                TestItApiClient testItApi = new TestItApiClient(testItSettingsStorage.getState().getEndpoint(),
                        testItSettingsStorage.getState().getToken());
                WorkItem workItem = testItApi.workItemsApi().getWorkItem(workItemIdField.getText());
                AutotestPostRequestDto autotestToCreate = AutotestDtoUtils.mapEntry(packageName, className, entry);
                autotestToCreate.setWorkItemIdsForLinkWithAutoTest(List.of(workItem.getId()));
                autotestToCreate.setProjectId(testItSettingsStorage.getState().getProjectId());
                AutotestDto autotest = testItApi.autotestsApi().createAutotest(autotestToCreate);
                AnnotationUtils.addAutotestAnnotation(entry.getKey(), String.valueOf(autotest.getGlobalId()));
                setVisible(false);
            } catch (Exception e) {
                throw new PluginException("Failed to create autotest for work item", e);
            }
        }
    }
}
