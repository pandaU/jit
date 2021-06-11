package com.xxzx.jit.jit.jitDemo;


import com.xxzx.jit.jit.config.PathConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyClassLoader extends ClassLoader {
    public MyClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected Class<?> findClass(String path) throws ClassNotFoundException {
        byte[] bytes = loadBytes(path);
        String name = path;
        if (path.contains(PathConfig.EXT_JAVA_DIR)) {
            String line = readFirstLine(path.replace(".class", ".java"));
            line = line.trim();
            final String[] split = path.split("\\\\");
            String last = split[split.length - 1];
            String[] split1 = last.split("\\.");
            String suffix = split1[0];
            if (line == null || line.isEmpty() || !line.startsWith(PathConfig.PACKAGE)){
                name = suffix;
            }else {
                String prefix = line.replace(PathConfig.PACKAGE,"").trim();
                name = prefix + "." + suffix;
            }

        }
        return defineClass(name,bytes,0,bytes.length);
    }

    public static byte[] loadBytes(String path){
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
        }
        return  bytes;
    }

    public static String readFirstLine(String path){
        String firstLine = null;
        try {
            BufferedReader bf = new BufferedReader(new FileReader(path));
            firstLine = bf.readLine();
            bf.close();
        } catch (IOException e) {
        }
        return firstLine;
    }
}
