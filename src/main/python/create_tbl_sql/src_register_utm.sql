create  table if not exists src.register_utm(
  user_id int comment '',
  utm_source string comment '���ϵ����Դ',
  utm_medium string comment '���ϵ��ý��',
  utm_term string comment '���ϵ���ִ�',
  utm_content string comment '���ϵ������',
  utm_campaign string comment '���ϵ������',
  utm_url string comment '��������',
  source_ip string comment '��Դ��ַ'
)
comment =''
;
