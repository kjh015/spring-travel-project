package com.traveler.logpipeline.kafka.service;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

public class JavaMethodExecutor {

    /**
     * 전달받은 Java 메서드 코드를 EvalRunner 클래스로 감싸서
     * 메모리에서 컴파일 → 리플렉션으로 evaluate(int) 메서드 실행 후 결과 반환
     */
    public static boolean  compileAndRunMethod(String methodCode, Class<?>[] paramTypes, Object[] paramValues) throws Exception {
        String className = "EvalRunner"; // 컴파일할 클래스명
        String fullCode = """
            public class EvalRunner {
                %s
            }
        """.formatted(methodCode); // 전달받은 메서드 코드를 EvalRunner 클래스 안에 삽입

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler(); // 자바 컴파일러 객체 획득
        if (compiler == null) {
            throw new IllegalStateException("JDK에서 실행해야 합니다 (JRE에서는 JavaCompiler 사용 불가)");
        }

        // 자바 소스코드를 JavaFileObject로 감쌈
        JavaFileObject javaFile = new InMemoryJavaFileObject(className, fullCode);

        // 컴파일 오류 수집기
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        // 표준 파일 매니저 및 메모리 전용 파일 매니저 생성
        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        MemoryJavaFileManager fileManager = new MemoryJavaFileManager(standardFileManager);

        // 컴파일 작업 정의
        JavaCompiler.CompilationTask task = compiler.getTask(
                null,                    // 출력 Writer (null이면 System.err)
                fileManager,             // 커스텀 파일 매니저
                diagnostics,             // 진단 결과 저장
                null, null,              // 옵션, 클래스 이름 (필요 없음)
                List.of(javaFile)        // 컴파일할 JavaFileObject
        );

        // 컴파일 실행
        if (!task.call()) {
            StringBuilder sb = new StringBuilder("컴파일 에러:\n");
            diagnostics.getDiagnostics().forEach(d -> sb.append(d.getMessage(null)).append("\n"));
            throw new IllegalStateException(sb.toString());
        }

        // 클래스 로더를 통해 EvalRunner 로드
        ClassLoader loader = fileManager.getClassLoader(null);
        Class<?> cls = loader.loadClass(className); // EvalRunner 클래스 로드

        // 인스턴스 생성
        Object instance = cls.getConstructor().newInstance();

        // evaluate(int) 메서드 찾고 실행
        Method method = cls.getMethod("evaluate", paramTypes);
        Object result = method.invoke(instance, paramValues);

        if (!(result instanceof Boolean)) {
            throw new IllegalStateException("메서드는 boolean 값을 반환해야 합니다.");
        }

        return (boolean) result;
    }

    /**
     * 문자열 기반 Java 소스코드를 JavaFileObject로 감싸기 위한 클래스
     */
    static class InMemoryJavaFileObject extends SimpleJavaFileObject {
        private final String code;

        protected InMemoryJavaFileObject(String className, String code) {
            // URI 형식의 식별자로 JavaFileObject 생성
            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code; // 코드 내용 반환
        }
    }

    /**
     * 메모리 상에서 .class 파일을 저장하고 로딩할 수 있는 FileManager
     */
    static class MemoryJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
        private final Map<String, ByteArrayOutputStream> compiledClassData = new HashMap<>();

        protected MemoryJavaFileManager(StandardJavaFileManager fileManager) {
            super(fileManager); // 표준 파일 매니저 위임
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className,
                                                   JavaFileObject.Kind kind, FileObject sibling) {
            // 클래스 컴파일 결과를 저장할 OutputStream 준비
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            compiledClassData.put(className, baos); // 클래스 이름으로 저장

            // .class 출력을 메모리 스트림에 연결
            return new SimpleJavaFileObject(URI.create("bytes:///" + className), kind) {
                @Override
                public OutputStream openOutputStream() {
                    return baos;
                }
            };
        }

        @Override
        public ClassLoader getClassLoader(Location location) {
            // 메모리에 저장된 클래스 바이트코드를 로딩하는 커스텀 ClassLoader 반환
            return new ClassLoader() {
                @Override
                protected Class<?> findClass(String name) throws ClassNotFoundException {
                    ByteArrayOutputStream baos = compiledClassData.get(name);
                    if (baos == null) throw new ClassNotFoundException(name);

                    byte[] bytes = baos.toByteArray();
                    return defineClass(name, bytes, 0, bytes.length); // 바이트코드 → 클래스 로딩
                }
            };
        }
    }
}
