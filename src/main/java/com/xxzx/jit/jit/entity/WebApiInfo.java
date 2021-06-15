package com.xxzx.jit.jit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * The type Web api info.
 *
 * @author XieXiongXiong
 * @date 2021 -06-15
 */
@TableName("web_api_info")
@Data
public class WebApiInfo {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "bean_name")
    private String beanName;

    @TableField(value = "api_path")
    private String apiPath;

    @TableField(value = "method_name")
    private String methodName;

    @TableField(value = "class_path")
    private String classPath;

    @TableField(value = "status")
    private Integer  status;

    @TableField(value = "utime")
    private String  utime;
}
