create  table if not exists src.teacher_extra_class_money(
  id int comment '',
  teacher_id int comment '��ʦid user_teacher.id',
  time_day int comment 'ͳ��ʱ��  �賿ʱ���',
  hour double comment '��ʦ�����ܿ�ʱ��(��λ��Сʱ������ʱ���ϵĿ�)',
  money double comment '��ʦ��һ����ʱ�ηѣ�����ʱ���ϵĿΣ�',
  class_ids string comment '��ʦ�γ�id���Զ�������',
  detail string comment '���л���ÿ�ڿγ����� ��array(array('class_id' => '111',//�γ�id 'start_time' =>12233,//����ʱ�俪ʼ'end_time' => 12233,//����ʱ�����'fee' => 0.11//����ʱ�������ʱ�ηѳ˻�))'
)
comment '��ʦÿ�ն����ʱ���ܺͼ��ܿ�ʱ����ͳ�Ʊ�'
;
