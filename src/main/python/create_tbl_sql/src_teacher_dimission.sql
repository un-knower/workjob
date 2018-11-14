create  table if not exists src.teacher_dimission(
  id int comment '',
  teacher_id int comment '老师ID:user_teacher.id',
  time_day int comment '离职/复职时间',
  type int comment '1:离职 、2：复职、3：面试通过、4：面试淘汰、5：社招培训淘汰、6：备用人才库启用、 7：校招待审核淘汰、8：注册、9：社招培训审核通过  、10：校招待审核通过 、 11：分配培训员（进入面试列表）、 12：跟进、13：变更绑定人、14：变更入职时间、15：入职 16：培训期各种考核 17:社招注册待分配淘汰  18.申请复职',
  operator int comment '操作人ID',
  before_work_id int comment '操作此次记录之前老师的状态 对应user_teacher.work_id',
  after_work_id int comment '操作此次记录之后老师的状态 对应user_teacher.work_id',
  create_time int comment '创建时间',
  do_type int comment '淘汰原因（1：联系不上 2：主动退出 3：不合格 99：其他）/离职原因（1：主动离职、2：辞退、99：其他）/跟进方式（1：电话、2：微信、3：短信、99：其他）等，根据type字段来判断，默认为：其他（99）',
  before_master int comment '操作前培训师id user_account.id',
  after_master int comment '操作后培训师id user_account.id',
  before_employedtime int comment '操作前老师的入职时间',
  after_employedtime int comment '操作后老师的入职时间',
  remark string comment '备注信息',
  dimission_status int comment 'type为1时使用，1：待离职  2：撤销离职  3：离职生效',
  exam_type int comment 'type=16有效 考核类型 1：听音测试 2：基础培训 3：模拟考核 4:入职培训 5:正式考核',
  exam_result int comment 'type=16有效 1:通过  2：淘汰  3：再试一次  4:未通过（针对教研考核）',
  exam_nums int comment '考核第几次',
  research_master int comment 'type=16,exam_type=5时使用：正式考核分配的教研员，user_account.id'
)
comment '(原表名：老师离职/复职记录表) 现表名：老师生命周期记录表'
;
