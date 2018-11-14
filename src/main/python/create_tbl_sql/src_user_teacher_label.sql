create  table if not exists src.user_teacher_label(
  id int comment '关联id',
  user_teacher_id int comment '老师id，user_teacher.id',
  teacher_label_id int comment '标签id，teacher_label.id',
  time_created int comment '关联时间'
)
comment '老师标签关联表'
;
