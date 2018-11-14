create  table if not exists src.subscribe_log(
  id int comment '',
  unique_id string comment '',
  open_id string comment 'open_id',
  is_expired int comment '是否为失效扫描行为',
  event int comment '事件类型： 关注事件=10   scan = 20',
  create_time int comment '创建时间',
  update_time string comment '修改时间'
)
comment =''
;
