create  table if not exists src.todo_after_sales_work(
  id int comment '',
  student_id bigint comment '',
  tag_id int comment '����������',
  status int comment '��ǩ����:0��ʾ���1��ʾ�ر�',
  next_visit_time int comment '�ط���ȴʱ��'
)
comment =''
;
