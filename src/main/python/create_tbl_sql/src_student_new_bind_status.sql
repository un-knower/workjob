create  table if not exists src.student_new_bind_status(
  student_id int comment '学生id',
  is_deleted int comment '是否删除 0-未删除',
  channel_type int comment '渠道类型 0-无渠道 10-主课渠道 20-转介绍渠道 30-活动渠道 40-其他渠道',
  channel_name string comment '渠道名称',
  kefu_id int comment '新签客服id',
  intention int comment '1:已死客户 2:冲刺客户 3:重点跟进 4:无效客户 5:考虑中',
  user_type int comment '用户类型 0：crm用户，1：测试账号（测试同学，产品同学）， 2：培训师，  3: 考核账号， 4:监听账号',
  last_visit_time int comment '最后回访时间',
  last_unbind_type int comment '最后一次解绑符合的领取类型，绑定后重置, 0-默认值，10-新用户，20-非新用户24小时未联系，30-未上过体验课，40-体验未付费，50-体验未跟进, 60-90天未上体验课, 70-退费解绑, 80-标记无需跟进解绑, 90-付费用户手动操作（包括部分付费）',
  last_kefu_id int comment '上一次绑定客服id',
  related_class_id int comment '掉落时关联的课程id信息',
  buy_status int comment '用户付费状态，0-未付费，10-已付费',
  last_called_time int comment '最后打电话时间',
  order int comment '排序优先字段，值越大越在前',
  register_time int comment '注册时间',
  country_code string comment '国家区号',
  tag_type int comment '标记类型 0-默认值，20-奖励名单用户，5-潜在用户转90天',
  update_time string comment '更新时间'
)
comment '用户可领取状态信息表，对应可领取列表查询'
;
