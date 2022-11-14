package com.github.avpyanov.ideaplugin.allure;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "com.github.avpyanov.ideaplugin.allure.AllureSettingsStorage",
        storages = @Storage(value = "AllureSettingsStorage.xml")
)
public class AllureSettingsStorage implements PersistentStateComponent<AllureSettings> {

   private final AllureSettings allureSettings = new AllureSettings();

    public static AllureSettingsStorage getInstance() {
        return ApplicationManager.getApplication().getService(AllureSettingsStorage.class);
    }

    @Override
    public @Nullable AllureSettings getState() {
        return allureSettings;
    }

    @Override
    public void loadState(@NotNull AllureSettings state) {
        XmlSerializerUtil.copyBean(state, allureSettings);
    }
}