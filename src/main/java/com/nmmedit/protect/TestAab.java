package com.nmmedit.protect;

import com.nmmedit.apkprotect.aab.AabFolders;
import com.nmmedit.apkprotect.aab.AabProtect;
import com.nmmedit.apkprotect.deobfus.MappingReader;
import com.nmmedit.apkprotect.dex2c.converter.ClassAnalyzer;
import com.nmmedit.apkprotect.dex2c.converter.instructionrewriter.RandomInstructionRewriter;
import com.nmmedit.apkprotect.dex2c.filters.BasicKeepConfig;
import com.nmmedit.apkprotect.dex2c.filters.ClassAndMethodFilter;
import com.nmmedit.apkprotect.dex2c.filters.ProguardMappingConfig;
import com.nmmedit.apkprotect.dex2c.filters.SimpleRules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

public class TestAab {

    public static void main(String[] args)  {
        // 开始加固
        System.out.println("start");

        try {
            String aabFilePath = "G:\\androidDemo2\\nmmp-master\\nmmp-master\\nmm-protect\\test\\test.aab";
            String mappingFilePath = "G:\\androidDemo2\\nmmp-master\\nmmp-master\\nmm-protect\\test\\mapping.txt";
            String ruleFilePath = "G:\\androidDemo2\\nmmp-master\\nmmp-master\\nmm-protect\\test\\rules.txt";
            // 这里调用aabMain
            final File aab = new File(aabFilePath);
            final File outDir = new File(aab.getParentFile(), "bundleOut");

            ClassAndMethodFilter filterConfig = new BasicKeepConfig();
            final SimpleRules simpleRules = new SimpleRules();
//            if (args.length > 1) {
                simpleRules.parse(new InputStreamReader(new FileInputStream(ruleFilePath), StandardCharsets.UTF_8));
//            } else {
                //all classes
//                simpleRules.parse(new StringReader("class *"));
//            }

//             final MappingReader mappingReader = new MappingReader(AabProtect.getAabProguardMapping(aab));
            final MappingReader mappingReader = new MappingReader(new File(mappingFilePath));
            filterConfig = new ProguardMappingConfig(filterConfig, mappingReader, simpleRules);


            final ClassAnalyzer classAnalyzer = new ClassAnalyzer();
            //todo 可能需要加载某些厂商私有的sdk


            final AabFolders aabFolders = new AabFolders(aab, outDir);

            final AabProtect aarProtect = new AabProtect.Builder(aabFolders)
                    .setInstructionRewriter(new RandomInstructionRewriter())
                    .setFilter(filterConfig)
                    .setClassAnalyzer(classAnalyzer)
                    .build();
            aarProtect.run();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

}
