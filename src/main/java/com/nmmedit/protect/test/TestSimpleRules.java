package com.nmmedit.protect.test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class TestSimpleRules {
    private final HashMap<ClassRule, MethodRule> convertRules = new HashMap<>();


    public TestSimpleRules() {
    }

    public void parse(Reader ruleReader) throws IOException {
        try (BufferedReader reader = new BufferedReader(ruleReader)) {
            TestSimpleRules.ClassRule classRule = null;
            final ArrayList<String> methodNameList = new ArrayList<>();
            boolean methodParsing = false;
            int lineNumb = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if ("".equals(line)) {//empty line
                    lineNumb++;
                    continue;
                }
                out("line="+line);
                if (line.startsWith("class")) {
                    final String[] split = line.split(" +");
                    final int length = split.length;
                    if (length < 2) {
                        throw new RemoteException("Error rule " + lineNumb + ": " + line);
                    }

                    out("长度是"+length);
                    for (int i=0;i<length;i++) {
                        out("值是"+split[i]);
                    }

                    String className = split[1];
                    String supperName = "";
                    String interfaceName = "";
                    if (length >= 4) {
                        if ("extends".equals(split[2])) {//class * extends A
                            supperName = split[3];
                        } else if ("implements".equals(split[2])) {//class * implements I
                            interfaceName = split[3];
                        }
                    }
                    out("className=" + className);

                    classRule = new TestSimpleRules.ClassRule(className, supperName, interfaceName);
                    int mstart;
                    if ((mstart = line.indexOf('{')) != -1) { // my.pkg.A { methodA;methodB;}
                        int mend;
                        if ((mend = line.indexOf('}')) != -1) {
                            final String[] methodNames = line.substring(mstart + 1, mend).trim().split(";");
                            if (methodNames.length == 0) {
                                throw new RemoteException("Error rule " + lineNumb + ": " + line);
                            }
                            for (String name : methodNames) {
                                out("name=" + name);
                                convertRules.put(classRule, new TestSimpleRules.MethodRule(name));
                            }
                        } else {
                            methodNameList.clear();
                            methodParsing = true;
                        }
                    } else {
                        //any methods
                        convertRules.put(classRule, new TestSimpleRules.MethodRule("*"));
                    }
                } else if (methodParsing) {
                    // my.pkg.A {
                    //   methodA;
                    //   methodB;
                    // }
                    if (line.indexOf('}') != -1) {
                        if (methodNameList.isEmpty()) {
                            throw new RemoteException("Error rule " + lineNumb + ": " + line);
                        }
                        for (String methodName : methodNameList) {
                            if ("".equals(methodName)) {
                                continue;
                            }
                            convertRules.put(classRule, new TestSimpleRules.MethodRule(methodName));
                        }
                        methodParsing = false;
                    } else {
                        methodNameList.add(line.replace(";", ""));
                    }
                } else {
                    throw new RemoteException("Error rule " + lineNumb + ": " + line);
                }

                lineNumb++;
            }
        }
    }

    private Set<TestSimpleRules.MethodRule> methodRules;

    /***
     * 举例 !com.kwad,!com.qq,com.*
     * 如果是com.kwad.a这个类，那么就要返回false
     * 如果是com.a 这个类，那么就要返回true
     * 如果是 com.qq这个类，那么就要返回false
     * @param classType
     * @param supperType
     * @param ifacTypes
     * @return
     */
    public boolean matchClass( String classType,  String supperType,  List<String> ifacTypes) {
        for (TestSimpleRules.ClassRule rule : convertRules.keySet()) {
            String typeRegex = null;
            if(rule.className.contains(",")) {
                String[] split = rule.className.split(",");
                for (int i = 0;i<split.length;i++) {
                    String str = split[i];
                    if (str.startsWith("!")) {
                        String newStr = str.replace("!",""); // 举例：com.kwad
                        typeRegex = toRegex(classNameToType(newStr));

                        if(classType.matches(typeRegex)) {
                            return false;
                        }

                    }
                }
            }
            typeRegex = toRegex(classNameToType(rule.className));

            if (classType.matches(typeRegex)) {// match classType
                if (!"".equals(rule.supperName)) {//supper name not empty
                    if (supperType != null) {
                        final String type = classNameToType(rule.supperName);
                        if (supperType.equals(type)) {
//                            methodRules = convertRules.get(rule);
                            return true;
                        }
                    }
                    continue;
                }
                if (!"".equals(rule.interfaceName)) {//interface name not empty
                    for (String iface : ifacTypes) {
                        if (iface.equals(classNameToType(rule.interfaceName))) {
//                            methodRules = convertRules.get(rule);
                            return true;
                        }
                    }
                    continue;
                }
//                methodRules = convertRules.get(rule);
                return true;
            }
        }
        methodRules = null;
        return false;
    }

    public boolean matchMethod(String methodName) {
        if (methodRules == null || methodName == null) {
            return false;
        }
        for (TestSimpleRules.MethodRule methodRule : methodRules) {
            if (methodName.matches(toRegex(methodRule.methodName))) {
                return true;
            }
        }
        return false;
    }

    private static String classNameToType(String className) {
        // Lcom/;
        return "L" + className.replace('.', '/') + ";";
    }


    private static String toRegex(String s) {
        final StringBuilder sb = new StringBuilder(s.length() + 3);
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            switch (c) {
                case '*':
                    sb.append('.');
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }


    private void out(String msg) {
        System.out.println("msg = " + msg);
    }


    private static class ClassRule {

        private final String className;
        //supper class

        private final String supperName;
        //interface

        private final String interfaceName;

        public ClassRule( String className) {
            this(className, "", "");
        }

        public ClassRule( String className,  String supperName,  String interfaceName) {
            this.className = className;
            this.supperName = supperName;
            this.interfaceName = interfaceName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestSimpleRules.ClassRule classRule = (TestSimpleRules.ClassRule) o;

            if (!className.equals(classRule.className)) return false;
            if (!supperName.equals(classRule.supperName)) return false;
            return interfaceName.equals(classRule.interfaceName);
        }

        @Override
        public int hashCode() {
            int result = className.hashCode();
            result = 31 * result + supperName.hashCode();
            result = 31 * result + interfaceName.hashCode();
            return result;
        }
    }

    private static class MethodRule {

        private final String methodName;
        // args ?
        // private final List<String> args;

        public MethodRule( String methodName) {
            this.methodName = methodName;
        }
    }

}
