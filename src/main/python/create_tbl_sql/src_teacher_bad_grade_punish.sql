create  table if not exists src.teacher_bad_grade_punish(
  id int comment '',
  class_id int comment '�γ�id��class_room.id',
  teacher_id int comment '��ʦid,user_teacher.id',
  reason string comment 'ԭ��(2017-11-16�ѷ�)',
  status int comment '0:δ����1.��ѵʦȷ�ϲ���2.��ѵʦȡ������3������Աȷ�ϲ���4������Աȡ������',
  time_created int comment '����ʱ��',
  operator_master int comment '��ѵʦ������ID',
  operator_manager int comment '����Ա������ID',
  money double comment '�������',
  master_operate_time int comment '��ѵʦ����ʱ��',
  manager_operate_time int comment '����Ա����ʱ��'
)
comment '��������ͷ���'
;
