package com.traveler.logpipeline.kafka.service;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

public class JavaMethodExecutor {

    public static String compileAndRunMethod(String methodCode, int inputValue) throws Exception {
        String className = "EvalRunner";
        String fullCode = """
            public class EvalRunner {
                %s
            }
        """.formatted(methodCode);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        JavaFileObject javaFile = new InMemoryJavaFileObject(className, fullCode);
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        MemoryJavaFileManager fileManager = new MemoryJavaFileManager(standardFileManager);

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, List.of(javaFile));
        if (!task.call()) {
            StringBuilder sb = new StringBuilder("컴파일 에러:\n");
            for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                sb.append(diagnostic.getMessage(null)).append("\n");
            }
            return sb.toString();
        }

        // 클래스 로딩 후 메서드 실행
        ClassLoader loader = fileManager.getClassLoader(null);
        Class<?> cls = loader.loadClass(className);
        Object instance = cls.getConstructor().newInstance();
        Method method = cls.getMethod("evaluate", int.class);
        Object result = method.invoke(instance, inputValue);

        return String.valueOf(result);
    }

    // 내부 클래스 1: InMemory Java File
    static class InMemoryJavaFileObject extends SimpleJavaFileObject {
        private final String code;

        protected InMemoryJavaFileObject(String className, String code) {
            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    // 내부 클래스 2: 메모리 기반 JavaFileManager
    static class MemoryJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
        private final Map<String, ByteArrayOutputStream> compiledClassData = new HashMap<>();

        protected MemoryJavaFileManager(StandardJavaFileManager fileManager) {
            super(fileManager);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            compiledClassData.put(className, baos);
            return new SimpleJavaFileObject(URI.create("bytes:///" + className), kind) {
                @Override
                public OutputStream openOutputStream() {
                    return baos;
                }
            };
        }

        @Override
        public ClassLoader getClassLoader(Location location) {
            return new ClassLoader() {
                @Override
                protected Class<?> findClass(String name) throws ClassNotFoundException {
                    ByteArrayOutputStream baos = compiledClassData.get(name);
                    if (baos == null) throw new ClassNotFoundException(name);
                    byte[] bytes = baos.toByteArray();
                    return defineClass(name, bytes, 0, bytes.length);
                }
            };
        }
    }
}

