package com.github.avpyanov.ideaplugin.forms;

import com.intellij.ui.components.JBTabbedPane;

import javax.swing.*;
import java.awt.*;

public class TestItSettingsForm {

    private final JFrame jFrame = new JFrame("Settings");

    public TestItSettingsForm() {
        jFrame.setResizable(false);
        GeneralSettings generalSettings = new GeneralSettings(jFrame);
        ExportSettingsForm exportSettings = new ExportSettingsForm(jFrame);

        JTabbedPane tabbedPane = new JBTabbedPane();
        tabbedPane.add("General", generalSettings.getPanel());
        tabbedPane.add("Export", exportSettings.getPanel());

        jFrame.setTitle("Settings");
        jFrame.setSize(400, 500);
        jFrame.add(tabbedPane);
        jFrame.setLocationRelativeTo(null);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setLocation(dim.width / 2 - jFrame.getSize().width / 2, dim.height / 2 - jFrame.getSize().height / 2);
    }

    public void setVisible() {
        jFrame.setVisible(true);
    }
}