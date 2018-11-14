create  table if not exists src.teacher_reward_punish_money(
  id int comment '',
  teacher_id int comment 'user_teacher.id',
  late_reward double comment '迟到奖励',
  compensate_reward double comment '爽约奖励',
  overtime_reward double comment '超时奖励',
  product_reward double comment '体验成单奖励',
  tmp_leave_reward double comment '临时请假奖励',
  holiday_reward double comment '节假日加班奖励',
  praise_reward double comment '好评奖励',
  fulltime_reward double comment '全勤奖励',
  normal_leave_reward double comment '正常请假奖励',
  late_punish double comment '迟到惩罚',
  compensate_punish double comment '爽约惩罚',
  overtime_punish double comment '超时惩罚',
  product_punish double comment '体验成单惩罚',
  tmp_leave_punish double comment '临时请假惩罚',
  holiday_punish double comment '节假日惩罚',
  praise_punish double comment '好评惩罚',
  fulltime_punish double comment '全勤惩罚',
  normal_leave_punish double comment '正常请假惩罚',
  month_reward double comment '月总奖励',
  month_punish double comment '月总惩罚',
  month_first int comment '本月1日时间戳',
  statistics_time int comment '统计时间'
)
comment '奖励惩罚脚本表（每日更新本月奖惩活动数据）'
;
