package com.xxzx.jit.jit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.xxzx.jit.jit.mapper*", "com.baomidou.springboot.mapper*"})
public class JitApplication {

    public static void main(String[] args) {

       SpringApplication.run(JitApplication.class, args);
    }

}
