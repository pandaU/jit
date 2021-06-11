package com.xxzx.jit.jit;

import com.xxzx.jit.jit.utils.ApplicationContextRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class JitApplication {

    public static void main(String[] args) {

       SpringApplication.run(JitApplication.class, args);
    }

}
