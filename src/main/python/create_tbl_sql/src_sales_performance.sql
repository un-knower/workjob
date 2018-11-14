create  table if not exists src.sales_performance(
  id int comment '',
  kefu_id int comment 'user_account.id 新签客服ID',
  kefu_id_re int comment 'user_account.id 复购客服ID',
  user_id int comment '用户id',
  order_id int comment 'product_order.id 订单ID',
  payment_no string comment 'payment_list.payment_no 流水单号',
  actual_fee double comment '本次流水支付金额',
  time_pay int comment '支付时间',
  time_created int comment '创建时间',
  time_updated int comment '修改时间',
  is_deleted int comment '是否删除0 1'
)
comment '销售业绩表'
;
