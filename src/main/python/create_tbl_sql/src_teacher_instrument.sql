create  table if not exists src.teacher_instrument(
  teacher_id int comment '',
  instrument_id int comment '����ID',
  grade int comment '1������2������3���м�4���߼�',
  level int comment '1,2,3,4,5',
  hour_first double comment '25���ӿ�ʱ��΢����',
  hour_second double comment '45���ӿ�ʱ��΢����',
  hour_third double comment '50���ӿ�ʱ��΢����',
  salary double comment 'ʱ�η�΢����'
)
comment =''
;
