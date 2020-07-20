package com.example.accessibilitytool;

import java.io.IOException;

public class Commands {
    private static final String batteryLevelCommand ="dumpsys battery set level %s";

    public void showDevicesWithADB() throws IOException, InterruptedException {
        CommandExecutor.exec("");
    }
}
