create  table if not exists src.teacher_dimission(
  id int comment '',
  teacher_id int comment '��ʦID:user_teacher.id',
  time_day int comment '��ְ/��ְʱ��',
  type int comment '1:��ְ ��2����ְ��3������ͨ����4��������̭��5��������ѵ��̭��6�������˲ſ����á� 7��У�д������̭��8��ע�ᡢ9��������ѵ���ͨ��  ��10��У�д����ͨ�� �� 11��������ѵԱ�����������б��� 12��������13��������ˡ�14�������ְʱ�䡢15����ְ 16����ѵ�ڸ��ֿ��� 17:����ע���������̭  18.���븴ְ',
  operator int comment '������ID',
  before_work_id int comment '�����˴μ�¼֮ǰ��ʦ��״̬ ��Ӧuser_teacher.work_id',
  after_work_id int comment '�����˴μ�¼֮����ʦ��״̬ ��Ӧuser_teacher.work_id',
  create_time int comment '����ʱ��',
  do_type int comment '��̭ԭ��1����ϵ���� 2�������˳� 3�����ϸ� 99��������/��ְԭ��1��������ְ��2�����ˡ�99��������/������ʽ��1���绰��2��΢�š�3�����š�99���������ȣ�����type�ֶ����жϣ�Ĭ��Ϊ��������99��',
  before_master int comment '����ǰ��ѵʦid user_account.id',
  after_master int comment '��������ѵʦid user_account.id',
  before_employedtime int comment '����ǰ��ʦ����ְʱ��',
  after_employedtime int comment '��������ʦ����ְʱ��',
  remark string comment '��ע��Ϣ',
  dimission_status int comment 'typeΪ1ʱʹ�ã�1������ְ  2��������ְ  3����ְ��Ч',
  exam_type int comment 'type=16��Ч �������� 1���������� 2��������ѵ 3��ģ�⿼�� 4:��ְ��ѵ 5:��ʽ����',
  exam_result int comment 'type=16��Ч 1:ͨ��  2����̭  3������һ��  4:δͨ������Խ��п��ˣ�',
  exam_nums int comment '���˵ڼ���',
  research_master int comment 'type=16,exam_type=5ʱʹ�ã���ʽ���˷���Ľ���Ա��user_account.id'
)
comment '(ԭ��������ʦ��ְ/��ְ��¼��) �ֱ�������ʦ�������ڼ�¼��'
;
