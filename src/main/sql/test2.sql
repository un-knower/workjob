select
    day,
    if(b.stream_id is null, '未设置', b.stream_name) as stream_name,
    if(os_type is null,'汇总',os_type) as os_type,
    count(1),
    sum(if(stuck_num > 3, 1, 0)) stuck_num,
    nvl(avg(if(load_time >= 0 and player_ver <> 'ksy_1.8.4', load_time, null)), 0) as load_time,
    nvl(avg(100*stuck_duration/play_duration), 0) as stuck_time_per_100_seconds,
    nvl(avg(stuck_num*100000/play_duration), 0) as stuck_num_per_100_seconds
from (
  -- 过滤了记录:发现在这里数据已经被全部过滤掉了
    select day, room_id,supplier_id, os_type, stuck_num, load_time, player_ver, stuck_duration,play_duration
    from (
        -- 合并的结果
        SELECT
            day, s.uuid,s.case_id,2 as log_ver, max(os_type) as os_type, max(os_ver) as os_ver, max(app_ver) as app_ver, max(player_ver) as player_ver, max(net_type) as net_type, max(band_width) as band_width, max(ip) as ip, max(user_id) as user_id,
            max(room_id) as room_id, max(supplier_id) as supplier_id, max(stream_id) as stream_id, max(stream_type) as stream_type, max(stream_quality) as stream_quality, max(decode_type) as decode_type, max(stream_ip) as stream_ip, min(start_time) as start_time,
            round(sum(avg_rate*play_duration)/sum(play_duration)) as avg_rate, max(get_addr_time) as get_addr_time, max(first_read_time) as first_read_time, max(load_time) as load_time, sum(stuck_num) as stuck_num, sum(stuck_duration) as stuck_duration, sum(play_duration) as play_duration
        FROM (
          SELECT
          day,start_time, avg_rate, get_addr_time, first_read_time, load_time, stuck_num, stuck_duration, play_duration,case_id
          from ods.ods_sdk_player_log_event
          WHERE day='2018-08-01'
          AND  from_unixtime(start_time, 'yyyy-MM-dd') ='2018-08-02'
          AND  play_duration > 0 and play_duration <= 18000000 AND stuck_duration >= 0 and stuck_duration <= 1800000  AND length(case_id) >4
        ) e RIGHT JOIN (
          SELECT
          uuid, case_id, 2 as log_ver, os_type, os_ver, app_ver, player_ver, net_type, band_width, ip, user_id, room_id, supplier_id, stream_id, stream_type, stream_quality, decode_type, stream_ip
          from ods.ods_sdk_player_log_status where day='2018-08-01' AND  app_ver >= '5.0.0' AND length(case_id) > 4 AND length(uuid) > 4
        ) s ON s.case_id=e.case_id
        WHERE (avg_rate > 0.0001 or  os_type in ('I', 'A'))
        group by day,s.uuid,s.case_id
        having count(1) <= 8640
    )b
    where day = '2018-08-01'
    and play_duration > stuck_duration
    and first_read_time <= 180000
  )a left outer join(
  select stream_id, stream_name from dim.dim_stream_type where status = 1
)b on a.supplier_id = b.stream_id
group by day,
if(b.stream_id is null, '未设置', b.stream_name),os_type grouping sets((day,if(b.stream_id is null, '未设置', b.stream_name),os_type),(day,if(b.stream_id is null, '未设置', b.stream_name)))
order by stream_name, os_type;