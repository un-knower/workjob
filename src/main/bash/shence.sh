#!/usr/bin/env bash
dir="/data/BI"
last=`date +"%Y-%m-%d" -d  "-1 days"`
year=`date +"%Y" -d  "-1 days"`
month=`date +"%Y-%m" -d  "-1 days"`
function readfile()
{
	while read LINE || [[ -n "$LINE" ]]
	do
		result=$result$LINE
	done  < $1
	echo $result
}
#impala-shell -q  "drop table rawdata.export_data"
sql1="
 CREATE TABLE rawdata.export_data AS /*SA_BEGIN(production)*/
 select
    event,user_id,distinct_id,date,time,\$latest_utm_medium AS latest_utm_medium,t_channel_var,text_content,shareType,mfm_userid_share,user_isLogin,select_content,brand,\$element_id AS element_id,course_isRed,user_life_cycle,course_isSoundRecording,course_isWrittenTeacherComment,register_date_time,\$model AS model,user_isFirstcomplete,\$browser AS browser,course_date_time,network_condition,\$country AS country,\$utm_campaign AS utm_campaign_1,video_name,course_reserveType,page_type,user_name,mpl_userid_share,\$ip AS ip,\$screen_height AS screen_height,verificationcode_state,shareContent,course_instrument_Type,course_classType,t_OS_type,medalID,course_proficiencyScore,t_course_number,\$referrer_host AS referrer_host,course_edit_date_time,course_praiseAmount,button_name,teacher_isOpentime,t_device_version,course_Time,course_isWrittenComment,register_type,course_labelList_positive,text_name,course_labelList_nagative,fail_type,device_is_passed,courseID,\$track_signup_original_id AS track_signup_original_id,course_rhythmScore,page_url,\$utm_medium AS utm_medium_1,\$element_type AS element_type,app_version,device_firm,\$is_first_time AS is_first_time,teacherID,entrance_Type,mfm_id,entranceArea,t_course_instrument_type,area,tabName,mfm_enter_time,utm_campaign,teacher_Type,\$screen_width AS screen_width,OS_type,music_uploadChannel,\$title AS title,\$latest_traffic_source_type AS latest_traffic_source_type,mpl_title,utm_source,vk_session_id,wifitest_is_passed,promopt,musiccourseID,\$browser_language AS browser_language,t_teacher_grade,\$referrer AS referrer,fisheyeTest_is_passed,\$url_path AS url_path_1,\$element_position AS element_position,\$url AS url_1,\$latest_utm_term AS latest_utm_term,client,t_teacher_name,\$kafka_offset AS kafka_offset,url_path,utm_content,\$carrier AS carrier,\$os_version AS os_version_1,\$is_first_day AS is_first_day,course_payType,message,t_imei,teacher_Level,t_score_id,teacher_Grade,course_expressivenessScore,imei,user_kid_name,poster_id,install_date_time,\$manufacturer AS manufacturer,device_version,button_type,\$os AS os,video_position,\$wifi AS wifi,t_song_name,cource_edit_type,domain_name,device_imei,content_detail,\$scene AS scene,idfv,to_page_url,course_is_Red,\$bot_name AS bot_name,t_music_attribute,OS_version,register_source,idfa,course_complete_date_time,\$app_version AS app_version_1,t_install_date_time,finish_type,\$resume_from_background AS resume_from_background,\$browser_version AS browser_version,\$viewport_position AS viewport_position,user_is_paid,t_idfv,mfm_exit_time,t_idfa,mpl_enter_time,product,aaaaaaaaa,\$viewport_width AS viewport_width,\$utm_source AS utm_source_1,url,user_id_mk,device_mac,\$utm_content AS utm_content_1,t_teacher_id,page_path,button_position,scoreID,user_life_Cycle_,login_type,course_Date,utm_medium,\$network_type AS network_type,music_Attribute,t_OS_version,user_isFirstreserve,\$province AS province,\$latest_search_keyword AS latest_search_keyword,\$element_content AS element_content,to_page_title,install_time,coordinate_x,\$city AS city,coordinate_y,event_duration,mpl_id,course_teacherRating,mfm_source,course_Duration_minutes,parent_id,channel_var,control_detail,t_platform,video_Duration_minutes_4,video_Duration_minutes_3,userType,position,\$screen_name AS screen_name,video_Duration_minutes_2,phone_Number,video_Duration_minutes_1,\$utm_term AS utm_term_1,platform,\$viewport_height AS viewport_height,course_isScreenshot,teacher_Name,\$device_id AS device_id,\$latest_utm_campaign AS latest_utm_campaign,course_Requirement_uploadChannel,t_userType,\$latest_referrer AS latest_referrer,mfm_title,medal_name,t_android_id,functionName,t_app_version,viewChannel,student_id,\$latest_utm_source AS latest_utm_source,course_isSoundComment,banner_index,promopt_type,share_path,utm_term,course_Score,t_course_id,course_intonationScore,android_id,\$latest_referrer_host AS latest_referrer_host,\$latest_utm_content AS latest_utm_content
 from events
 where \`date\`='${last}'
 /*SA_END*/
"
#impala-shell -q "${sql1}"
#impala-shell -q  "select * from rawdata.export_data" -B --output_delimiter="\001" -o "${dir}/${last}.txt"
# 导出昨日数据到本地
cd "${dir}"
impala-shell -q  "/*SA_BEGIN(production)*/ SELECT * FROM events WHERE date='${last}' /*SA_END*/" -B --output_delimiter="\001" -o ${dir}/${last}.txt
if [[ $? -ne 0  ]]; then
    error=$(readfile ${dir}/error.log)
    res=`curl http://172.16.3.249:10110/wework/send/platform -X POST -d "message=神策数据导出失败,具体原因请查看日志:\n${error}" -s`
    rm -f ${dir}/${last}.txt
    exit 1
fi
scp ${last}.txt 172.16.3.249:/mnt/disk1/bidata
rm -f ${dir}/${last}.txt
ssh 172.16.3.249 > /dev/null 2>&1 << eeooff
hive -e "load data local inpath \"/mnt/disk1/bidata/${last}.txt\" overwrite into table src.src_events partition(year='${year}',month='${month}',day='${last}')"
rm -f "/mnt/disk1/bidata/${last}.txt"
exit
eeooff
