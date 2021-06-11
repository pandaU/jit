package com.xxzx.jit.jit.jitDemo;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyClassLoader extends ClassLoader {
    public MyClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected Class<?> findClass(String path) throws ClassNotFoundException {
        byte[] bytes= new byte[0];
        try {
            bytes = loadBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String name = path;
        if (path.contains("E:\\jitClass\\")) {
            final String[] split = path.split("\\\\");
            String last = split[split.length - 1];
            String[] split1 = last.split("\\.");
            String suffix = split1[0];
            name = "com.xxzx.jit.jit." + suffix;
        }
        return defineClass(name,bytes,0,bytes.length);
    }

    public static byte[] loadBytes(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        return  bytes;
    }
}
