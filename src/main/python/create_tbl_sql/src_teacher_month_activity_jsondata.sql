create  table if not exists src.teacher_month_activity_jsondata(
  id int comment '',
  teacher_id int comment '��ʦid, user_teacher.id',
  time_day int comment 'ͳ���·� ÿ��1��ʱ���',
  content string comment '���л��Ļ���ݣ������л�Ϊ�����ʽ������array(array('name'=>'�����', 'status' =>'1' //1�������㣬2���ѽ��� 'money'=>''//��� ))',
  update_time string comment '����ʱ��'
)
comment '��ʦ�»���ݱ�'
;
