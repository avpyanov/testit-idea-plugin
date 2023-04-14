package com.github.avpyanov.ideaplugin.forms;

import com.github.avpyanov.ideaplugin.testit.TestItClient;
import com.github.avpyanov.ideaplugin.utils.AnnotationUtils;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.CollectionComboBoxModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class RemoveAutotestForm extends JFrame {

    private JPanel removeAutotestPanel;
    private JComboBox<String> autotestIdsField;
    private JButton removeButton;
    private JButton cancelButton;

    public RemoveAutotestForm(Map<PsiMethod, String> testMethods) {
        setTitle("Remove autotest");
        setSize(400, 150);
        setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
        add(removeAutotestPanel);
        autotestIdsField.setEditable(false);
        autotestIdsField.setEnabled(true);
        ArrayList<String> autotestIds = new ArrayList<>(testMethods.values());
        autotestIdsField.setModel(new CollectionComboBoxModel<>(autotestIds));

        removeButton.addActionListener(e -> handleRemove(testMethods));
        cancelButton.addActionListener(e -> handleCancel());
    }

    private void handleRemove(Map<PsiMethod, String> testMethods) {
        final String autotestId = Objects.requireNonNull(autotestIdsField.getSelectedItem()).toString();
        TestItClient.getClient().autotestsApi().deleteAutotest(autotestId);
        Map.Entry<PsiMethod, String> psiMethodEntry = testMethods.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(autotestId))
                .findFirst().orElseThrow();

        AnnotationUtils.removeAutotestAnnotation(psiMethodEntry.getKey());
        testMethods.entrySet().removeIf(next -> next.getValue().equals(autotestId));

        if (testMethods.isEmpty()){
            setVisible(false);
        } else {
            ArrayList<String> autotestIds = new ArrayList<>(testMethods.values());
            autotestIdsField.setModel(new CollectionComboBoxModel<>(autotestIds));
        }
    }

    private void handleCancel() {
        setVisible(false);
    }
}
