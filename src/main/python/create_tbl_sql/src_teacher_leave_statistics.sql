create  table if not exists src.teacher_leave_statistics(
  id int comment '',
  teacher_id int comment '��ʦid��user_teacher.id',
  time_day int comment '����������ʱ���',
  leave_type int comment '������ͣ�1����ǰ��٣�2����ʱ��٣�3����ͣ��٣�4��������5���������',
  time_start int comment '��ٿ�ʼʱ��',
  time_end int comment '��ٽ���ʱ��',
  time_created int comment '����ʱ��',
  time_updated int comment '����ʱ��',
  is_deleted int comment '�Ƿ�ɾ����0����1����',
  excuse string comment '�������',
  operator int comment '�����ˣ�user_account.id'
)
comment '��ʦ��ټ�¼���±�֧��һ������٣�'
;
