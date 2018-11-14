create  table if not exists src.student_wechat_class(
  id int comment '',
  title string comment '标题',
  icon string comment '缩略图',
  banner_img string comment '课程banner图',
  url string comment '链接地址',
  class_time int comment '上课时间',
  end_time int comment '课程结束时间',
  content string comment '课程介绍',
  teacher_name string comment '讲课老师',
  teacher_description string comment '老师介绍',
  is_disable int comment '是否不可用',
  is_back int comment '是否可回顾',
  back_description string comment '回顾介绍',
  is_top int comment '是否顶置',
  creater int comment '创建人',
  create_time int comment '创建时间',
  modifer int comment '修改或删除人',
  update_time int comment '修改时间',
  is_delete int comment '删除标志',
  send_msg_status int comment '设置回顾时发送消息给所有直播预约客户的状态  0未发送  1已发送',
  poster_path string comment '海报地址',
  is_free int comment '是否免费  0 否 1是',
  is_send int comment ''
)
comment =''
;
