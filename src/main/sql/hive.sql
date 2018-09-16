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