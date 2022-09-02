package com.github.avpyanov.ideaplugin.forms;

import javax.swing.*;
import java.awt.*;

class PanelActions {

    static JPanel createTextPanel(String label, JTextField textField) {
        JPanel panel = new JPanel();
        panel.add(new Label(label), BorderLayout.LINE_END);
        panel.add(textField, BorderLayout.LINE_START);
        return panel;
    }

    static JPanel createPanel(String label, JComponent component) {
        JPanel panel = new JPanel();
        panel.add(new Label(label), BorderLayout.LINE_END);
        panel.add(component, BorderLayout.LINE_START);
        return panel;
    }

    private PanelActions() {
    }
}