create  table if not exists src.teacher_type_change(
  id bigint comment '����ID',
  teacher_id bigint comment '��ʦID: user_teacher.id',
  type int comment '��ʦ�ı����� 1���˺����ͣ�2����������',
  dest_type int comment '״̬: �˺����� 1-������ʦ, 2-����Ա, 3-�����˺ţ��������� 1-ȫְ��ʦ, 2-��ְ��ʦ',
  is_deleted int comment '�Ƿ�ɾ�� 0��������1��ɾ��',
  is_changed int comment '0-�����, 1-�ѱ��',
  change_time int comment '���ʱ��',
  create_time int comment '����ʱ��',
  update_time int comment '����ʱ��'
)
comment '��ʦ���ͱ����Ϣ'
;
