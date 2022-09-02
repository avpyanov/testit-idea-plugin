package com.github.avpyanov.ideaplugin.testit;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "com.github.avpyanov.ideaplugin.testit.TestItSettingsStorage",
        storages = @Storage(value = "TestItSettingsPlugin.xml")
)
public class TestItSettingsStorage implements PersistentStateComponent<TestItSettings> {

    private final TestItSettings testItSettings = new TestItSettings();

    public static TestItSettingsStorage getInstance() {
        return ApplicationManager.getApplication().getService(TestItSettingsStorage.class);
    }

    @Override
    public @Nullable
    TestItSettings getState() {
        return testItSettings;
    }

    @Override
    public void loadState(@NotNull TestItSettings state) {
        XmlSerializerUtil.copyBean(state, testItSettings);
    }
}
