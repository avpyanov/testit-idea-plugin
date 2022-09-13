package com.github.avpyanov.ideaplugin.forms;

import com.intellij.ui.components.JBTabbedPane;

import javax.swing.*;
import java.awt.*;

public class TestItSettingsForm extends JFrame {

    public TestItSettingsForm() {
        setResizable(false);
        GeneralSettingsForm generalSettings = new GeneralSettingsForm(this);
        ExportSettingsForm exportSettings = new ExportSettingsForm(this);

        JTabbedPane tabbedPane = new JBTabbedPane();
        tabbedPane.add(generalSettings);
        tabbedPane.add(exportSettings);

        setTitle("Settings");
        setSize(400, 500);
        add(tabbedPane);
        setLocationRelativeTo(null);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
    }
}