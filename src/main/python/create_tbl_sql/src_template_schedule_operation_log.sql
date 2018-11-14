create  table if not exists src.template_schedule_operation_log(
  id int comment '',
  student_id int comment '学生ID',
  action_type int comment '操作类型 1:添加模版 2:删除模版',
  reason_type int comment '更改模版原因：1 ',
  reason_text string comment '更改模版备注',
  add_template_info string comment '用户提交固定模版信息',
  old_template_list string comment '新模版提交前用户有效固定模版信息',
  new_template_list string comment '新模版提交后用户有效固定模版信息',
  operator_id bigint comment '操作人id,关联user_account.id',
  user_type int comment '10:内部用户 20:用户 ',
  platform int comment '1:crm 2:官网 3:学生app 4:学生公众号 5:学生小程序 6:脚本',
  create_time string comment '创建时间',
  update_time string comment '更新时间'
)
comment '固定课模版操作记录'
;
