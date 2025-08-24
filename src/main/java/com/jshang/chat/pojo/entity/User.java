package com.jshang.chat.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 创建时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private LocalDateTime updateTime;
}
