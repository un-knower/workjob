create  table if not exists src.teacher_train_arrangement(
  id int comment '',
  teacher_id int comment 'user_teacher.id',
  type int comment '预约类型   1：听音测试 2：基础培训 3：模拟考核 4:入职培训 5:正式考核',
  time_class int comment '预约开始时间',
  time_end int comment '预约结束时间',
  status int comment '状态  0：未开始   1：已完成  2：已取消',
  nums int comment '当前第几次预约',
  teacher_master int comment '培训师id user_account.id',
  is_deleted int comment '是否删除  0未删除  1已删除',
  time_created int comment '创建时间',
  operator int comment '创建人 user_account.id',
  time_updated int comment '更新时间',
  updated_user int comment '更新人 user_account.id',
  dimission_id int comment 'teacher_dimission.id',
  research_master int comment '教研员id user_account.id',
  update_time string comment '修改时间'
)
comment '老师培训、招聘期听音测试、基础培训等预约时间表'
;
