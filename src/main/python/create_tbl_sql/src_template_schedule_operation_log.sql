create  table if not exists src.template_schedule_operation_log(
  id int comment '',
  student_id int comment 'ѧ��ID',
  action_type int comment '�������� 1:���ģ�� 2:ɾ��ģ��',
  reason_type int comment '����ģ��ԭ��1 ',
  reason_text string comment '����ģ�汸ע',
  add_template_info string comment '�û��ύ�̶�ģ����Ϣ',
  old_template_list string comment '��ģ���ύǰ�û���Ч�̶�ģ����Ϣ',
  new_template_list string comment '��ģ���ύ���û���Ч�̶�ģ����Ϣ',
  operator_id bigint comment '������id,����user_account.id',
  user_type int comment '10:�ڲ��û� 20:�û� ',
  platform int comment '1:crm 2:���� 3:ѧ��app 4:ѧ�����ں� 5:ѧ��С���� 6:�ű�',
  create_time string comment '����ʱ��',
  update_time string comment '����ʱ��'
)
comment '�̶���ģ�������¼'
;
