package com.nmmedit.apkprotect;

import com.android.tools.smali.dexlib2.dexbacked.DexBackedClassDef;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.DexFile;
import com.android.tools.smali.dexlib2.writer.io.FileDataStore;
import com.android.tools.smali.dexlib2.writer.pool.DexPool;
import com.android.zipflinger.Source;
import com.android.zipflinger.Sources;
import com.android.zipflinger.ZipArchive;
import com.android.zipflinger.ZipMap;
import com.android.zipflinger.ZipSource;
import com.nmmedit.apkprotect.andres.AxmlEdit;
import com.nmmedit.apkprotect.data.Prefs;
import com.nmmedit.apkprotect.dex2c.Dex2c;
import com.nmmedit.apkprotect.dex2c.DexConfig;
import com.nmmedit.apkprotect.dex2c.GlobalDexConfig;
import com.nmmedit.apkprotect.dex2c.converter.ClassAnalyzer;
import com.nmmedit.apkprotect.dex2c.converter.instructionrewriter.InstructionRewriter;
import com.nmmedit.apkprotect.dex2c.converter.structs.RegisterNativesCallerClassDef;
import com.nmmedit.apkprotect.dex2c.converter.structs.RegisterNativesUtilClassDef;
import com.nmmedit.apkprotect.dex2c.filters.ClassAndMethodFilter;
import com.nmmedit.apkprotect.util.ApkUtils;
import com.nmmedit.apkprotect.util.CmakeUtils;
import com.nmmedit.apkprotect.util.FileUtils;

import javax.annotation.Nonnull;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ApkProtectV2 {

    public static final String ANDROID_MANIFEST_XML = "AndroidManifest.xml";
    public static final String ANDROID_APP_APPLICATION = "android.app.Application";

    private final ApkFolders apkFolders;
    private final InstructionRewriter instructionRewriter;
    private final ClassAndMethodFilter filter;
    private final ClassAnalyzer classAnalyzer;

    private ApkProtectV2(ApkFolders apkFolders,
                         InstructionRewriter instructionRewriter,
                         ClassAndMethodFilter filter,
                         ClassAnalyzer classAnalyzer) {
        this.apkFolders = apkFolders;
        this.instructionRewriter = instructionRewriter;
        this.filter = filter;
        this.classAnalyzer = classAnalyzer;
        BuildNativeLib.init_nmmp_name();
    }

    public void run() throws IOException {
        final File apkFile = apkFolders.getInApk();
        final File zipExtractDir = apkFolders.getZipExtractTempDir();

        try {
            byte[] manifestBytes = ApkUtils.getFile(apkFile, ANDROID_MANIFEST_XML);
            if (manifestBytes == null) {
                throw new RuntimeException("Not is apk");
            }

            final String packageName = AxmlEdit.getPackageName(manifestBytes);

            CmakeUtils.generateCSources(apkFolders.getDex2cSrcDir(), instructionRewriter);

            List<File> files = getClassesFiles(apkFile, zipExtractDir);
            if (files.isEmpty()) {
                throw new RuntimeException("No classes.dex");
            }

            final int minSdk = AxmlEdit.getMinSdk(manifestBytes);
            classAnalyzer.setMinSdk(minSdk);

            for (File file : files) {
                classAnalyzer.loadDexFile(file);
            }

            final GlobalDexConfig globalConfig = Dex2c.handleAllDex(
                    files,
                    filter,
                    instructionRewriter,
                    classAnalyzer,
                    apkFolders.getCodeGeneratedDir());

            final Set<String> mainDexClassTypeSet = new HashSet<>();
            collectStartupClasses(mainDexClassTypeSet, globalConfig, packageName, manifestBytes);

            final ArrayList<File> outDexFiles = injectInstructionAndWriteToFile(
                    globalConfig,
                    mainDexClassTypeSet,
                    60000,
                    apkFolders.getTempDexDir());

            final List<String> abis = getAbis(apkFile);
            final Map<String, Map<File, File>> nativeLibs =
                    BuildNativeLib.generateNativeLibs(apkFolders.getOutRootDir(), abis);

            File mainDex = outDexFiles.get(0);
            final File newMainDex = internNativeUtilClassDef(mainDex, globalConfig, BuildNativeLib.NMMP_NAME);
            outDexFiles.set(0, newMainDex);

            final File outputApk = apkFolders.getOutputApk();
            if (outputApk.exists()) {
                outputApk.delete();
            }

            try (ZipArchive zipArchive = new ZipArchive(outputApk.toPath())) {
                final ZipMap zipMap = ZipMap.from(apkFile.toPath());
                zipCopy(zipMap, zipArchive);

                final Source androidManifestSource = Sources.from(
                        new ByteArrayInputStream(manifestBytes),
                        ANDROID_MANIFEST_XML,
                        Deflater.DEFAULT_COMPRESSION);
                androidManifestSource.align(4);
                zipArchive.add(androidManifestSource);

                for (File file : outDexFiles) {
                    final Source source = Sources.from(file, file.getName(), Deflater.DEFAULT_COMPRESSION);
                    source.align(4);
                    zipArchive.add(source);
                }

                for (Map.Entry<String, Map<File, File>> entry : nativeLibs.entrySet()) {
                    final String abi = entry.getKey();
                    for (File file : entry.getValue().values()) {
                        final Source source = Sources.from(
                                file,
                                "lib/" + abi + "/" + file.getName(),
                                Deflater.DEFAULT_COMPRESSION);
                        source.align(4);
                        zipArchive.add(source);
                    }
                }
            }
        } finally {
            FileUtils.deleteFile(zipExtractDir);
        }
    }

    private static List<String> getAbis(File apk) throws IOException {
        final Pattern pattern = Pattern.compile("lib/(.*)/.*\\.so");
        final ZipFile zipFile = new ZipFile(apk);
        final Enumeration<? extends ZipEntry> entries = zipFile.entries();
        Set<String> abis = new HashSet<>();
        while (entries.hasMoreElements()) {
            final ZipEntry entry = entries.nextElement();
            final Matcher matcher = pattern.matcher(entry.getName());
            if (matcher.matches()) {
                abis.add(matcher.group(1));
            }
        }
        abis.remove("armeabi");
        if (abis.isEmpty()) {
            ArrayList<String> abi = new ArrayList<>();
            if (Prefs.isArm()) {
                abi.add("armeabi-v7a");
            }
            if (Prefs.isArm64()) {
                abi.add("arm64-v8a");
            }
            if (Prefs.isX86()) {
                abi.add("x86");
            }
            if (Prefs.isX64()) {
                abi.add("x86_64");
            }
            return abi;
        }
        return new ArrayList<>(abis);
    }

    private static List<File> getClassesFiles(File apkFile, File zipExtractDir) throws IOException {
        List<File> files = ApkUtils.extractFiles(apkFile, "classes(\\d+)*\\.dex", zipExtractDir);
        files.sort((file, t1) -> {
            final String numb = file.getName().replace("classes", "").replace(".dex", "");
            final String numb2 = t1.getName().replace("classes", "").replace(".dex", "");
            int n = "".equals(numb) ? 0 : Integer.parseInt(numb);
            int n2 = "".equals(numb2) ? 0 : Integer.parseInt(numb2);
            return n - n2;
        });
        return files;
    }

    private static File dexWriteToFile(DexPool dexPool, int index, File dexOutDir) throws IOException {
        if (!dexOutDir.exists()) {
            dexOutDir.mkdirs();
        }

        File outDexFile;
        if (index == 0) {
            outDexFile = new File(dexOutDir, "classes.dex");
        } else {
            outDexFile = new File(dexOutDir, String.format("classes%d.dex", index + 1));
        }
        dexPool.writeTo(new FileDataStore(outDexFile));
        return outDexFile;
    }

    private static List<String> getApplicationClassesFromMainDex(GlobalDexConfig globalConfig,
                                                                 String applicationClass) throws IOException {
        return getClassHierarchyFromShellDex(globalConfig, applicationClass, ANDROID_APP_APPLICATION);
    }

    private static List<String> getClassHierarchyFromShellDex(GlobalDexConfig globalConfig,
                                                              String className,
                                                              String stopSuperClass) throws IOException {
        final List<String> mainDexClassList = new ArrayList<>();
        String tmpType = classDotNameToType(className);
        final String stopType = classDotNameToType(stopSuperClass);
        mainDexClassList.add(tmpType);
        for (DexConfig config : globalConfig.getConfigs()) {
            DexBackedDexFile dexFile = DexBackedDexFile.fromInputStream(
                    null,
                    new BufferedInputStream(new FileInputStream(config.getShellDexFile())));
            final Set<? extends DexBackedClassDef> classes = dexFile.getClasses();
            ClassDef classDef;
            while (true) {
                classDef = getClassDefFromType(classes, tmpType);
                if (classDef == null) {
                    break;
                }
                if (stopType.equals(classDef.getSuperclass())) {
                    return mainDexClassList;
                }
                tmpType = classDef.getSuperclass();
                mainDexClassList.add(tmpType);
            }
        }
        return mainDexClassList;
    }

    private static ClassDef getClassDefFromType(Set<? extends ClassDef> classDefSet, String type) {
        for (ClassDef classDef : classDefSet) {
            if (classDef.getType().equals(type)) {
                return classDef;
            }
        }
        return null;
    }

    public static ArrayList<File> injectInstructionAndWriteToFile(GlobalDexConfig globalConfig,
                                                                  Set<String> mainClassSet,
                                                                  int maxPoolSize,
                                                                  File dexOutDir) throws IOException {
        final ArrayList<File> dexFiles = new ArrayList<>();
        InjectionState state = new InjectionState();
        final List<DexConfig> configs = globalConfig.getConfigs();

        if (!mainClassSet.isEmpty()) {
            for (DexConfig config : configs) {
                state = internShellDexClasses(config, mainClassSet, true, maxPoolSize, dexOutDir, dexFiles, state);
            }
        }

        for (DexConfig config : configs) {
            state = internShellDexClasses(config, mainClassSet, false, maxPoolSize, dexOutDir, dexFiles, state);
        }

        if (state.hasClasses) {
            final int size = dexFiles.size();
            final File file = dexWriteToFile(state.dexPool, size, dexOutDir);
            dexFiles.add(file);
        }

        return dexFiles;
    }

    private static InjectionState internShellDexClasses(DexConfig config,
                                                        Set<String> classTypeSet,
                                                        boolean includeSelected,
                                                        int maxPoolSize,
                                                        File dexOutDir,
                                                        ArrayList<File> dexFiles,
                                                        InjectionState state) throws IOException {
        DexBackedDexFile dexNativeFile = DexBackedDexFile.fromInputStream(
                null,
                new BufferedInputStream(new FileInputStream(config.getShellDexFile())));

        if (state.dexPool == null) {
            state.dexPool = new DexPool(dexNativeFile.getOpcodes());
        }

        for (ClassDef classDef : dexNativeFile.getClasses()) {
            final boolean selected = classTypeSet.contains(classDef.getType());
            if (selected != includeSelected) {
                continue;
            }
            internClass(config, state.dexPool, classDef);
            state.hasClasses = true;
            if (state.dexPool.hasOverflowed(maxPoolSize)) {
                final int size = dexFiles.size();
                final File file = dexWriteToFile(state.dexPool, size, dexOutDir);
                dexFiles.add(file);
                state.dexPool = new DexPool(dexNativeFile.getOpcodes());
                state.hasClasses = false;
            }
        }
        return state;
    }

    public static void copyDex(@Nonnull DexFile oldDexFile, @Nonnull DexPool newDex) {
        for (ClassDef classDef : oldDexFile.getClasses()) {
            newDex.internClass(classDef);
        }
    }

    private static void internClass(DexConfig config, DexPool dexPool, ClassDef classDef) {
        final Set<String> classes = config.getHandledNativeClasses();
        final String type = classDef.getType();
        final String className = type.substring(1, type.length() - 1);
        if (classes.contains(className)) {
            final RegisterNativesCallerClassDef nativeClassDef = new RegisterNativesCallerClassDef(
                    classDef,
                    config.getOffsetFromClassName(className),
                    "L" + config.getRegisterNativesClassName() + ";",
                    config.getRegisterNativesMethodName());
            dexPool.internClass(nativeClassDef);
        } else {
            dexPool.internClass(classDef);
        }
    }

    public static File internNativeUtilClassDef(@Nonnull File mainDex,
                                                @Nonnull GlobalDexConfig globalConfig,
                                                @Nonnull String libName) throws IOException {
        DexFile mainDexFile = DexBackedDexFile.fromInputStream(
                null,
                new BufferedInputStream(new FileInputStream(mainDex)));

        DexPool newDex = new DexPool(mainDexFile.getOpcodes());
        copyDex(mainDexFile, newDex);

        final ArrayList<String> nativeMethodNames = new ArrayList<>();
        for (DexConfig config : globalConfig.getConfigs()) {
            nativeMethodNames.add(config.getRegisterNativesMethodName());
        }

        newDex.internClass(new RegisterNativesUtilClassDef(
                "L" + globalConfig.getConfigs().get(0).getRegisterNativesClassName() + ";",
                nativeMethodNames,
                libName));

        final File injectLoadLib = new File(mainDex.getParent(), "injectLoadLib");
        if (!injectLoadLib.exists()) {
            injectLoadLib.mkdirs();
        }

        final File newFile = new File(injectLoadLib, mainDex.getName());
        newDex.writeTo(new FileDataStore(newFile));
        return newFile;
    }

    private static void zipCopy(ZipMap zipMap, ZipArchive outArchive) throws IOException {
        final Pattern regex = Pattern.compile(
                "classes(\\d)*\\.dex" +
                        "|META-INF/.*\\.(RSA|DSA|EC|SF|MF)" +
                        "|AndroidManifest\\.xml");
        final ZipSource zipSource = new ZipSource(zipMap);
        for (String entryName : zipMap.getEntries().keySet()) {
            if (regex.matcher(entryName).matches()) {
                continue;
            }
            zipSource.select(entryName, entryName, ZipSource.COMPRESSION_NO_CHANGE, 4);
        }
        outArchive.add(zipSource);
    }

    private static String classDotNameToType(String classDotName) {
        return "L" + classDotName.replace('.', '/') + ";";
    }

    private static void collectStartupClasses(Set<String> mainDexClassTypeSet,
                                              GlobalDexConfig globalConfig,
                                              String packageName,
                                              byte[] manifestBytes) throws IOException {
        final String applicationClass = normalizeManifestClassName(
                packageName,
                AxmlEdit.getApplicationName(manifestBytes));
        if (!applicationClass.isEmpty()) {
            mainDexClassTypeSet.addAll(getApplicationClassesFromMainDex(globalConfig, applicationClass));
        }

        final String appComponentFactory = normalizeManifestClassName(
                packageName,
                AxmlEdit.getAppComponentFactory(manifestBytes));
        if (!appComponentFactory.isEmpty()) {
            mainDexClassTypeSet.addAll(
                    getClassHierarchyFromShellDex(globalConfig, appComponentFactory, "android.app.AppComponentFactory"));
        }

        addClassIfPresent(mainDexClassTypeSet, globalConfig, "androidx.core.app.CoreComponentFactory");
        addClassIfPresent(mainDexClassTypeSet, globalConfig, "android.support.v4.app.CoreComponentFactory");
    }

    private static void addClassIfPresent(Set<String> classTypeSet,
                                          GlobalDexConfig globalConfig,
                                          String className) throws IOException {
        final String classType = classDotNameToType(className);
        for (DexConfig config : globalConfig.getConfigs()) {
            DexBackedDexFile dexFile = DexBackedDexFile.fromInputStream(
                    null,
                    new BufferedInputStream(new FileInputStream(config.getShellDexFile())));
            if (getClassDefFromType(dexFile.getClasses(), classType) != null) {
                classTypeSet.add(classType);
                return;
            }
        }
    }

    private static String normalizeManifestClassName(String packageName, String className) {
        if (className == null || className.isEmpty()) {
            return "";
        }
        if (className.startsWith(".")) {
            return packageName + className;
        }
        if (className.indexOf('.') == -1) {
            return packageName + "." + className;
        }
        return className;
    }

    private static final class InjectionState {
        private DexPool dexPool;
        private boolean hasClasses;
    }

    public static class Builder {
        private final ApkFolders apkFolders;
        private InstructionRewriter instructionRewriter;
        private ClassAndMethodFilter filter;
        private ClassAnalyzer classAnalyzer;

        public Builder(ApkFolders apkFolders) {
            this.apkFolders = apkFolders;
        }

        public Builder setInstructionRewriter(InstructionRewriter instructionRewriter) {
            this.instructionRewriter = instructionRewriter;
            return this;
        }

        public Builder setFilter(ClassAndMethodFilter filter) {
            this.filter = filter;
            return this;
        }

        public Builder setClassAnalyzer(ClassAnalyzer classAnalyzer) {
            this.classAnalyzer = classAnalyzer;
            return this;
        }

        public ApkProtectV2 build() {
            if (instructionRewriter == null) {
                throw new RuntimeException("instructionRewriter == null");
            }
            if (classAnalyzer == null) {
                throw new RuntimeException("classAnalyzer==null");
            }
            return new ApkProtectV2(apkFolders, instructionRewriter, filter, classAnalyzer);
        }
    }
}
