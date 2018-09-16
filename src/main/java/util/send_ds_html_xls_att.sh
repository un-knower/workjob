#!/usr/bin/bash

WORK_DIR=$(cd `dirname $0`; pwd)

#param
mail_name=""
recv_list=""
table_info=""
etl_time=""
columns=""
# 就是命令行里输入的条件
where_clause=""
back_time="1d"
mysql_key="rpt_date"
mail_mode="html"
help_str="Usage: \n-r recv_list \n-t etl_time \n-w where_clause \n-f conf_file"
back_unit="days"
back_value="1"
result_file_list=""

#get opt
while getopts "r:t:w:f:h" opt
do
    case $opt in
        r) recv_list=$OPTARG
        ;;
        t) etl_time=$OPTARG
        ;;
        w) where_clause=$OPTARG
        ;;
        f) conf_file=$OPTARG
        ;;
        h) echo -e $help_str
           exit 0
        ;;
        \?) echo "Invalid option: -$OPTARG"
            echo -e $help_str
            exit -1
        ;;
  esac
done

echo ${conf_file}
#get first row for mail_title

mail_name=`head -1 ${conf_file}`
if [[ -n ${mail_title} ]]; then
    mail_name=${mail_title}
fi

conf_name=`echo $(basename $conf_file) | cut -d '.' -f 1`
#check opt
if [[ -z "${mail_name}" || -z "${recv_list}" || -z "${etl_time}" ]];then
    echo "mail_name/recv_list/etl_time should not be empty."
    exit -1
fi

#record read lines
a=0

#check back time
back_unit=${back_time: -1}
back_value=$((10#${back_time%${back_unit}*}))
case $back_unit in
    m) back_unit="month"
    ;;
    d) back_unit="day"
    ;;
    h) back_unit="hour"
    ;;
    *) echo "Invalid unit for back_time.(m-month d-day h-hour)"
       exit -1
    ;;
esac

#time param
etl_time=`date -d "${etl_time} ${back_value} ${back_unit} ago" "+%Y-%m-%d %H:%M:%S"`
etl_time=${etl_time:0:13}":00:00"
etl_date=${etl_time:0:10}
year=${etl_date:0:4}
month=${etl_date:0:7}
hour=${etl_time:11:2}

html_file=${WORK_DIR}/${conf_name}_${etl_date}.html
conf_tmp_file=${WORK_DIR}/${conf_name}.tmp

# 第一行是邮件主题 下面依次是附件数据（也就是从第二行开始的）
tail -n +2 ${conf_file}|sed '/^$/d'>${conf_tmp_file}

while read line
do

    #get conf file
    table_info=`echo $line|awk -F "@@" '{print $1}'`
    title=`echo $line|awk -F "@@" '{print $2}'`
    column_info=`echo $line|awk -F "@@" '{print $3}'`
    columns=`echo $line|awk -F "@@" '{print $4}'`
    where_clause_spec=`echo $line|awk -F "@@" '{print $5}'`

    if [[ ${table_info} == *.* ]];then
        table_type='hive'
        hive_db=`echo ${table_info} | cut -d '.' -f 1`
        table_name=`echo ${table_info} | cut -d '.' -f 2`
    else
        echo "Invalid table_info format."
        exit -1
    fi

    # where_clause 命令行里的输入条件 -w         where_clause_spec 是从配置文件中解析出来的
    # 下面的语句是判断是否为null  由于配置文件中没有相应的语句，所以执行了elseif
    if [[ -n ${where_clause} && -n ${where_clause_spec} ]];then
        hive_where="${where_clause} and ${where_clause_spec}"
    elif [[ -n ${where_clause} ]];then
        hive_where="${where_clause}"
    else
        hive_where="day = '${etl_date}'"
    fi
    # -z 测试字符串是否为空字符串
    #columns   在前面的获取conf info 中获取了表的类型，是mysql 还是hive
    if [[ ${table_type} == "mysql" && -z ${columns} ]];then
        columns="*"
    elif [[ -z ${columns} ]];then
        columns="\`(create_time|year|month|day|hour)?+.+\`"
    fi

    result_file=${WORK_DIR}/${mail_name}_${table_name}_${etl_date}.txt

    echo "select ${columns} from ${hive_db}.${table_name} tmp_tbl where ${hive_where}"
    hive -e "
            set hive.cli.print.header=true;
            set hive.support.quoted.identifiers=none;
            select ${columns} from ${hive_db}.${table_name} tmp_tbl where ${hive_where}
            " > ${result_file}
    echo >> ${result_file}
    sed -i 's/\t/|/g' ${result_file}
    sed -i "1s/tmp_tbl\.//g" ${result_file}

    #check result
    rec_no=`wc -l ${result_file} | cut -d ' ' -f 1`
    if [[ ${rec_no} -le 2 ]];then
        echo "no result for mail."
        exit -1
    fi

    if [[ ! -z ${column_info} ]]; then
         sed -i "1s/.*/${column_info}/g" ${result_file}
    fi

    let a++

    if [[ ${a} -eq 1 ]];then
      echo -e '<html>\n<body>' > ${html_file}
    fi

    echo '<h3 style="font-weight:bold">'${title}'</h3>' >> ${html_file}
    echo '<table border="1" cellspacing="0" cellpadding="1">' >> ${html_file}

    is_head='1'
    cat ${result_file}|sed '/^$/d'|while read line
    do
        if [[ $is_head = '1' ]];then
            echo '<tr style="font-weight:bold;background-color:Lavender">' >> ${html_file}
            is_head='0'
        else
            echo '<tr>' >> ${html_file}
        fi
        OIFS=$IFS
        IFS='§'
        arr=(${line//|/§})
        for field in ${arr[@]}
        do
            echo '<td>'"$field"'</td>' >> ${html_file}
        done
        IFS=$OIFS
        echo '</tr>' >> ${html_file}
    done

    rm -f ${result_file}

    if [[ -n ${where_clause} && -n ${where_clause_spec} ]];then
        hive_where="day>=date_sub('${etl_date}',30) and ${where_clause_spec}"
    elif [[ -n ${where_clause} ]];then
        hive_where="day>=date_sub('${etl_date}',30)"
    fi

    #columns
    if [[ ${table_type} == "mysql" && -z ${columns} ]];then
        columns="*"
    elif [[ -z ${columns} ]];then
        columns="\`(create_time|year|month|day|hour)?+.+\`"
    fi

    result_file=${WORK_DIR}/${title}.txt

    echo "select ${columns} from ${hive_db}.${table_name} tmp_tbl where ${hive_where}"
    hive -e "
            set hive.cli.print.header=true;
            set hive.support.quoted.identifiers=none;
            select ${columns} from ${hive_db}.${table_name} tmp_tbl where ${hive_where}
            " > ${result_file}
    echo >> ${result_file}
    sed -i 's/\t/|/g' ${result_file}
    sed -i "1s/tmp_tbl\.//g" ${result_file}

    #check result
    rec_no=`wc -l ${result_file} | cut -d ' ' -f 1`
    if [[ ${rec_no} -le 2 ]];then
        echo "no result for mail."
        exit -1
    fi

    if [[ ! -z ${column_info} ]]; then
         sed -i "1s/.*/${column_info}/g" ${result_file}
    fi

    if [[ -z ${result_file_list} ]]; then
        result_file_list=${result_file}
    else
        result_file_list="${result_file_list},${result_file}"
    fi

    echo -e '</table>\n' >> ${html_file}
done<${conf_tmp_file}
echo -e '</body>\n</html>' >> ${html_file}

rm -f ${conf_tmp_file}

#txt 文件转xls
python /data1/data_platform/daily_etl_rpt/tools/txt_to_xls.py ${result_file_list} ${WORK_DIR}/${mail_name}_${etl_date} "|"

for tmp_file in $(echo ${result_file_list} | tr "," "\n");
do
    rm -f ${tmp_file}
done

python /data1/data_platform/daily_etl_rpt/tools/send_mail_ex_att.py ${mail_name} ${recv_list} ${html_file} ${WORK_DIR}/${mail_name}_${etl_date}.xls html

rm -f ${WORK_DIR}/${mail_name}_${etl_date}.xls

rm -f ${mail_name}

#python ${WORK_DIR}/send_mail_ex.py "${mail_name} ${etl_date}" "${recv_list}" ${html_file} "html"
rm -f ${html_file}
