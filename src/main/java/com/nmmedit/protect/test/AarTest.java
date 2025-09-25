package com.nmmedit.protect.test;

import com.nmmedit.apkprotect.aar.AarFolders;
import com.nmmedit.apkprotect.aar.AarProtect;
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
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

public class AarTest {

    public static void main(String[] args) {

        try {

            final File aar = new File("G:\\androidDemo2\\nmmp-master\\nmmp-master\\nmm-protect\\build\\libs\\alipayApp-1.2.4.aar");
            final File outDir = new File(aar.getParentFile(), "build");

            ClassAndMethodFilter filterConfig = new BasicKeepConfig();
            final SimpleRules simpleRules = new SimpleRules();
//            if (args.length > 1) {
                simpleRules.parse(new InputStreamReader(new FileInputStream("G:\\androidDemo2\\nmmp-master\\nmmp-master\\nmm-protect\\build\\libs\\aarRules.txt"),
                        StandardCharsets.UTF_8));
//            } else {
                //all classes
//                simpleRules.parse(new StringReader("class *"));
//            }

            if (args.length > 2) {
                final MappingReader mappingReader = new MappingReader(new File(args[2]));
                filterConfig = new ProguardMappingConfig(filterConfig, mappingReader, simpleRules);
            } else {
                filterConfig = new SimpleConvertConfig(new BasicKeepConfig(), simpleRules);
            }

            final ClassAnalyzer classAnalyzer = new ClassAnalyzer();
            //todo 可能需要加载某些厂商私有的sdk


            final AarFolders aarFolders = new AarFolders(aar, outDir);

            final AarProtect aarProtect = new AarProtect.Builder(aarFolders)
                    .setInstructionRewriter(new RandomInstructionRewriter())
                    .setFilter(filterConfig)
                    .setClassAnalyzer(classAnalyzer)
                    .build();
            aarProtect.run();
        }catch (Exception e) {
            e.printStackTrace();
        }


    }

}
