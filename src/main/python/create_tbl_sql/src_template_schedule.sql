create  table if not exists src.template_schedule(
  id bigint comment '',
  start_time int comment '时间时间，e.g:时间戳',
  end_time int comment '结束时间，e.g:时间戳,无限时间填入:4294967295',
  student_id bigint comment '用户id',
  status int comment '1:可用 2:废弃 3：失效',
  create_time string comment '创建时间',
  update_time string comment '更新时间',
  operator_id bigint comment '操作人id,关联user_account.id',
  user_type int comment '10:内部用户 20:用户 ',
  platform int comment '1:crm 2:官网 3:学生app 4:学生公众号 5:学生小程序 6:脚本'
)
comment '模版时间段'
;
