#!/usr/bin/bash
#DIM_DB=dim              #正式维表库
#DIM_TMP_DB=dim_tmp      #临时维表库
ODS_DB=ods               #正式ods库
ODS_TMP_DB=src           #临时ods库
TMP_DB_PARTITION_KEEP=7 #
WORK_DIR=$(cd `dirname $0`; pwd)
cd ${WORK_DIR}

#定义变量
tbl_conf=""
etl_date=""
back_days=1
map_num=4
exec_freq=""
delay=0
help_str="Usage:
     -f tbl_conf
     -t etl_date
     -c db_source
     -i is_init
     -b back_days(default 1)
     -m map_num(default 1, 1-30)
     -x exec_freq(default every day, wn-week n in every week(w0-Sunday), dn-day n in every month)
     -d delay(default 0 min)"
#调用示例:
#sh sqoop_import.sh -f /usr/shellfile/table_cfg/src_admin_role.cfg -t 2018-09-21 -c /usr/shellfile/base_cfg/mysql_source_69.cfg
#sh sqoop_import.sh -f /home/hadoop/table_cfg/src_admin_role.cfg -t 2018-09-21 -c  /home/hadoop/base_cfg/mysql_source_69.cfg
#参数说明:
#-f tbl_conf -- 同步表的配置文件path，绝对路径
#-t 2018-09-21  --本次同步的时间
#-c db_source --数据库的连接参数配置文件path，绝对路径
#-i 是否需要进行初始化，如果初始化会自动忽略table_cfg中的增量条件
#-b back_days(default 1) 默认的时候为etl_date的前一天
#-m map_num(default 1, 1-30) 定义启动的map数量
#-x exec_freq(default every day, wn-week n in every week(w0-Sunday), dn-day n in every month) 暂时停用
#-d delay(default 0 min)

#get opt,控制多个命令行参数,根据定义的参数类型进行传参
while getopts "f:t:c:i:b:m:x:d:h" opt
do
    case $opt in
        f) tbl_conf=$OPTARG
        ;;
        t) etl_date=$OPTARG
        ;;
        c) db_source=$OPTARG
        ;;
        i) is_init=$OPTARG
        ;;
        b) back_days=$((10#$OPTARG))
        ;;
        m) map_num=$((10#$OPTARG))
        ;;
        x) exec_freq=$OPTARG
        ;;
        d) delay=$((10#$OPTARG))
        ;;
        h) echo $help_str
           exit 0
        ;;
        \?) echo "Invalid option: -$OPTARG"
            echo $help_str
            exit -1
        ;;
  esac
done

#check opt，tbl_conf，etl_date，db_source三个参数必传
if [[ -z "${tbl_conf}" || -z "${etl_date}" || -z "${db_source}" ]];then
    echo "table conf/etl_date/db_source should not be empty."
    exit -1
fi

echo "==========================Date Bgein=========================="
echo "传入的时间(当天的时间):${etl_date}"
#check exec condition
#exec_d=`"date -d "${etl_date}" "+%d`
#echo "111111111"
#exec_d_ex=${exec_d##*0}
#exec_w=`date -d "${etl_date}" "+%w"`
#if [[ -n ${exec_freq} && ${exec_freq} != "d${exec_d}" && ${exec_freq} != "d${exec_d_ex}" && ${exec_freq} != "w${exec_w}" ]];then
#    echo "Not in run time and return success. exec_freq=${exec_freq}, exec_d=${exec_d}, exec_w=${exec_w}"
#    exit 0
#fi

#判断map的数量，必须在1-30之间
if [[ ${map_num} -gt 30 || ${map_num} -lt 0 ]];then
    echo "map_num limit to 1 - 30."
    exit -1
fi

# 统一的时间处理
today=${etl_date}
#默认当天跑上一天的数据
etl_date=`date -d "${etl_date} ${back_days} days ago" "+%Y-%m-%d"`
# 分区标示
dt_date=`date -d "${etl_date}" "+%Y%m%d"`
#etl_date的时间段 如2018-09-01 00:00:00" --- 2018-09-01 23：59：59
runtime_begin="${etl_date} 00:00:00"
runtime_end="${etl_date} 23:59:59"

#next_datetime=`date -d "${etl_date} tomorrow" "+%F"`" 00:00:00"

#执行时间的unix_timestamp
runtime_unix_begin=`date -d "${etl_date}" "+%s"`
runtime_unix_end=`date -d "${runtime_end}" "+%s"`

#通过表配置中的文件名称解析同步目标表的表名
tbl_name=`echo ${tbl_conf##*/} | cut -d '.' -f 1`

yyyymmdd=`date -d "${etl_date}" "+%Y%m%d"`
yyyymm=`date -d "${etl_date}" "+%Y%m"`


echo "实际获取数据的时间today:${today}"
echo "etl_date:${etl_date}"
echo "runtime_begin:${runtime_begin}"
echo "runtime_end:${runtime_end}"
echo "runtime_unix_begin:${runtime_unix_begin}"
echo "runtime_unix_end:${runtime_unix_end}"
echo "tbl_name:${tbl_name}"

echo "==========================Date End=========================="
#delay
sleep ${delay}m

#记录程序运行的开始时间点
start_time=`date "+%Y-%m-%d %H:%M:%S"`
echo "start_time:${start_time}"
echo "run sqoop import with param: ${tbl_conf} ${etl_date} ${back_days} ${map_num}"
#read table config
while read line || [[ -n "$line" ]]
do
    line=`echo ${line} | sed "s/{etl_date}/'${etl_date}'/g" | sed "s/{runtime_unix_begin}/${runtime_unix_begin}/g" | sed "s/{runtime_unix_end}/${runtime_unix_end}/g" | sed "s/{runtime_begin}/'${runtime_begin}'/g" | sed "s/{runtime_end}/'${runtime_end}'/g" | sed "s/{today}/'${today}'/g" | sed "s/{yyyymmdd}/${yyyymmdd}/g" | sed "s/{yyyymm}/${yyyymm}/g"`
    tag=`echo ${line} | cut -d "|" -f 1`
    value=`echo ${line} | cut -d "|" -f 2`
    case ${tag} in
#        "db_inst") db_inst=${value}
#        ;;
        "mysql_tbl") mysql_tbl=${value}
        ;;
        "mysql_cols") mysql_cols=${value}
        ;;
        "mysql_cond") mysql_cond=${value}
        ;;
        "is_partition") is_partition=${value}
        ;;
        "bit_columons") bit_columons=${value}
        ;;
    esac
done < ${tbl_conf}

echo " mysql_tbl:${mysql_tbl}"
echo " mysql_cols:${mysql_cols}"
echo " mysql_cond:${mysql_cond}"
echo " is_partition:${is_partition}"
echo " bit_columons:${bit_columons}"
# 处理 bit 数据类型
arr=($(echo ${bit_columons}| sed  's/,/ /g'))
options="${WORK_DIR}/${mysql_tbl}.options"
for x in ${arr[@]}
do
	echo "--map-column-hive" >> "${options}"
	echo "${x}=string" >> "${options}"
done
if [[ ${#arr[@]} -eq 0 ]]; then
    touch ${options}
fi
if [ "${tbl_name:0:3}" = 'src' ];then
    hive_db=${ODS_DB}
    hive_tmp_db=${ODS_TMP_DB}
    echo "hive_db:${ODS_DB}"
    echo "hive_tmp_db:${ODS_TMP_DB}"
else
    hive_db=${DIM_DB}
    hive_tmp_db=${DIM_TMP_DB}
fi
#如果源表的where条件为空，则自动补上 1=1，
#如果is_init=true，则覆盖原来的条件，进行全量抽取
echo "is_init:${is_init}"
if [[ "${mysql_cond}" == ""  || "${is_init}" == "true" || "${is_init}" == "1" ]];then
    mysql_cond="1 = 1"
fi
echo "处理后的mysql_cond:${mysql_cond}"

#if [[ -z "${db_inst}" ]];then
#    echo "Empty db info!"
#    exit -1
#fi

#开始记录写入日志
#todo 需要补充对于表记录的判断，如果表记录不存在，则需要插入一条数据
while read line;do
    eval "$line"
done < /data/BI/DW/Hadoop/sqoop/conf/kettle_log_source.cfg
echo "开始记录状态日志到${mysql_log_ip}"
echo "日志mysql信息:${mysql_log_ip},${mysql_log_port},${log_username},${log_passwd}"

mysql -h${mysql_log_ip} -P${mysql_log_port} -u${log_username} -p${log_passwd} -e "update kettle.execinfo set CALCSTARTTIME='${start_time}',STATUS='finished' where NAME= '${hive_tmp_db}.${tbl_name}'"

#db_inst_arr=(${db_inst})
#db_mysql=${MYSQL_CONF}
#if [[ "${db_inst}" == "MycatUCReadOnly" ]];then
#    db_mysql=${MYSQL_CONF_P}
#    db_inst_arr=("MycatUCSplit1" "MycatUCSplit2" "MycatUCSplit3" "MycatUCSplit4")
#fi

overwrite="--hive-overwrite"
#for db_inst_i in ${db_inst_arr[@]}
#do
    #get mysql conf
#    if [[ -z "${db_inst_i}" ]];then
#        echo "Empty db_inst"
#        exit -1
#    fi

# 加载db源基础信息资源文件，包含${mysql_ip}，${mysql_port}，${mysql_db}，${username}，${passwd}
while read line;do
    eval "$line"
done < ${db_source}

    mysql_ip="${mysql_ip}"
    mysql_port"=${mysql_port}"
    mysql_db="${mysql_db}"
    connect_str="jdbc:mysql://${mysql_ip}:${mysql_port}/${mysql_db}?tinyInt1isBit=false&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false"
    username="${username}"
    passwd="${passwd}"
    echo "mysql_ip:${mysql_ip}"
    echo "mysql_port:${mysql_port}"
    echo "mysql_db:${mysql_db}"
    echo "username:${username}"
    echo "passwd:${passwd}"
    echo "connect_str:${connect_str}"

    #sqoop 导入至src库
    #src库使用dt分区，保留最近几天的导入数据
    echo "dump data from ${mysql_ip} ${connect_str}..."
    echo "hive 分区 ${dt_date}"
    /opt/apps/ecm/service/sqoop/1.4.6-1.0.0/package/sqoop-1.4.6-1.0.0/bin/sqoop import -Dmapreduce.job.queuename=default \
        --connect "${connect_str}" --username "${username}"  --password "${passwd}" \
        --table "${mysql_tbl}" \
        --columns "${mysql_cols}" \
        --where "${mysql_cond}" \
        --hive-table "${hive_tmp_db}.${tbl_name}" \
        --hive-partition-key dt --hive-partition-value ${dt_date} \
        --hive-delims-replacement " " \
        --null-string '\\N' --null-non-string '\\N' \
        --target-dir "/user/sqoop/import_tmp/${tbl_name}/" \
        --delete-target-dir \
        --hive-import ${overwrite} \
        --direct -m ${map_num} \
        --options-file "${options}"

    ret_code=$?
    if [[ $ret_code -eq 0 ]];then
        echo "Sucess  when run sqoop import. $ret_code"
        rm -f ${WORK_DIR}/${mysql_tbl}.java
        rm -f ${options}
    fi
    overwrite=""

#done

#hive 命令将tmp库导入正式库
#大部分ods表使用year month day三级分区，是否分区在配置文件中设置
#year=${etl_date:0:4}
#month=${etl_date:0:7}
#if [ "${is_partition}" = 'true' ];then
#    partition_cond=" partition(year='${year}',month='${month}',day='${etl_date}')"
#fi
#hive -e "set hive.support.quoted.identifiers=none;
#         insert overwrite table ${hive_db}.${tbl_name}${partition_cond}
#         select \`(dt)?+.+\` from ${hive_tmp_db}.${tbl_name} where dt='${etl_date}'"
#
##删除tmp库中的过期分区
expire_date=`date -d "${etl_date} -${TMP_DB_PARTITION_KEEP} days" "+%Y%m%d"`
hive -e "ALTER TABLE ${hive_tmp_db}.${tbl_name} DROP IF EXISTS PARTITION (dt <= '${expire_date}')"


#校验mysql和hive两边数据条数
#get mysql conf
#db_info=`cat ${db_mysql} | jq ". | map(select(.InstanceName == \"${db_inst}\"))[0]"`
#conn_host=`echo ${db_info} | jq '.Server' | sed s/\"//g`
#conn_port=`echo ${db_info} | jq '.Port' | sed s/\"//g`
#conn_db=`echo ${db_info} | jq '.DB' | sed s/\"//g`
#username=`echo ${db_info} | jq '.UserID' | sed s/\"//g`
#passwd=`echo ${db_info} | jq '.Password' | sed s/\"//g`
#if [ -n "${mysql_cond}" ]; then
#    mysql_cond="where "${mysql_cond}
#fi
#mysql_num=`mysql -h${conn_host} -P${conn_port} -u${username} -p${passwd} -e "use ${conn_db};select count(*) as cnt from ${mysql_tbl} ${mysql_cond}"`
#hive_num=`hive -e "select count(*) as cnt from ${hive_tmp_db}.${tbl_name} where dt = '${etl_date}'"`
#if [ ${mysql_num:4} = ${hive_num} ];then is_match='true'; else is_match='false'; fi

end_time=`date "+%Y-%m-%d %H:%M:%S"`

#db_info=`cat ${MYSQL_CONF} | jq ". | map(select(.InstanceName == \"datajob\"))[0]"`
#mysql_ip=`echo ${db_info} | jq '.Server' | sed s/\"//g`
#mysql_port=`echo ${db_info} | jq '.Port' | sed s/\"//g`
#username=`echo ${db_info} | jq '.UserID' | sed s/\"//g`
#passwd=`echo ${db_info} | jq '.Password' | sed s/\"//g`
#写入日志
echo "写入状态日志到${mysql_log_ip}"
echo "执行的sql语句：update kettle.execinfo set CALCSTARTTIME='${start_time}',CALCENDTIME='${end_time}',EXCSTARTTIME='${start_time}',EXCENDTIME='${end_time}',STATUS='finished' where NAME= '${hive_tmp_db}.${tbl_name}'"
mysql -h${mysql_log_ip} -P${mysql_log_port} -u${log_username} -p${log_passwd} -e "update kettle.execinfo set CALCSTARTTIME='${start_time}',CALCENDTIME='${end_time}',EXCSTARTTIME='${start_time}',EXCENDTIME='${end_time}',STATUS='finished' where NAME= '${hive_tmp_db}.${tbl_name}'"
#"insert into import_result VALUES ('${tbl_name}', '${etl_date}', '${start_time}', '${end_time}', ${mysql_num:4}, ${hive_num}, '${is_match}', 0)"
#rm -f ${WORK_DIR}/${mysql_tbl}.java


