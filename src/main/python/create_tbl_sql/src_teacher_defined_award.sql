create  table if not exists src.teacher_defined_award(
  id int comment '自增id',
  name string comment '自定义奖惩的名字简述',
  teacher_id int comment '老师id',
  class_id int comment '关联的课的id，class_room.id',
  awardtype int comment '奖惩类型（0：代表惩罚，1：代表奖励）',
  money double comment '金额',
  mark string comment '奖励惩罚备注',
  is_publish int comment '是否发布过,0,未发布，1，已发布',
  template_id int comment '模板ID:template_manager.id',
  time_created int comment '创建时间',
  time_updated int comment '',
  statistics_time int comment ''
)
comment '老师自定义奖励惩罚表'
;
