create  table if not exists src.user_organization(
  id bigint comment '通过idGen获取',
  organization_id bigint comment '组织表中的id organization.id',
  user_id bigint comment 'user_account.id',
  user_type int comment '0:未分配, 1:组员 2:组长 3:主管 4:vp 5:超级管理员',
  department_id bigint comment 'organization.id where type=3',
  group_id bigint comment 'organization.id where type=4',
  operator_id bigint comment '操作人id',
  state int comment '1:可用',
  created_time int comment '创建时间',
  updated_time string comment '更新时间'
)
comment '用户角色表 1对多'
;
