create  table if not exists src.teacher_late_punish(
  id int comment '',
  class_id int comment '课程id,class_room.id',
  teacher_id int comment '老师id,user_teacher.id',
  reason string comment '原因(2017-11-16已废)',
  status int comment '0：待培训师处理，1：培训师取消迟到（待管理员审核），2：管理员批准取消迟到，3：培训师确认迟到，4：管理员驳回取消迟到（管理员确认迟到）',
  time_class int comment '上课时间',
  time_end int comment '课程结束时间',
  teacher_in_time int comment '老师进入教室时间',
  is_ex_class int comment '0：购买课，1：体验课',
  time_created int comment '处理时间（最终状态确认时间）',
  operator_master int comment '培训师操作人id，user_account.id',
  operator_manager int comment '管理员操作人id,user_account.id',
  money double comment '金额',
  master_operate_time int comment '培训师处理时间',
  manager_operate_time int comment '管理员处理时间'
)
comment '老师迟到惩罚课程表'
;
