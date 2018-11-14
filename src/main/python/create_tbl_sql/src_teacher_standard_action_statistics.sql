create  table if not exists src.teacher_standard_action_statistics(
  id int comment '',
  teacher_id int comment '老师id',
  class_id int comment '课程id，class_room.id',
  time_class int comment '该课程上课开始时间 class_room.time_class',
  standard string comment 'n/m 标识达标分数形式',
  detail string comment '结果详情 序列化的结果，反序列化的数组为array(array('name'=>'标准化项目名','result'=>'达标'))',
  status int comment '是否达标  1达标      2 不达标 ',
  operate_status int comment '0:未处理1.培训师确认2.培训师取消3：管理员确认4：管理员取消',
  operator_master int comment '培训师操作人ID',
  operator_manager int comment '管理员操作人ID',
  master_operate_time int comment '培训师处理时间',
  manager_operate_time int comment '管理员处理时间',
  is_deleted int comment '是否删除  0 否  1是',
  create_time int comment '生成时间',
  update_time int comment '更新时间',
  create_user int comment '添加监察记录的人  user_account.id',
  modify_user int comment '编辑监察记录的人  user_account.id',
  modify_time int comment '编辑监察记录时间 ',
  feedback string comment '反馈信息',
  money double comment '非标准化动作扣款'
)
comment '老师标准化动作统计表'
;
