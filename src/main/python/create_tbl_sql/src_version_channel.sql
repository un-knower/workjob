create  table if not exists src.version_channel(
  Id int comment '',
  uid int comment '用户编号，根据role角色来定义，role为0 user.id role为1user_teacher.id',
  role int comment '角色，0：学生（默认是学生）1：老师',
  clientId string comment '设备的唯一标识',
  version string comment '版本号',
  channelId int comment '渠道来源编号， 1:app;2：微信--crm发送url；3腾讯应用宝；4：百度手机助手（包含91，安卓市场）；5:360手机助手；6：小米应用商店；7：华为应用商店；8：oppo应用商店；9:vivo应用商店；10：阿里+豌豆荚+九游+UC；11：pc官网',
  createTime int comment '创建时间'
)
comment '用户版本的渠道来源'
;
