package com.github.avpyanov.ideaplugin;

import com.intellij.openapi.ui.Messages;

public class PluginException extends RuntimeException {

    public PluginException(final String message, final Throwable e) {
        super(message, e);
        Messages.showMessageDialog(e.getMessage(), message, Messages.getErrorIcon());
    }
}
