package com.xxzx.jit.jit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxzx.jit.jit.entity.WebApiInfo;
import com.xxzx.jit.jit.mapper.WebApiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * The type Web api service.
 *
 * @author XieXiongXiong
 * @date 2021 -06-15
 */
@Service("webApiService")
public class WebApiServiceImpl {
    private final WebApiMapper webApiMapper;
    @Autowired(required = false)
    public WebApiServiceImpl(WebApiMapper webApiMapper) {
        this.webApiMapper = webApiMapper;
    }

    public int saveWebApi(WebApiInfo webApiInfo){
        return webApiMapper.insert(webApiInfo);
    }

    public WebApiInfo selectById(Long id){
        return webApiMapper.selectById(id);
    }

    public int upById(WebApiInfo webApiInfo){
        return webApiMapper.updateById(webApiInfo);
    }

    public List<WebApiInfo> list(String beanName,String methodName,String apiMapping){
        QueryWrapper<WebApiInfo> qw =  new QueryWrapper<>();
        if (beanName != null && !beanName.isEmpty()){
            qw.eq("bean_name",beanName);
        }
        if (methodName != null && !methodName.isEmpty()){
            qw.eq("method_name",methodName);
        }
        if (apiMapping != null && !apiMapping.isEmpty()){
            qw.eq("api_path",apiMapping);
        }
        qw.select("id,bean_name,method_name,api_path,status,class_path,date_format(utime,'%Y-%m-%d %H:%i:%s') as utime");
        return webApiMapper.selectList(qw);
    }
}
