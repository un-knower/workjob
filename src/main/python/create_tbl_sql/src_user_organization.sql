create  table if not exists src.user_organization(
  id bigint comment 'ͨ��idGen��ȡ',
  organization_id bigint comment '��֯���е�id organization.id',
  user_id bigint comment 'user_account.id',
  user_type int comment '0:δ����, 1:��Ա 2:�鳤 3:���� 4:vp 5:��������Ա',
  department_id bigint comment 'organization.id where type=3',
  group_id bigint comment 'organization.id where type=4',
  operator_id bigint comment '������id',
  state int comment '1:����',
  created_time int comment '����ʱ��',
  updated_time string comment '����ʱ��'
)
comment '�û���ɫ�� 1�Զ�'
;
