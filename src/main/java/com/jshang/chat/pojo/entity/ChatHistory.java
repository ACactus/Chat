package com.jshang.chat.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 聊天记录表
 * @TableName chat_history
 */
@TableName(value ="chat_history")
@Data
public class ChatHistory implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 流水号
     */
    private String seq;

    /**
     * 会话ID
     */
    private Long conversationId;

    /**
     * 角色： 1-机器人 2-用户
     */
    private Integer role;

    /**
     * 原始内容
     */
    private String rawContent;

    /**
     * 0-未评价 1-点赞 2-点踩
     */
    private Integer rate;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
