create  table if not exists src.teacher_label(
  id int comment '标签id',
  parent_id int comment '父类id',
  name string comment '标签名字',
  type int comment '1，内部标签，2，外部标签',
  time_created int comment '创建时间',
  time_updated int comment '更新时间',
  is_deleted int comment '0,未删除，1，已删除',
  status int comment '状态 1：启用；2：禁用',
  select_type int comment '标签选项 1：单选； 2：多选',
  code string comment 'code码',
  version int comment '版本  1：老版； 2：新版'
)
comment '老师标签配置'
;
