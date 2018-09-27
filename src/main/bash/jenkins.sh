#!/usr/bin/env bash
work_dir="/data/jenkins/azkaban"
etl_date=`date  +"%Y-%m-%d-%H-%M-%S"`
etl_day=`date  +"%Y-%m-%d"`
JOB_NAME="azkaban"
cd ${work_dir}
# 从 jenkins 获取项目
$(curl --user kingcall:www1234  -o ${JOB_NAME}.zip http://127.0.0.1:8081/jenkins/job/azkaban/ws/DW/Hadoop/azkaban/*zip*/azkaban.zip -s)
log=" ${work_dir}/logs/${etl_day}.log"
if [[ $? -ne 0  ]]; then
   echo "curl faild: time ${etl_date}" >>"${log}"
   exit -1
fi
# 发布到 azkaban
res=`curl -k -X POST --data "action=login&username=azkaban&password=azkaban" https://172.16.3.252:8081 -s`
if [[ `echo $res | jq '.status' | sed s/\"//g` != "success" ]];then
    echo "session_id faild" >> "${log}"
    exit -1
fi
session_id=`echo $res | jq '.["session.id"]' | sed s/\"//g`
res=`curl -k -i -H "Content-Type: multipart/mixed" -X POST --form "session.id=$session_id" --form "ajax=upload" --form "file=@${JOB_NAME}.zip;type=application/zip" --form "project=${JOB_NAME}" https://172.16.3.252:8081/manager -s`
error_msg=`echo $res|awk -F ',' '{if ($1  ~ "error") print $1}'`
if [[ $? -ne 0  ]]; then
   echo "update faild: ${error_msg}" >> "${log}"
   exit -1
fi

# 备份
mv ${JOB_NAME}.zip ${work_dir}/backup/${JOB_NAME}-${etl_date}.zip
echo "job done"

