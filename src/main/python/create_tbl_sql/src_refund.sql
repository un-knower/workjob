create  table if not exists src.refund(
  id int comment '',
  user_id int comment '',
  order_id int comment '',
  amount int comment '',
  price double comment '',
  time_created int comment '',
  left_id int comment 'class_left.id',
  reason string comment '�˷�����',
  class_type string comment '�γ�����/ʱ��/����',
  user_name string comment '�˿�������',
  user_card_no string comment '�˿������п���',
  user_bank_name string comment '�˿������',
  mobile string comment '�ֻ���',
  refund_status int comment '0���˷ѣ�1�˷��У�2�˷ѳɹ�',
  refund_channel string comment '�˿�����:����������paypal��֧����������',
  refund_type string comment '�˿�ԭ��:����ѧ�� ��ʦ���� ���η��� �����ײ͵�',
  currency string comment '����',
  province string comment '����ʡ',
  city string comment '������'
)
comment =''
;
