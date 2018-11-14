create  table if not exists src.teacher_instrument(
  teacher_id int comment '',
  instrument_id int comment '乐器ID',
  grade int comment '1：启蒙2：初级3：中级4：高级',
  level int comment '1,2,3,4,5',
  hour_first double comment '25分钟课时费微调数',
  hour_second double comment '45分钟课时费微调数',
  hour_third double comment '50分钟课时费微调数',
  salary double comment '时段费微调数'
)
comment =''
;
