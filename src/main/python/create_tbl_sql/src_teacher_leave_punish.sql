create  table if not exists src.teacher_leave_punish(
  id int comment '',
  leave_id int comment '��ټ�¼��id,statistics_teacher_rest.id',
  teacher_id int comment '��ʦid,user_teacher.id',
  time_day int comment '������ڵ���0��ʱ���',
  leave_type int comment '0��ɾ����1����ǰ��٣�2����ʱ��٣�3����ͣ�����٣���4��������5���������',
  fixtime_tag int comment 'ʱ������ٱ�ǩ��0��ʱ������٣�1��ʱ�������',
  time_start int comment '��ٿ�ʼʱ��',
  time_end int comment '��ٽ���ʱ��',
  money double comment '�ۿ���',
  remark string comment '��ע'
)
comment '��ٿۿ��'
;
