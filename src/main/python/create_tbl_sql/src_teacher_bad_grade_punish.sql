create  table if not exists src.teacher_bad_grade_punish(
  id int comment '',
  class_id int comment '课程id，class_room.id',
  teacher_id int comment '老师id,user_teacher.id',
  reason string comment '原因(2017-11-16已废)',
  status int comment '0:未处理1.培训师确认差评2.培训师取消差评3：管理员确认差评4：管理员取消差评',
  time_created int comment '处罚时间',
  operator_master int comment '培训师操作人ID',
  operator_manager int comment '管理员操作人ID',
  money double comment '处罚金额',
  master_operate_time int comment '培训师处理时间',
  manager_operate_time int comment '管理员处理时间'
)
comment '差评处理惩罚表'
;
