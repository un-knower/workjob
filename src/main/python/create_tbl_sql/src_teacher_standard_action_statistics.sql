create  table if not exists src.teacher_standard_action_statistics(
  id int comment '',
  teacher_id int comment '��ʦid',
  class_id int comment '�γ�id��class_room.id',
  time_class int comment '�ÿγ��Ͽο�ʼʱ�� class_room.time_class',
  standard string comment 'n/m ��ʶ��������ʽ',
  detail string comment '������� ���л��Ľ���������л�������Ϊarray(array('name'=>'��׼����Ŀ��','result'=>'���'))',
  status int comment '�Ƿ���  1���      2 ����� ',
  operate_status int comment '0:δ����1.��ѵʦȷ��2.��ѵʦȡ��3������Աȷ��4������Աȡ��',
  operator_master int comment '��ѵʦ������ID',
  operator_manager int comment '����Ա������ID',
  master_operate_time int comment '��ѵʦ����ʱ��',
  manager_operate_time int comment '����Ա����ʱ��',
  is_deleted int comment '�Ƿ�ɾ��  0 ��  1��',
  create_time int comment '����ʱ��',
  update_time int comment '����ʱ��',
  create_user int comment '��Ӽ���¼����  user_account.id',
  modify_user int comment '�༭����¼����  user_account.id',
  modify_time int comment '�༭����¼ʱ�� ',
  feedback string comment '������Ϣ',
  money double comment '�Ǳ�׼�������ۿ�'
)
comment '��ʦ��׼������ͳ�Ʊ�'
;
