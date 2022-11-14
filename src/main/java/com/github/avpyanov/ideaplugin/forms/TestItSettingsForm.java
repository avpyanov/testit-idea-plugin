package com.github.avpyanov.ideaplugin.forms;

import com.intellij.ui.components.JBTabbedPane;

import javax.swing.*;
import java.awt.*;

public class TestItSettingsForm extends JFrame {

    public TestItSettingsForm() {
        setResizable(false);
        GeneralSettingsForm generalSettings = new GeneralSettingsForm(this);
        ExportSettingsForm exportSettings = new ExportSettingsForm(this);
        AllureSettingsForm allureSetting = new AllureSettingsForm(this);

        JTabbedPane tabbedPane = new JBTabbedPane();
        tabbedPane.add(generalSettings);
        tabbedPane.add(exportSettings);
        tabbedPane.add(allureSetting);

        setTitle("Settings");
        add(tabbedPane);
        setSize(450, 500);
        setLocationRelativeTo(null);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
    }
}