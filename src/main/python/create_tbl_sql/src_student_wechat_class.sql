create  table if not exists src.student_wechat_class(
  id int comment '',
  title string comment '����',
  icon string comment '����ͼ',
  banner_img string comment '�γ�bannerͼ',
  url string comment '���ӵ�ַ',
  class_time int comment '�Ͽ�ʱ��',
  end_time int comment '�γ̽���ʱ��',
  content string comment '�γ̽���',
  teacher_name string comment '������ʦ',
  teacher_description string comment '��ʦ����',
  is_disable int comment '�Ƿ񲻿���',
  is_back int comment '�Ƿ�ɻع�',
  back_description string comment '�ع˽���',
  is_top int comment '�Ƿ���',
  creater int comment '������',
  create_time int comment '����ʱ��',
  modifer int comment '�޸Ļ�ɾ����',
  update_time int comment '�޸�ʱ��',
  is_delete int comment 'ɾ����־',
  send_msg_status int comment '���ûع�ʱ������Ϣ������ֱ��ԤԼ�ͻ���״̬  0δ����  1�ѷ���',
  poster_path string comment '������ַ',
  is_free int comment '�Ƿ����  0 �� 1��',
  is_send int comment ''
)
comment =''
;
