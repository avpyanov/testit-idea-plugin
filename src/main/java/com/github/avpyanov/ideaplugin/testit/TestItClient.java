package com.github.avpyanov.ideaplugin.testit;

import com.github.avpyanov.testit.client.TestItApiClient;

public class TestItClient {

    private static final TestItSettingsStorage testItSettings = TestItSettingsStorage.getInstance();

    public static TestItApiClient getClient() {
        return new TestItApiClient(testItSettings.getState().getEndpoint(), testItSettings.getState().getToken());
    }

    private TestItClient() {
    }
}
