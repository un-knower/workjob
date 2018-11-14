create  table if not exists src.todo_work_tag(
  id int comment '',
  work_tag int comment '待跟进任务类型',
  work_name string comment '待跟进任务名称',
  is_delete int comment '是否删除'
)
comment =''
;
