package com.example.accessibilitytool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecutor {
    public static void exec(String command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("adb", "devices");
        Process pc = pb.start();
        pc.waitFor();
        System.out.println("Done");

//        Process process = null;
//        String commandString;
//        commandString = String.format("%s", "devices");
////        if(deviceId != null) {
////            commandString = String.format("%s", "adb -s " + deviceId + " shell " + command);
////        }else
////            commandString = String.format("%s", "adb shell " + command);
//
//        System.out.print("Command is "+commandString+"\n");
//        try {
//            process = Runtime.getRuntime().exec("adb devices");
//        } catch (IOException e) {
//            System.out.println("ERROR, MISTAKE, ERROR, MISTAKE" + e.getMessage());
//        }
//        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            System.out.print(line+"\n");
//        }
    }
}
