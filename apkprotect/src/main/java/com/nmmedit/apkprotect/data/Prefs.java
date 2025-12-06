package com.nmmedit.apkprotect.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nmmedit.apkprotect.data.config.Config;
import com.nmmedit.apkprotect.util.FileUtils;
import com.nmmedit.apkprotect.util.OsDetector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Prefs {
    public static String CONFIG_WINDOWS_NAME = "config-windows.json";
    public static String VMSRC_NAME = "vmsrc.zip";
//    public static String CONFIG_PATH = new File(FileUtils.getHomePath(), "tools/" + (OsDetector.isWindows() ? CONFIG_WINDOWS_NAME : "config.json")).getAbsolutePath();

    public static String getConfigPath() {
        return new File(
                FileUtils.getHomePath(),
                "tools/" + (OsDetector.isWindows() ? CONFIG_WINDOWS_NAME : "config.json")
        ).getAbsolutePath();
    }


    public static Config config() {
        String CONFIG_PATH = getConfigPath();
        final File configFile = new File(CONFIG_PATH);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try (
                    final InputStream inputStream = Prefs.class.getResourceAsStream("/" + (OsDetector.isWindows() ? CONFIG_WINDOWS_NAME : "config.json"));
                    final FileOutputStream outputStream = new FileOutputStream(configFile);
            ) {
                FileUtils.copyStream(inputStream, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("config-windows.json的路径是"+configFile.getAbsolutePath());
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            String content = FileUtils.readFile(CONFIG_PATH, StandardCharsets.UTF_8);
            final Config config = gson.fromJson(content, Config.class);
            //compact old config
            if (config.environment == null) {
                //remove old config
                final File file = new File(CONFIG_PATH);
                file.delete();
                //load new config
                return config();
            }
            return config;
        } catch (IOException e) {
            throw new RuntimeException("Load config failed", e);
        }
    }

    public static boolean isArm() {
        return config().abi.arm;
    }

    public static boolean isArm64() {
        return config().abi.arm64;
    }

    public static boolean isX86() {
        return config().abi.x86;
    }

    public static boolean isX64() {
        return config().abi.x64;
    }

    public static String sdkPath() {
        return config().environment.sdk_path;
    }

    public static String cmakePath() {
        System.out.println("ndk_path" + config().environment.cmake_path);
        return config().environment.cmake_path;
    }


    public static String ndkPath() {
        System.out.println("ndk_path" + config().environment.ndk_path);
        return config().environment.ndk_path;
    }

    public static String ndkToolchains() {
        return config().environment.ndk_toolchains;
    }

    public static String ndkAbi() {
        return config().environment.ndk_abi;
    }

    public static String ndkStrip() {
        return config().environment.ndk_strip;
    }
}
