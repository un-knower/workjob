create  table if not exists src.teacher_train_arrangement(
  id int comment '',
  teacher_id int comment 'user_teacher.id',
  type int comment 'ԤԼ����   1���������� 2��������ѵ 3��ģ�⿼�� 4:��ְ��ѵ 5:��ʽ����',
  time_class int comment 'ԤԼ��ʼʱ��',
  time_end int comment 'ԤԼ����ʱ��',
  status int comment '״̬  0��δ��ʼ   1�������  2����ȡ��',
  nums int comment '��ǰ�ڼ���ԤԼ',
  teacher_master int comment '��ѵʦid user_account.id',
  is_deleted int comment '�Ƿ�ɾ��  0δɾ��  1��ɾ��',
  time_created int comment '����ʱ��',
  operator int comment '������ user_account.id',
  time_updated int comment '����ʱ��',
  updated_user int comment '������ user_account.id',
  dimission_id int comment 'teacher_dimission.id',
  research_master int comment '����Աid user_account.id',
  update_time string comment '�޸�ʱ��'
)
comment '��ʦ��ѵ����Ƹ���������ԡ�������ѵ��ԤԼʱ���'
;
