create  table if not exists src.todo_after_sales(
  student_id int comment 'ѧ��id',
  kefu_id_re int comment '�����ͷ�id',
  next_visit_time int comment '�´λط�ʱ��',
  next_visit_day int comment '�´λط�����',
  next_visit_hour int comment '�´λط��յ�Сʱ',
  work_tag_order int comment '�������������ȼ�',
  close_time int comment '���ʱ��',
  change_time int comment '����ʱ��',
  is_first int comment '�Ƿ��ǵ�һ�ο�����ǩ��0Ϊ��1Ϊ��'
)
comment =''
;
