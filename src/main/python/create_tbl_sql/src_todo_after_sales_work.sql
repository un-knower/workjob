create  table if not exists src.todo_after_sales_work(
  id int comment '',
  student_id bigint comment '',
  tag_id int comment '待跟进任务',
  status int comment '标签类型:0表示激活，1表示关闭',
  next_visit_time int comment '回访冷却时间'
)
comment =''
;
