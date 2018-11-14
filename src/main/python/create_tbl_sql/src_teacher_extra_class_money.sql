create  table if not exists src.teacher_extra_class_money(
  id int comment '',
  teacher_id int comment '老师id user_teacher.id',
  time_day int comment '统计时间  凌晨时间戳',
  hour double comment '老师这天总课时长(单位：小时，额外时段上的课)',
  money double comment '老师这一天总时段费（额外时段上的课）',
  class_ids string comment '老师课程id，以逗号连接',
  detail string comment '序列化的每节课成详情 如array(array('class_id' => '111',//课程id 'start_time' =>12233,//超出时间开始'end_time' => 12233,//超出时间结束'fee' => 0.11//超出时长与基础时段费乘积))'
)
comment '老师每日额外课时长总和及总课时费用统计表'
;
