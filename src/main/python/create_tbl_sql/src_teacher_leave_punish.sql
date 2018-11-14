create  table if not exists src.teacher_leave_punish(
  id int comment '',
  leave_id int comment '请假记录的id,statistics_teacher_rest.id',
  teacher_id int comment '老师id,user_teacher.id',
  time_day int comment '请假日期当日0点时间戳',
  leave_type int comment '0：删除，1：提前请假，2：临时请假，3：暂停（病假），4：旷工，5：紧急请假',
  fixtime_tag int comment '时段内请假标签，0：时段外请假，1：时段内请假',
  time_start int comment '请假开始时间',
  time_end int comment '请假结束时间',
  money double comment '扣款金额',
  remark string comment '备注'
)
comment '请假扣款表'
;
