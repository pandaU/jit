package com.xxzx.jit.jit.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClassUtil {
    public static void outJava() {
        String content = "package com.xxzx.jit.jit;\n" +
                "\n" +
                "import org.springframework.beans.factory.InitializingBean;\n" +
                "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                "import org.springframework.web.bind.annotation.RestController;\n" +
                "\n" +
                "@RestController\n" +
                "public class ActionController implements InitializingBean {\n" +
                "\n" +
                "    @Override\n" +
                "    public void afterPropertiesSet() throws Exception {\n" +
                "        System.out.println(\"hello\");\n" +
                "    }\n" +
                "\n" +
                "    @RequestMapping(\"/demo\")\n" +
                "    public String toAction(String name){\n" +
                "        return name;\n" +
                "    }\n" +
                "}";
        String fileName = "E:\\jitClass\\ActionController.java";
        File file = new File(fileName);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 编译java类
     * 使用Runtime执行javac命令
     * @param classPath 不带后缀  例如com.test.Notice  而不要写成com.test.Notice.java
     * @throws java.io.IOException
     */
    public static void javac(String classPath) throws IOException {


       // Process process = Runtime.getRuntime().exec("javac -cp"+ jarAbsolutePath+ " " + packageAndClass);
       Process process = Runtime.getRuntime().exec("javac " + classPath);
        try {
            InputStream errorStream = process.getErrorStream();
            InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line=bufferedReader.readLine()) != null){
                System.out.println(line);
            }
            //如果大于0，表明 编译成功，可以手工CMD编译
            int exitVal = process.waitFor();
            System.out.println("Process exitValue: " + exitVal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        outJava();
        try {
            javac("E:\\jitClass\\ActionController.java");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
