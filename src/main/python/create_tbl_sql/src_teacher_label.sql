create  table if not exists src.teacher_label(
  id int comment '��ǩid',
  parent_id int comment '����id',
  name string comment '��ǩ����',
  type int comment '1���ڲ���ǩ��2���ⲿ��ǩ',
  time_created int comment '����ʱ��',
  time_updated int comment '����ʱ��',
  is_deleted int comment '0,δɾ����1����ɾ��',
  status int comment '״̬ 1�����ã�2������',
  select_type int comment '��ǩѡ�� 1����ѡ�� 2����ѡ',
  code string comment 'code��',
  version int comment '�汾  1���ϰ棻 2���°�'
)
comment '��ʦ��ǩ����'
;
