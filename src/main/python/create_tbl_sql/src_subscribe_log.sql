create  table if not exists src.subscribe_log(
  id int comment '',
  unique_id string comment '',
  open_id string comment 'open_id',
  is_expired int comment '�Ƿ�ΪʧЧɨ����Ϊ',
  event int comment '�¼����ͣ� ��ע�¼�=10   scan = 20',
  create_time int comment '����ʱ��',
  update_time string comment '�޸�ʱ��'
)
comment =''
;
