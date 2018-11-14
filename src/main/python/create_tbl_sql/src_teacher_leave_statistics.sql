create  table if not exists src.teacher_leave_statistics(
  id int comment '',
  teacher_id int comment '老师id，user_teacher.id',
  time_day int comment '请假日期零点时间戳',
  leave_type int comment '请假类型，1：提前请假，2：临时请假，3：暂停请假，4：旷工，5：紧急请假',
  time_start int comment '请假开始时间',
  time_end int comment '请假结束时间',
  time_created int comment '创建时间',
  time_updated int comment '更新时间',
  is_deleted int comment '是否删除，0：否，1：是',
  excuse string comment '请假理由',
  operator int comment '操作人，user_account.id'
)
comment '老师请假记录表（新表，支持一天多次请假）'
;
