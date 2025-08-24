DROP TABLE IF EXISTS chat_history;
CREATE TABLE chat_history
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    seq             CHAR(32) NOT NULL COMMENT '流水号',
    conversation_id BIGINT   NOT NULL COMMENT '会话ID',
    role            TINYINT  NOT NULL COMMENT '角色： 1-机器人 2-用户',
    raw_content     TEXT     NOT NULL COMMENT '原始内容',
    rate            TINYINT  NOT NULL COMMENT '0-未评价 1-点赞 2-点踩',

    create_time     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '聊天记录表';
ALTER TABLE chat_history
    ADD INDEX idx_conversation_id_role (conversation_id, role);
ALTER TABLE chat_history
    ADD UNIQUE INDEX uk_seq (seq);

DROP TABLE IF EXISTS chat_conversation;
CREATE TABLE chat_conversation
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    seq         CHAR(32) NOT NULL COMMENT '流水号',
    user_id     BIGINT       NOT NULL COMMENT '用户ID',
    title       VARCHAR(255) NOT NULL COMMENT '标题',

    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '聊天会话表';
ALTER TABLE chat_conversation
    ADD INDEX idx_user_id (user_id);
ALTER TABLE chat_conversation
    ADD UNIQUE INDEX uk_seq (seq);

DROP TABLE IF EXISTS user;
CREATE TABLE user
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    user_name   VARCHAR(64) NOT NULL COMMENT '用户名',

    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '用户表';
