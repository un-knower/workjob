create  table if not exists src.student_new_bind_status(
  student_id int comment 'ѧ��id',
  is_deleted int comment '�Ƿ�ɾ�� 0-δɾ��',
  channel_type int comment '�������� 0-������ 10-�������� 20-ת�������� 30-����� 40-��������',
  channel_name string comment '��������',
  kefu_id int comment '��ǩ�ͷ�id',
  intention int comment '1:�����ͻ� 2:��̿ͻ� 3:�ص���� 4:��Ч�ͻ� 5:������',
  user_type int comment '�û����� 0��crm�û���1�������˺ţ�����ͬѧ����Ʒͬѧ���� 2����ѵʦ��  3: �����˺ţ� 4:�����˺�',
  last_visit_time int comment '���ط�ʱ��',
  last_unbind_type int comment '���һ�ν����ϵ���ȡ���ͣ��󶨺�����, 0-Ĭ��ֵ��10-���û���20-�����û�24Сʱδ��ϵ��30-δ�Ϲ�����Σ�40-����δ���ѣ�50-����δ����, 60-90��δ�������, 70-�˷ѽ��, 80-�������������, 90-�����û��ֶ��������������ָ��ѣ�',
  last_kefu_id int comment '��һ�ΰ󶨿ͷ�id',
  related_class_id int comment '����ʱ�����Ŀγ�id��Ϣ',
  buy_status int comment '�û�����״̬��0-δ���ѣ�10-�Ѹ���',
  last_called_time int comment '����绰ʱ��',
  order int comment '���������ֶΣ�ֵԽ��Խ��ǰ',
  register_time int comment 'ע��ʱ��',
  country_code string comment '��������',
  tag_type int comment '������� 0-Ĭ��ֵ��20-���������û���5-Ǳ���û�ת90��',
  update_time string comment '����ʱ��'
)
comment '�û�����ȡ״̬��Ϣ����Ӧ����ȡ�б��ѯ'
;
