create  table if not exists src.teacher_complain_punish(
  id int comment '',
  teacher_id int comment '��ʦid,user_teacher.id',
  student_id int comment 'ѧ��id,user.id',
  class_id int comment '������id,class_room.id',
  content string comment 'Ͷ������',
  status int comment '1,��ѵʦ������;2,����Ա�����;3,Ͷ����ȷ��;4,Ͷ����ȡ��',
  reason string comment 'Ͷ��ԭ��',
  teacher_customer int comment '��ʦ�ͷ�id,user_account.id',
  teacher_master int comment '�������ѵʦ��id,user_account.id',
  teacher_manager int comment '��˵Ĺ���Աid,user_account.id',
  money double comment 'Ͷ�ߴ�������money',
  time_created int comment 'Ͷ�ߴ���ʱ��',
  time_updated int comment 'Ͷ�߸���ʱ��',
  operate_status int comment '0,����ȷ��;1,��ѵʦȷ��;2,����Աȷ��',
  multiple_money int comment '�ۿ��ʱ�ѵı���',
  label string comment 'Ͷ�߱�ǩ��1���Ͽ�̬�ȣ�2����������',
  master_operate_time int comment '��ѵʦ����ʱ��',
  manager_operate_time int comment '����Ա����ʱ��',
  workorder_course_id int comment '����ϵͳ�γ̱�id',
  workorder_initiator_id int comment '����������',
  initiator_source int comment '��������Դ 10��wo��20��crm��30��tms��40��admin'
)
comment '��ʦ�ͷ��ּ��ϴ��ļҳ�Ͷ�߱�'
;
