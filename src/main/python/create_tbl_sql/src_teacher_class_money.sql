create  table if not exists src.teacher_class_money(
  id int comment '',
  teacher_id int comment '',
  class_id int comment '',
  class_money double comment '课时费',
  time_class int comment '上课时间',
  is_publish int comment '',
  time_end int comment '课程结束时间'
)
comment =''
;
