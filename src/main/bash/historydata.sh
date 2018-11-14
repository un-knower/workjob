#!/usr/bin/env bash
dir="/data/BI"
last=${1:0:10}
year=${1:0:4}
month=${1:0:7}
function readfile()
{	
	while read LINE || [[ -n "$LINE" ]]
	do
		result=$result$LINE
	done  < $1
	echo $result
}
# 导出昨日数据到本地 
cd "${dir}"
impala-shell -q  "/*SA_BEGIN(production)*/ SELECT * FROM events WHERE date='${last}' /*SA_END*/" -B --output_delimiter="\001" -o ${dir}/${last}.txt
if [[ $? -ne 0  ]]; then
    error=$(readfile ${dir}/export.log)
    res=`curl http://172.16.3.249:10110/wework/send/platform -X POST -d "message=神策events数据导出失败,具体原因请查看日志:\n${error}" -s`
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
echo ${last} >>/data/BI/history.log
exit 0 
