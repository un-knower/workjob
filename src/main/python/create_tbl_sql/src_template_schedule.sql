create  table if not exists src.template_schedule(
  id bigint comment '',
  start_time int comment 'ʱ��ʱ�䣬e.g:ʱ���',
  end_time int comment '����ʱ�䣬e.g:ʱ���,����ʱ������:4294967295',
  student_id bigint comment '�û�id',
  status int comment '1:���� 2:���� 3��ʧЧ',
  create_time string comment '����ʱ��',
  update_time string comment '����ʱ��',
  operator_id bigint comment '������id,����user_account.id',
  user_type int comment '10:�ڲ��û� 20:�û� ',
  platform int comment '1:crm 2:���� 3:ѧ��app 4:ѧ�����ں� 5:ѧ��С���� 6:�ű�'
)
comment 'ģ��ʱ���'
;
