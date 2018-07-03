package io.stallion.starter.examples.boot;

import io.stallion.plugins.BasePluginSettings;
import io.stallion.settings.SettingMeta;

public class MyZyxAppSettings extends BasePluginSettings {

    public static MyZyxAppSettings getInstance() {
        return getInstance(MyZyxAppSettings.class, "myzyxappname");
    }

    @SettingMeta
    private String someApiKey;


    @Override
    public void assignDefaults() {

    }

    public String getSomeApiKey() {
        return someApiKey;
    }

    public MyZyxAppSettings setSomeApiKey(String someApiKey) {
        this.someApiKey = someApiKey;
        return this;
    }
}