package com.nmmedit.protect;

import com.nmmedit.apkprotect.ApkFolders;
import com.nmmedit.apkprotect.ApkProtect;
import com.nmmedit.apkprotect.data.Prefs;
import com.nmmedit.apkprotect.deobfus.MappingReader;
import com.nmmedit.apkprotect.dex2c.converter.ClassAnalyzer;
import com.nmmedit.apkprotect.dex2c.converter.instructionrewriter.RandomInstructionRewriter;
import com.nmmedit.apkprotect.dex2c.filters.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ApkMain {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("No Input apk.");
            System.err.println("<inApk> [<convertRuleFile> mapping.txt]");
            return;
        }
        final File apk = new File(args[0]);
        final File outDir = new File(apk.getParentFile(), "build");

        ClassAndMethodFilter filterConfig = new BasicKeepConfig();
        final SimpleRules simpleRules = new SimpleRules();
        if (args.length > 1) {
            simpleRules.parse(new InputStreamReader(new FileInputStream(args[1]), StandardCharsets.UTF_8));
        } else {
            //all classes
            simpleRules.parse(new StringReader("class *"));
        }

        if (args.length > 2) {
            final MappingReader mappingReader = new MappingReader(new File(args[2]));
            filterConfig = new ProguardMappingConfig(filterConfig, mappingReader, simpleRules);
        } else {
            filterConfig = new SimpleConvertConfig(new BasicKeepConfig(), simpleRules);
        }

        if (args.length > 3) {
            String configWindowsName = args[3];
            Prefs.CONFIG_WINDOWS_NAME = configWindowsName;
            System.out.println("传入的configWindowsName=" + configWindowsName + "Prefs.CONFIG_WINDOWS_NAME="+Prefs.CONFIG_WINDOWS_NAME);
        }

        if (args.length > 4) {
            String vmsrcZipName = args[4];
            Prefs.VMSRC_NAME = vmsrcZipName;
            System.out.println("传入的vmsrcName="+vmsrcZipName + "Prefs.VMSRC_NAME="+Prefs.VMSRC_NAME);
        }

        final ClassAnalyzer classAnalyzer = new ClassAnalyzer();
        //todo 可能需要加载某些厂商私有的sdk


        final ApkFolders apkFolders = new ApkFolders(apk, outDir);


        final ApkProtect apkProtect = new ApkProtect.Builder(apkFolders)
                .setInstructionRewriter(new RandomInstructionRewriter())
                .setFilter(filterConfig)
                .setClassAnalyzer(classAnalyzer)
                .build();
        apkProtect.run();
    }
}
