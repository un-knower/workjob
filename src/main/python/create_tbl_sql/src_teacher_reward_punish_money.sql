create  table if not exists src.teacher_reward_punish_money(
  id int comment '',
  teacher_id int comment 'user_teacher.id',
  late_reward double comment '�ٵ�����',
  compensate_reward double comment 'ˬԼ����',
  overtime_reward double comment '��ʱ����',
  product_reward double comment '����ɵ�����',
  tmp_leave_reward double comment '��ʱ��ٽ���',
  holiday_reward double comment '�ڼ��ռӰཱ��',
  praise_reward double comment '��������',
  fulltime_reward double comment 'ȫ�ڽ���',
  normal_leave_reward double comment '������ٽ���',
  late_punish double comment '�ٵ��ͷ�',
  compensate_punish double comment 'ˬԼ�ͷ�',
  overtime_punish double comment '��ʱ�ͷ�',
  product_punish double comment '����ɵ��ͷ�',
  tmp_leave_punish double comment '��ʱ��ٳͷ�',
  holiday_punish double comment '�ڼ��ճͷ�',
  praise_punish double comment '�����ͷ�',
  fulltime_punish double comment 'ȫ�ڳͷ�',
  normal_leave_punish double comment '������ٳͷ�',
  month_reward double comment '���ܽ���',
  month_punish double comment '���ܳͷ�',
  month_first int comment '����1��ʱ���',
  statistics_time int comment 'ͳ��ʱ��'
)
comment '�����ͷ��ű���ÿ�ո��±��½��ͻ���ݣ�'
;
