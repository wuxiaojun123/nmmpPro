package com.nmmedit.protect;


import com.nmmedit.apkprotect.ApkFolders;
import com.nmmedit.apkprotect.ApkProtectV2;
import com.nmmedit.apkprotect.data.Prefs;
import com.nmmedit.apkprotect.deobfus.MappingReader;
import com.nmmedit.apkprotect.dex2c.converter.ClassAnalyzer;
import com.nmmedit.apkprotect.dex2c.converter.instructionrewriter.RandomInstructionRewriter;
import com.nmmedit.apkprotect.dex2c.filters.*;

import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.Method;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class ApkMain {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("No Input apk.");
            System.err.println("<inApk> [<convertRuleFile> mapping.txt]");
            return;
        }
        final File apk = new File(args[0]);
        final File outDir = new File(apk.getParentFile(), "build");

        // 创建Compose保护过滤器
        ClassAndMethodFilter composeProtectFilter = new ComposeProtectFilter();

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

        // 组合过滤器：先通过Compose保护过滤器，再通过其他过滤器
        filterConfig = new ComposeProtectFilterWrapper(composeProtectFilter, filterConfig);
        System.out.println("已启用Compose保护过滤器");
        System.out.println("保护策略: 优先保护Compose相关类，然后应用其他转换规则");

        if (args.length > 3) {
            String configWindowsName = args[3];
            Prefs.CONFIG_WINDOWS_NAME = configWindowsName;
            System.out.println("传入的configWindowsName=" + configWindowsName + "Prefs.CONFIG_WINDOWS_NAME="+Prefs.CONFIG_WINDOWS_NAME);
        }

        if (args.length > 4) {
            String vmsrcZipName = args[4];
            Prefs.VMSRC_NAME = vmsrcZipName;
            System.out.println("传入的vmsrcName="+vmsrcZipName + "Prefs.VMSRC_NAME="+ Prefs.VMSRC_NAME);
        }

        final ClassAnalyzer classAnalyzer = new ClassAnalyzer();
        //todo 可能需要加载某些厂商私有的sdk


        final ApkFolders apkFolders = new ApkFolders(apk, outDir);


        final ApkProtectV2 apkProtect = new ApkProtectV2.Builder(apkFolders)
                .setInstructionRewriter(new RandomInstructionRewriter())
                .setFilter(filterConfig)
                .setClassAnalyzer(classAnalyzer)
                .build();
        apkProtect.run();
    }


    /**
     * Compose 保护过滤器，专门保护 Compose 相关的类、接口和方法。
     */
    private static class ComposeProtectFilter implements ClassAndMethodFilter {
        private static final String COMPOSER_TYPE = "Landroidx/compose/runtime/Composer;";
        private final Set<String> protectedClasses = new HashSet<>();
        private final Set<String> protectedMethods = new HashSet<>();
        private int protectedClassCount = 0;
        private int protectedMethodCount = 0;

        public ComposeProtectFilter() {
            loadProtectionRules();
        }

        private void loadProtectionRules() {
            try {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("compose-protect-rules.txt");
                if (inputStream != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            line = line.trim();
                            if (line.isEmpty() || line.startsWith("#")) {
                                continue;
                            }
                            if (line.startsWith("class ")) {
                                String className = line.substring(6).trim();
                                if (className.endsWith(".*")) {
                                    className = className.substring(0, className.length() - 2);
                                }
                                protectedClasses.add(normalizeClassPattern(className));
                            }
                        }
                    }
                    System.out.println("已加载 Compose 保护规则，保护类数量: " + protectedClasses.size());
                    System.out.println("保护类前缀示例: " + protectedClasses.stream().limit(5).toArray());
                } else {
                    loadDefaultProtectionRules();
                    System.out.println("使用默认 Compose 保护规则");
                }
            } catch (Exception e) {
                System.err.println("加载 Compose 保护规则失败，使用默认规则: " + e.getMessage());
                loadDefaultProtectionRules();
            }
        }

        private void loadDefaultProtectionRules() {
            addProtectedClassPrefix("androidx.compose");
            addProtectedClassPrefix("androidx.compose.ui");
            addProtectedClassPrefix("androidx.compose.foundation");
            addProtectedClassPrefix("androidx.compose.material");
            addProtectedClassPrefix("androidx.compose.runtime");
            addProtectedClassPrefix("androidx.compose.animation");
            addProtectedClassPrefix("androidx.compose.ui.graphics");
            addProtectedClassPrefix("androidx.compose.ui.text");
            addProtectedClassPrefix("androidx.compose.ui.unit");
            addProtectedClassPrefix("androidx.compose.ui.layout");
            addProtectedClassPrefix("androidx.compose.ui.input");
            addProtectedClassPrefix("androidx.compose.ui.viewinterop");
            addProtectedClassPrefix("androidx.compose.ui.platform");
            addProtectedClassPrefix("androidx.compose.ui.tooling");
            addProtectedClassPrefix("androidx.compose.ui.test");
            addProtectedClassPrefix("androidx.compose.ui.window");
            addProtectedClassPrefix("androidx.compose.ui.desktop");
            addProtectedClassPrefix("androidx.compose.ui.experimental");
            addProtectedClassPrefix("androidx.compose.ui.autofill");
            addProtectedClassPrefix("androidx.compose.ui.haptic");
            addProtectedClassPrefix("androidx.compose.ui.semantics");
            addProtectedClassPrefix("androidx.compose.ui.state");
            addProtectedClassPrefix("androidx.compose.ui.gesture");
            addProtectedClassPrefix("androidx.compose.ui.focus");
            addProtectedClassPrefix("androidx.compose.ui.alignment");
            addProtectedClassPrefix("androidx.compose.ui.geometry");
            addProtectedClassPrefix("androidx.compose.ui.vector");
            addProtectedClassPrefix("androidx.compose.ui.res");
            addProtectedClassPrefix("androidx.compose.ui.accessibility");
            addProtectedClassPrefix("androidx.compose.ui.pointer");
            addProtectedClassPrefix("androidx.compose.ui.selection");
            addProtectedClassPrefix("androidx.compose.ui.system");
            addProtectedClassPrefix("androidx.compose.ui.verification");
            addProtectedClassPrefix("androidx.compose.ui.visual");

            protectedMethods.add("draw");
            protectedMethods.add("measure");
            protectedMethods.add("layout");
            protectedMethods.add("compose");
            protectedMethods.add("remember");
            protectedMethods.add("derivedStateOf");
            protectedMethods.add("produceState");
            protectedMethods.add("collectAsState");
            protectedMethods.add("LaunchedEffect");
            protectedMethods.add("DisposableEffect");
            protectedMethods.add("SideEffect");
            protectedMethods.add("snapshotFlow");
            protectedMethods.add("mutableStateOf");
            protectedMethods.add("mutableStateListOf");
            protectedMethods.add("mutableStateMapOf");
        }

        private void addProtectedClassPrefix(String className) {
            protectedClasses.add(normalizeClassPattern(className));
        }

        private String normalizeClassPattern(String className) {
            return className.replace('.', '/');
        }

        private String normalizeDexType(String dexType) {
            if (dexType == null || dexType.isEmpty()) {
                return "";
            }
            String normalized = dexType;
            if (normalized.startsWith("L") && normalized.endsWith(";")) {
                normalized = normalized.substring(1, normalized.length() - 1);
            }
            return normalized.replace('.', '/');
        }

        private boolean isProtectedComposeClass(String dexType) {
            final String normalizedType = normalizeDexType(dexType);
            for (String protectedPattern : protectedClasses) {
                if (normalizedType.equals(protectedPattern)
                        || normalizedType.startsWith(protectedPattern + "/")) {
                    return true;
                }
            }
            return false;
        }

        private boolean isViewModelClass(ClassDef classDef) {
            final String superClass = classDef.getSuperclass();
            return "Landroidx/lifecycle/ViewModel;".equals(superClass)
                    || "Landroidx/lifecycle/AndroidViewModel;".equals(superClass);
        }

        private boolean isLikelyComposeGeneratedClass(String dexType) {
            final String normalizedType = normalizeDexType(dexType);
            return normalizedType.contains("/ComposableSingletons$")
                    || normalizedType.endsWith("Kt")
                    || normalizedType.contains("$WhenMappings")
                    || normalizedType.contains("$DefaultImpls");
        }

        private boolean hasProtectedMethodPrefix(String methodName) {
            for (String protectedPrefix : protectedMethods) {
                if (methodName.startsWith(protectedPrefix)) {
                    return true;
                }
            }
            return false;
        }

        private boolean hasComposerParameter(Method method) {
            for (CharSequence parameterType : method.getParameterTypes()) {
                if (COMPOSER_TYPE.contentEquals(parameterType)) {
                    return true;
                }
            }
            return false;
        }

        private boolean hasComposableMethod(ClassDef classDef) {
            for (Method method : classDef.getMethods()) {
                if (hasComposerParameter(method)) {
                    return true;
                }
            }
            return false;
        }

        private boolean shouldProtectMethod(Method method) {
            final String definingClass = method.getDefiningClass();
            if (isProtectedComposeClass(definingClass)) {
                return true;
            }

            if (hasComposerParameter(method)) {
                return true;
            }

            final String methodName = method.getName();
            if (!hasProtectedMethodPrefix(methodName)) {
                return false;
            }

            final String normalizedType = normalizeDexType(definingClass);
            return normalizedType.contains("/compose/")
                    || normalizedType.contains("/ui/")
                    || normalizedType.contains("/runtime/")
                    || normalizedType.contains("/foundation/")
                    || normalizedType.contains("/material/")
                    || isLikelyComposeGeneratedClass(definingClass);
        }

        @Override
        public boolean acceptClass(ClassDef classDef) {
            final String cleanClassName = normalizeDexType(classDef.getType());
            if (isProtectedComposeClass(classDef.getType())
                    || hasComposableMethod(classDef)
                    || isViewModelClass(classDef)) {
                protectedClassCount++;
                System.out.println("保护 UI/Compose 类[" + protectedClassCount + "]: " + cleanClassName);

                if (cleanClassName.contains("DrawScope")) {
                    System.out.println("*** 特别保护 DrawScope 相关类: " + cleanClassName + " ***");
                }

                return false;
            }

            return true;
        }

        @Override
        public boolean acceptMethod(Method method) {
            final String methodName = method.getName();
            if (shouldProtectMethod(method)) {
                protectedMethodCount++;
                System.out.println("保护 Compose 方法 [" + protectedMethodCount + "]: "
                        + normalizeDexType(method.getDefiningClass()) + "#" + methodName);

                if (methodName.startsWith("draw")) {
                    System.out.println("*** 特别保护 draw 相关方法: " + methodName + " ***");
                }

                return false;
            }

            return true;
        }

        public void printProtectionSummary() {
            System.out.println("=== Compose 保护总结 ===");
            System.out.println("受保护的类数量: " + protectedClassCount);
            System.out.println("受保护的方法数量: " + protectedMethodCount);
            System.out.println("保护规则数量: " + protectedClasses.size());
            System.out.println("方法保护前缀数量: " + protectedMethods.size());
        }
    }

    /**
     * 过滤器包装器，组合多个过滤器
     */
    private static class ComposeProtectFilterWrapper implements ClassAndMethodFilter {
        private final ClassAndMethodFilter composeFilter;
        private final ClassAndMethodFilter otherFilter;

        public ComposeProtectFilterWrapper(ClassAndMethodFilter composeFilter, ClassAndMethodFilter otherFilter) {
            this.composeFilter = composeFilter;
            this.otherFilter = otherFilter;
        }

        @Override
        public boolean acceptClass(ClassDef classDef) {
            if (!composeFilter.acceptClass(classDef)) {
                return false;
            }
            return otherFilter.acceptClass(classDef);
        }

        @Override
        public boolean acceptMethod(Method method) {
            if (!composeFilter.acceptMethod(method)) {
                return false;
            }
            return otherFilter.acceptMethod(method);
        }
    }

    /**
     * 简单的文本工具类
     */
    private static class TextUtils {
        public static boolean isEmpty(String str) {
            return str == null || str.trim().isEmpty();
        }
    }

}
