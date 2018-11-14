create  table if not exists src.register_utm(
  user_id int comment '',
  utm_source string comment '广告系列来源',
  utm_medium string comment '广告系列媒介',
  utm_term string comment '广告系列字词',
  utm_content string comment '广告系列内容',
  utm_campaign string comment '广告系列名称',
  utm_url string comment '访问域名',
  source_ip string comment '来源地址'
)
comment =''
;
