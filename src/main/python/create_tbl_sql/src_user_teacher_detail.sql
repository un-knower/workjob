create  table if not exists src.user_teacher_detail(
  id int comment '����id',
  teacher_id int comment '��ʦid',
  recruit_instrument_info string comment '��ɸ�ó�����json',
  time_created int comment '����ʱ��',
  time_updated int comment '����ʱ��',
  instrument_grade string comment '����id-�����ȼ�  �ö�������   ���磺  1-1,2-1,4-3',
  commission_school_id int comment '��ʦע��ʱɨ��� ���̨��һ�����ʱѡ��� school.id',
  commission_school_belong int comment '��ʦע��ʱ������������������',
  commission_school_type int comment '��ʦע��ʱ������������������',
  commission_school_status int comment '��ʦע��ʱ���������Ƿ����  0 û�� 1 �ѽ���(��̨�����ʦ��ѡ���õ�����)',
  s_teacher_master int comment '������ѵʦ,��ʦע��ʱ����������Ӧ����ʦ����ѵ�ڼ�����һλ��ѵʦid',
  update_time string comment '�޸�ʱ��'
)
comment '��ʦ��Ϣ������'
;
