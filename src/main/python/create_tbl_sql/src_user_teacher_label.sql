create  table if not exists src.user_teacher_label(
  id int comment '����id',
  user_teacher_id int comment '��ʦid��user_teacher.id',
  teacher_label_id int comment '��ǩid��teacher_label.id',
  time_created int comment '����ʱ��'
)
comment '��ʦ��ǩ������'
;
