create  table if not exists src.sales_performance(
  id int comment '',
  kefu_id int comment 'user_account.id ��ǩ�ͷ�ID',
  kefu_id_re int comment 'user_account.id �����ͷ�ID',
  user_id int comment '�û�id',
  order_id int comment 'product_order.id ����ID',
  payment_no string comment 'payment_list.payment_no ��ˮ����',
  actual_fee double comment '������ˮ֧�����',
  time_pay int comment '֧��ʱ��',
  time_created int comment '����ʱ��',
  time_updated int comment '�޸�ʱ��',
  is_deleted int comment '�Ƿ�ɾ��0 1'
)
comment '����ҵ����'
;
