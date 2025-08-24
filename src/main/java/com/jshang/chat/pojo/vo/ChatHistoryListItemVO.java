package com.jshang.chat.pojo.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.jshang.chat.pojo.entity.ChatHistory;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Classname ChatHistoryListItemVO
 * @Description 聊天历史vo
 * @Date 2025/8/24 11:16
 * @Author Shawn
 */
@Data
public class ChatHistoryListItemVO implements Serializable {
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

    public static ChatHistoryListItemVO ofEntity(ChatHistory entity) {
        ChatHistoryListItemVO vo = new ChatHistoryListItemVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
