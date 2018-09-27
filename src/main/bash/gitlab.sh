#!/usr/bin/env bash
# 基本工作目录 时间参数
etl_date=`date  +"%Y-%m-%d-%H-%M-%S"`
WORK_DIR=/data/BI/DW/Hadoop
BACKUP_DIR=/data/backup
JOB_NAME=azkaban
# 更新源代码
BI_DIR=/data/BI
cd ${BI_DIR}
git pull
if [[ $? -ne 0  ]]; then
   echo "git pull faild"
   exit -1
fi
# 打包 登录 上传
cd ${WORK_DIR}
zip ${JOB_NAME}.zip -q -r ${JOB_NAME}
res=`curl -k -X POST --data "action=login&username=azkaban&password=azkaban" https://127.0.0.1:8081 -s`
if [[ `echo $res | jq '.status' | sed s/\"//g` != "success" ]];then
    echo "Error when getting azkaban session.id"
    exit -1
fi
session_id=`echo $res | jq '.["session.id"]' | sed s/\"//g`
echo "get seesion_id: $session_id"
res=`curl -k -i -H "Content-Type: multipart/mixed" -X POST --form "session.id=$session_id" --form "ajax=upload" --form "file=@${JOB_NAME}.zip;type=application/zip" --form "project=${JOB_NAME}" https://127.0.0.1:8081/manager -s`
error_msg=`echo $res|awk -F ',' '{if ($1  ~ "error") print $1}'`
echo ${error_msg}
# 备份
mv ${JOB_NAME}.zip ${BACKUP_DIR}/${JOB_NAME}-${etl_date}.zip
echo "job done"

