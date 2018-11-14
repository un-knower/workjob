create  table if not exists src.refund(
  id int comment '',
  user_id int comment '',
  order_id int comment '',
  amount int comment '',
  price double comment '',
  time_created int comment '',
  left_id int comment 'class_left.id',
  reason string comment '退费理由',
  class_type string comment '课程类型/时长/乐器',
  user_name string comment '退款人姓名',
  user_card_no string comment '退款人银行卡号',
  user_bank_name string comment '退款开户银行',
  mobile string comment '手机号',
  refund_status int comment '0待退费，1退费中，2退费成功',
  refund_channel string comment '退款渠道:银行渠道、paypal、支付宝、其他',
  refund_type string comment '退款原因:放弃学琴 老师问题 主课反对 升级套餐等',
  currency string comment '币种',
  province string comment '开户省',
  city string comment '开户市'
)
comment =''
;
