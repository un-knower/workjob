create  table if not exists src.version_channel(
  Id int comment '',
  uid int comment '�û���ţ�����role��ɫ�����壬roleΪ0 user.id roleΪ1user_teacher.id',
  role int comment '��ɫ��0��ѧ����Ĭ����ѧ����1����ʦ',
  clientId string comment '�豸��Ψһ��ʶ',
  version string comment '�汾��',
  channelId int comment '������Դ��ţ� 1:app;2��΢��--crm����url��3��ѶӦ�ñ���4���ٶ��ֻ����֣�����91����׿�г�����5:360�ֻ����֣�6��С��Ӧ���̵ꣻ7����ΪӦ���̵ꣻ8��oppoӦ���̵ꣻ9:vivoӦ���̵ꣻ10������+�㶹��+����+UC��11��pc����',
  createTime int comment '����ʱ��'
)
comment '�û��汾��������Դ'
;
