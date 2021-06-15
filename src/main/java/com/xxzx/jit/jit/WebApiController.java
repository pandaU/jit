package com.xxzx.jit.jit;

import com.xxzx.jit.jit.config.PathConfig;
import com.xxzx.jit.jit.jitDemo.WebApiClassLoader;
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

/**
 * <p>
 * The type Test controller.
 *
 * @author XieXiongXiong
 * @date 2021 -06-15
 */
@RestController
public class WebApiController {


    /**
     * Register api string.
     *
     * @param file       the file
     * @param methodName the method name
     * @param apiMapping the api mapping
     * @return the string
     * @throws Exception the exception
     * @author XieXiongXiong
     * @date 2021 -06-15 08:42:25
     */
    @PostMapping("/registerApi")
    public String registerApi(@RequestParam("file") MultipartFile file,String methodName,String apiMapping) throws Exception {
        String fileName = file.getOriginalFilename();
        if (file.isEmpty() || !fileName.endsWith(PathConfig.JAVA_SUFFIX)) {
            return "请选择java文件";
        }
        if (!StringUtils.hasLength(fileName)){
            return "文件名称不合法";
        }
        String filePath = PathConfig.EXT_JAVA_DIR;
        String apiPath = filePath + fileName;
        File dest = new File(apiPath);
        try {
            file.transferTo(dest);
            WebApiClassLoader loader = new WebApiClassLoader(Thread.currentThread().getContextClassLoader());
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

    /**
     * Register bean 2 string.
     *
     * @param beanName   the bean name
     * @param methodName the method name
     * @param argsType   the args type
     * @return the string
     * @throws NoSuchMethodException     the no such method exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws InstantiationException    the instantiation exception
     * @throws ClassNotFoundException    the class not found exception
     * @author XieXiongXiong
     * @date 2021 -06-15 08:42:25
     */
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

    /**
     * Compiler boolean.
     *
     * @param javaAbsolutePath the java absolute path
     * @return the boolean
     * @author XieXiongXiong
     * @date 2021 -06-15 08:42:25
     */
    private static Boolean compiler(String javaAbsolutePath){
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int run = compiler.run(null, null, null, "-encoding", "UTF-8", "-extdirs", PathConfig.EXT_JAVA_LIB, javaAbsolutePath);
        return run == 0;
    }
}