package com.nmmedit.protect;

import com.nmmedit.apkprotect.ApkFolders;
import com.nmmedit.apkprotect.ApkProtectV2;
import com.nmmedit.apkprotect.deobfus.MappingReader;
import com.nmmedit.apkprotect.dex2c.converter.ClassAnalyzer;
import com.nmmedit.apkprotect.dex2c.converter.instructionrewriter.RandomInstructionRewriter;
import com.nmmedit.apkprotect.dex2c.filters.BasicKeepConfig;
import com.nmmedit.apkprotect.dex2c.filters.ClassAndMethodFilter;
import com.nmmedit.apkprotect.dex2c.filters.ProguardMappingConfig;
import com.nmmedit.apkprotect.dex2c.filters.SimpleConvertConfig;
import com.nmmedit.apkprotect.dex2c.filters.SimpleRules;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

public class TestApk {

    public static void main(String[] args) {

        try {
            String aabFilePath = "G:\\jiagu\\vmpTest\\duplicated-app_167_build_winsor_02_17_.apk";
            String mappingFilePath = "G:\\jiagu\\vmpTest\\mapping.txt";
            String ruleFilePath = "G:\\jiagu\\vmpTest\\rules_oppo_ndk21.txt";


            final File apk = new File(aabFilePath);
            final File outDir = new File(apk.getParentFile(), "build");

            ClassAndMethodFilter filterConfig = new BasicKeepConfig();
            final SimpleRules simpleRules = new SimpleRules();
//            if (args.length > 1) {
                simpleRules.parse(new InputStreamReader(new FileInputStream(new File(ruleFilePath)), StandardCharsets.UTF_8));
//            } else {
                //all classes
//                simpleRules.parse(new StringReader("class *"));
//            }

//            if (args.length > 2) {
                final MappingReader mappingReader = new MappingReader(new File(mappingFilePath));
                filterConfig = new ProguardMappingConfig(filterConfig, mappingReader, simpleRules);
//            } else {
//                filterConfig = new SimpleConvertConfig(new BasicKeepConfig(), simpleRules);
//            }

            final ClassAnalyzer classAnalyzer = new ClassAnalyzer();
            //todo 可能需要加载某些厂商私有的sdk


            final ApkFolders apkFolders = new ApkFolders(apk, outDir);


            final ApkProtectV2 apkProtect = new ApkProtectV2.Builder(apkFolders)
                    .setInstructionRewriter(new RandomInstructionRewriter())
                    .setFilter(filterConfig)
                    .setClassAnalyzer(classAnalyzer)
                    .build();
            apkProtect.run();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

}
