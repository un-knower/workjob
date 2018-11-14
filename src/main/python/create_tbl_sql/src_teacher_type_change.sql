create  table if not exists src.teacher_type_change(
  id bigint comment '主键ID',
  teacher_id bigint comment '老师ID: user_teacher.id',
  type int comment '老师改变类型 1：账号类型；2：工作类型',
  dest_type int comment '状态: 账号类型 1-陪练老师, 2-管理员, 3-测试账号；工作类型 1-全职老师, 2-兼职老师',
  is_deleted int comment '是否删除 0：正常；1：删除',
  is_changed int comment '0-待变更, 1-已变更',
  change_time int comment '变更时间',
  create_time int comment '创建时间',
  update_time int comment '更新时间'
)
comment '老师类型变更信息'
;
