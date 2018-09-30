Select * from log a left outer Join bmw_users b on case when a.user_id is null then concat('dp_hive',rand()) else a.user_id end = b.user_id

set mapred.reduce.tasks=10;
select browser_name,',',nvl(sum(charge_amount_curday),0),',',nvl(sum(pay_amount_curday),0) from (
  select distinct uid,browser_name from (
  select uid,browser_name from ods.ods_web_log_event_cleaned where day>='2018-08-01' and day<='2018-08-26'
  union all
  select uid,browser_name from ods.ods_web_log_page_view_cleaned where day>='2018-08-01' and day<='2018-08-26'
  )c
) a inner JOIN (
select charge_amount_curday, pay_amount_curday,uid from dw.dw_user_behavior_info_d where  day>='2018-08-01' and day<='2018-08-26'
) b on a.uid=b.uid GROUP BY browser_name
;


CREATE TABLE `test` (
  `id` int COMMENT 'id',
  `teacher_id` int COMMENT '老师id user_teacher.id',
  `time_day` string COMMENT '统计时间  凌晨时间戳',
  `hour` DOUBLE COMMENT '老师这天总课时长(单位：小时，额外时段上的课)',
  `money` DOUBLE  COMMENT '老师这一天总时段费（额外时段上的课）',
  `class_ids` varchar(200)  COMMENT '老师课程id，以逗号连接',
  `detail` string COMMENT '序列化的每节课成详情 如array(array(''class_id'' => ''111'',//课程id ''start_time'' =>12233,//超出时间开始''end_time'' => 12233,//超出时间结束''fee'' => 0.11//超出时长与基础时段费乘积))',
  `dwinsert_time` string COMMENT '数仓插入时间',
  `dwupdate_time` string COMMENT '数仓更新时间'
) COMMENT '老师每日额外课时长总和及总课时费用统计表'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
;

sqoop import \
--connect jdbc:mysql://172.16.6.69:3306/music \
--username etladmin \
--password adminetl_MK_123 \
--table user_channel \
--hbase-table realtime:user_channel \
--column-family channel \
--hbase-row-key id \
--hbase-create-table \
--hbase-bulkload


