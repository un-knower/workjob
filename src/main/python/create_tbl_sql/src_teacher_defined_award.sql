create  table if not exists src.teacher_defined_award(
  id int comment '����id',
  name string comment '�Զ��影�͵����ּ���',
  teacher_id int comment '��ʦid',
  class_id int comment '�����Ŀε�id��class_room.id',
  awardtype int comment '�������ͣ�0������ͷ���1����������',
  money double comment '���',
  mark string comment '�����ͷ���ע',
  is_publish int comment '�Ƿ񷢲���,0,δ������1���ѷ���',
  template_id int comment 'ģ��ID:template_manager.id',
  time_created int comment '����ʱ��',
  time_updated int comment '',
  statistics_time int comment ''
)
comment '��ʦ�Զ��影���ͷ���'
;
