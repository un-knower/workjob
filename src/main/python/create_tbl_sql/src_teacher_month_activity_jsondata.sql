create  table if not exists src.teacher_month_activity_jsondata(
  id int comment '',
  teacher_id int comment '老师id, user_teacher.id',
  time_day int comment '统计月份 每月1号时间戳',
  content string comment '序列化的活动内容，反序列化为数组格式，例：array(array('name'=>'活动名称', 'status' =>'1' //1、待结算，2、已结算 'money'=>''//金额 ))',
  update_time string comment '更新时间'
)
comment '老师月活动数据表'
;
