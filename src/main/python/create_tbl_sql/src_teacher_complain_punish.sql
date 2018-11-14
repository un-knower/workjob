create  table if not exists src.teacher_complain_punish(
  id int comment '',
  teacher_id int comment '老师id,user_teacher.id',
  student_id int comment '学生id,user.id',
  class_id int comment '关联课id,class_room.id',
  content string comment '投诉内容',
  status int comment '1,培训师待处理;2,管理员待审核;3,投诉已确认;4,投诉已取消',
  reason string comment '投诉原因',
  teacher_customer int comment '老师客服id,user_account.id',
  teacher_master int comment '处理的培训师的id,user_account.id',
  teacher_manager int comment '审核的管理员id,user_account.id',
  money double comment '投诉处理处罚的money',
  time_created int comment '投诉创建时间',
  time_updated int comment '投诉更新时间',
  operate_status int comment '0,暂无确认;1,培训师确认;2,管理员确认',
  multiple_money int comment '扣款课时费的倍数',
  label string comment '投诉标签，1，上课态度，2，能力问题',
  master_operate_time int comment '培训师处理时间',
  manager_operate_time int comment '管理员处理时间',
  workorder_course_id int comment '工单系统课程表id',
  workorder_initiator_id int comment '工单发起人',
  initiator_source int comment '发起人来源 10：wo、20：crm、30：tms、40：admin'
)
comment '老师客服分拣上传的家长投诉表'
;
