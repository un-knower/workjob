create  table if not exists src.teacher_late_punish(
  id int comment '',
  class_id int comment '�γ�id,class_room.id',
  teacher_id int comment '��ʦid,user_teacher.id',
  reason string comment 'ԭ��(2017-11-16�ѷ�)',
  status int comment '0������ѵʦ����1����ѵʦȡ���ٵ���������Ա��ˣ���2������Ա��׼ȡ���ٵ���3����ѵʦȷ�ϳٵ���4������Ա����ȡ���ٵ�������Աȷ�ϳٵ���',
  time_class int comment '�Ͽ�ʱ��',
  time_end int comment '�γ̽���ʱ��',
  teacher_in_time int comment '��ʦ�������ʱ��',
  is_ex_class int comment '0������Σ�1�������',
  time_created int comment '����ʱ�䣨����״̬ȷ��ʱ�䣩',
  operator_master int comment '��ѵʦ������id��user_account.id',
  operator_manager int comment '����Ա������id,user_account.id',
  money double comment '���',
  master_operate_time int comment '��ѵʦ����ʱ��',
  manager_operate_time int comment '����Ա����ʱ��'
)
comment '��ʦ�ٵ��ͷ��γ̱�'
;
