package com.xxzx.jit.jit;

import com.xxzx.jit.jit.config.PathConfig;
import com.xxzx.jit.jit.jitDemo.MyClassLoader;
import com.xxzx.jit.jit.utils.ApplicationContextRegister;
import com.xxzx.jit.jit.utils.RegisterBean;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RestController
public class TestController {


    @PostMapping("/registerApi")
    public String registerApi(@RequestParam("file") MultipartFile file,String methodName,String apiMapping) throws Exception {
        if (file.isEmpty()) {
            return "请选择java文件";
        }
        String fileName = file.getOriginalFilename();
        if (!StringUtils.hasLength(fileName)){
            return "文件名称不合法";
        }
        String filePath = PathConfig.EXT_JAVA_DIR;
        String apiPath = filePath + fileName;
        File dest = new File(apiPath);
        try {
            file.transferTo(dest);
            MyClassLoader loader = new MyClassLoader(Thread.currentThread().getContextClassLoader());
            /**动态编译*/
            Boolean compilerResp =  compiler(apiPath);
            if (!compilerResp){
                return "代码编译失败，请检查代码书写格式";
            }
            String[] strings = fileName.split("\\.");
            String apiName = strings[0];
            String javaPath = apiName + ".class";
            Class<?> aClass = loader.loadClass(filePath + javaPath);
            final char[] chars = apiName.toCharArray();
            chars[0] = chars[0] < 91 ? (char) (chars[0]+32)  : chars[0];
            String apiNameDown = new String(chars);
            Object bean = RegisterBean.registerBean(apiNameDown, aClass);
            Class<?> aClass1 = bean.getClass();
            final RestController annotation = aClass1.getAnnotation(RestController.class);
            if (annotation == null){
                return "发布失败,请确保类上有@RestController";
            }
            RegisterBean.controlCenter(aClass1, ApplicationContextRegister.getApplicationContext(),2,methodName,apiMapping);
            //// TODO: 2021/6/11  将发布信息存储到mysql 便于后期维护管理
            return "发布成功";
        } catch (IOException e) {
        }
        return "发布失败,请确保方法上有@RequestMapping";
    }
    @RequestMapping("/testBean")
    public String registerBean2(String beanName,String methodName,String  argsType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Class<?> args = Class.forName(argsType);
        Object instance = args.newInstance();
        Object bean = ApplicationContextRegister.getBean(beanName);
        Class<?> aClass = bean.getClass();
        Method toAction = aClass.getDeclaredMethod(methodName, args);
        Object resp = toAction.invoke(bean, instance);
        return resp.toString();

    }

    private static Boolean compiler(String javaAbsolutePath){
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int run = compiler.run(null, null, null, "-encoding", "UTF-8", "-extdirs", PathConfig.EXT_JAVA_LIB, javaAbsolutePath);
        return run == 0;
    }
}