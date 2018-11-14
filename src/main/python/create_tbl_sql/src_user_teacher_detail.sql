create  table if not exists src.user_teacher_detail(
  id int comment '主键id',
  teacher_id int comment '老师id',
  recruit_instrument_info string comment '初筛擅长乐器json',
  time_created int comment '创建时间',
  time_updated int comment '更新时间',
  instrument_grade string comment '乐器id-乐器等级  用逗号连接   例如：  1-1,2-1,4-3',
  commission_school_id int comment '老师注册时扫码的 或后台第一次添加时选择的 school.id',
  commission_school_belong int comment '老师注册时所属渠道的渠道归属',
  commission_school_type int comment '老师注册时所属渠道的渠道类型',
  commission_school_status int comment '老师注册时所属渠道是否禁用  0 没有 1 已禁用(后台添加老师能选禁用的渠道)',
  s_teacher_master int comment '渠道培训师,老师注册时所属渠道对应的老师在培训期间的最后一位培训师id',
  update_time string comment '修改时间'
)
comment '老师信息附属表'
;
