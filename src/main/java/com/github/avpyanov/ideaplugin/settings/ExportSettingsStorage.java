package com.github.avpyanov.ideaplugin.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "com.github.avpyanov.ideaplugin.settings.ExportSettingsStorage",
        storages = @Storage(value = "ExportSettingsStorage.xml")
)
public class ExportSettingsStorage implements PersistentStateComponent<ExportSettings> {

    private final ExportSettings pluginSettings = new ExportSettings();

    public static ExportSettingsStorage getInstance() {
        return ApplicationManager.getApplication().getService(ExportSettingsStorage.class);
    }

    @Override
    public @Nullable ExportSettings getState() {
        return pluginSettings;
    }

    @Override
    public void loadState(@NotNull ExportSettings state) {
        XmlSerializerUtil.copyBean(state, pluginSettings);
    }
}
