package com.github.avpyanov.ideaplugin.forms;

import com.github.avpyanov.ideaplugin.allure.AllureUtils;
import com.github.avpyanov.ideaplugin.testit.TestItClient;
import com.github.avpyanov.ideaplugin.utils.AutotestDtoUtils;
import com.github.avpyanov.testit.client.dto.AutotestDto;
import com.github.avpyanov.testit.client.dto.AutotestPutRequestDto;
import com.github.avpyanov.testit.client.dto.AutotestStep;
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

public class UpdateAutotestFromAllureForm extends JFrame {

    private final List<String> autotestIds;
    private final List<String> allureResultsList;
    private final Map<String, TestResult> allureResultsMap;
    private JPanel updateAutotestFromAllurePanel;
    private JButton updateButton;
    private JButton cancelButton;
    private JComboBox<String> autotestIdField;
    private JComboBox<String> allureResultsField;

    public UpdateAutotestFromAllureForm(List<String> autotestIds, Map<String, TestResult> allureResultsMap) {
        this.allureResultsMap = allureResultsMap;
        this.autotestIds = autotestIds;
        this.allureResultsList = new ArrayList<>(allureResultsMap.keySet());
        setTitle("Update autotest from allure results");
        setSize(550, 150);
        setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
        add(updateAutotestFromAllurePanel);
        autotestIdField.setEditable(false);
        autotestIdField.setEnabled(true);
        Collections.sort(autotestIds);
        Collections.sort(allureResultsList);
        autotestIdField.setModel(new CollectionComboBoxModel<>(autotestIds));
        allureResultsField.setEditable(false);
        allureResultsField.setEnabled(true);
        allureResultsField.setModel(new CollectionComboBoxModel<>(allureResultsList));
        updateButton.addActionListener(e -> handleUpdate());
        cancelButton.addActionListener(e -> handleCancel());
    }

    private void handleUpdate() {
        if (autotestIdField.getSelectedItem() != null && allureResultsField.getSelectedItem() != null) {
            TestResult testResult = allureResultsMap.get(allureResultsField.getSelectedItem().toString());
            List<StepResult> flattenSteps = AllureUtils.flattenSteps(testResult.getSteps());
            AutotestDto autoTest = TestItClient.getClient().autotestsApi().getAutoTest(autotestIdField.getSelectedItem().toString());
            AutotestPutRequestDto autotestPutRequestDto = AutotestDtoUtils.getAutotestPutRequestDto(autoTest);

            List<AutotestStep> autotestSteps = AllureUtils.convertAllureSteps(flattenSteps);
            autotestPutRequestDto.setSteps(autotestSteps);
            TestItClient.getClient().autotestsApi().updateAutotest(autotestPutRequestDto);

            autotestIds.remove(autotestIdField.getSelectedItem().toString());
            allureResultsList.remove(allureResultsField.getSelectedItem().toString());

            if (autotestIds.isEmpty()) {
                setVisible(false);
            } else {
                autotestIdField.setModel(new CollectionComboBoxModel<>(autotestIds));
                allureResultsField.setModel(new CollectionComboBoxModel<>(allureResultsList));
            }
        } else {
            Messages.showInfoMessage("Select autotest and allure results file first",
                    "Update steps from allure-results");
        }
    }

    private void handleCancel() {
        setVisible(false);
    }
}