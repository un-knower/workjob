create  table if not exists src.teacher_personal_statistics(
  id int comment '',
  teacher_id int comment '老师ID，user_teacher.id',
  work_id int comment '工作状态，3：在职，4：离职',
  fix_student_num int comment '固定学生数',
  class_num int comment '服务课时总数',
  growth int comment '当前成长值（课时总数-迟到课数-投诉数）',
  praise_num int comment '好评总数',
  praise_rate double comment '好评率（2位小数）',
  praise_rate_avg double comment '平均好评率（2位小数）',
  ex_class_num int comment '体验课数',
  ex_order_num int comment '体验课成单数',
  conversion_rate double comment '体验课成单转化率（2位小数）',
  conversion_rate_avg double comment '平均转化率（2位小数）',
  total_class_hour double comment '服务课程总时长（利用率分子，25分钟课程按半小时计，45分钟50分钟课程按一小时计）',
  use_rate double comment '利用率（2位小数）',
  use_rate_avg double comment '平均利用率（2位小数）',
  sum_leave_hour double comment '时段内提前请假临时请假总时长',
  sum_work_hour double comment '工作总时长',
  leave_rate double comment '请假率（2位小数）',
  leave_rate_avg double comment '平均请假率（2位小数）',
  late_num int comment '迟到数',
  late_rate double comment '迟到率（2位小数）',
  late_rate_avg double comment '平均迟到率（2位小数）',
  complain_num int comment '投诉数',
  complain_rate double comment '投诉率（2位小数）',
  complain_rate_avg double comment '平均投诉率（2位小数）',
  absence_num int comment '旷工数',
  absence_num_avg int comment '平均旷工数',
  resume_praise int comment '简历好评数',
  on_work_day int comment '上班天数',
  statistics_time int comment '统计时间',
  teach_experience int comment '教学经验  新简历使用',
  praise int comment '好评度  新简历使用',
  years int comment '老师工作年限',
  before_ex_class_num int comment '新成单前的体验课数（实时）',
  record_before_ex_class_num int comment '同步脚本前的成单前的体验课数（记录）',
  record_ex_class_num int comment '同步脚本前的体验课数（实时）',
  record_ex_order_num int comment '同步脚本前的体验课成单数（实时）',
  record_conversion_rate double comment '同步脚本前的体验课成单转化率（2位小数）（实时）'
)
comment '老师个人统计数据'
;
