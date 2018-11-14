create  table if not exists src.todo_after_sales(
  student_id int comment '学生id',
  kefu_id_re int comment '复购客服id',
  next_visit_time int comment '下次回访时间',
  next_visit_day int comment '下次回访日期',
  next_visit_hour int comment '下次回访日的小时',
  work_tag_order int comment '待跟进任务优先级',
  close_time int comment '完成时间',
  change_time int comment '改期时间',
  is_first int comment '是否是第一次开启标签；0为否，1为是'
)
comment =''
;
