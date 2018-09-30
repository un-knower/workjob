#coding=utf-8
import os

import pymysql
import sys
from sshtunnel import SSHTunnelForwarder

'''
database:连接的数据库名
user:据库用户名
password:数据库用户密码
jumpbox_id:跳板机ID
jumpbox_port:跳板机PORT
jumpbox_user:跳板机的登录用户名
jumpbox_pw:跳板机的登录密码
server_id:服务器主机IP
server_port:服务器主机PORT
'''
def connet2mysql(database,user='etladmin',password='adminetl_MK_123',jumpbox_id='47.97.212.14',jumpbox_port=59011,jumpbox_user="wangbo",jumpbox_pw="y6mX0@lcxLku9ibg",server_id='172.16.6.69',server_port=3306):
    # 跳板机SSH连接
    with SSHTunnelForwarder(
            (jumpbox_id, jumpbox_port),  # jumpbox跳板机的IP和PORT
            ssh_username=jumpbox_user,  # 跳板机的登录用户名
            ssh_password=jumpbox_pw,  # 跳板机的登录密码
            remote_bind_address=(server_id, server_port)  # 服务器主机所在的IP和PORT
    ) as tunnel:
        # 数据库连接配置，host默认127.0.0.1不用修改
        conn = pymysql.connect(
            host='127.0.0.1',
            port=tunnel.local_bind_port,
            user=user,  # 数据库用户名
            password=password,  # 数据库用户密码
            db=database,
            charset='utf8',
            cursorclass=pymysql.cursors.DictCursor
        )
        return conn

#查询数据库表名
def qryTableName(sql,host="127.0.0.1",username="root",password="www1234",dbsource="kingcall"):
    dbname = 'python'
    connection = pymysql.Connect(
        host=host,
        port=3306,
        user=username,
        passwd=password,
        db=dbsource,
        charset='utf8'
    )
    results = []
    try:
        with connection.cursor() as cursor:
            cursor.execute(sql)
            table_list = [tuple[0] for tuple in cursor.fetchall()]

    finally:
        connection.close()

    for i in table_list:
        # print(i.split("\'")[0])
        results.append(i)
    # print(results)
    return results

#组装hive语句和conf文件
def get_table_info(database,table,host="127.0.0.1",username="root",password="root",dbsource="python", hivedatabase="src",ispartition=True):
    #设置编码字符集

    cols = []
    create_tail=""
    # create_tail_tmp="partitioned by (dt string)\n;"
    comment="comment "
    create_head = "create  table if not exists {0}.{1}(\n".format(hivedatabase, table)
    if "true"==ispartition:
        create_tail ="partitioned by(year string,month string,day string) ;"
    else:
        create_tail =";"
    #调连接数据库方法
    # connect=connet2mysql(database=database)
    with SSHTunnelForwarder(
            ('47.97.212.14', 59011),#跳板机的IP和PORT
            ssh_username="wangbo",#跳板机的登录用户名
            ssh_password="y6mX0@lcxLku9ibg",#跳板机的登录密码
            # ssh_pkey="test.pem",
            remote_bind_address=('172.16.6.69', 3306) #服务器主机所在的IP和PORT
    ) as tunnel:
        # 数据库连接配置，host默认127.0.0.1不用修改
        connect = pymysql.connect(
            host='127.0.0.1',
            port=tunnel.local_bind_port,
            user='etladmin',#数据库用户名
            password='adminetl_MK_123', #数据库用户密码
            db=database,
            charset='utf8',
            cursorclass=pymysql.cursors.DictCursor
        )
        # print("connect:",connect)
        try:
            with connect.cursor(cursor=pymysql.cursors.DictCursor) as cursor:
                cout = cursor.execute("show create table "+table)
                result = cursor.fetchone()
                if (str(result["Create Table"]).split("ENGINE")[1].__contains__("COMMENT")):
                    comment = comment + result["Create Table"].split("COMMENT")[-1].lstrip("=") + '\n'
                else:
                    comment = comment + "=\'\'\n"

                cursor.execute("SHOW FULL FIELDS FROM "+table)
                try:
                    for row in cursor:  # cursor.fetchall()
                        cols.append(row['Field'])
                        if 'bigint' in row['Type']:
                            row['Type'] = "bigint"
                        elif 'int' in row['Type'] or 'tinyint' in row['Type'] or 'smallint' in row['Type'] or 'mediumint' in row['Type'] or 'integer' in row['Type']:
                            row['Type'] = "int"
                        elif 'double' in row['Type'] or 'float' in row['Type'] or 'decimal' in row['Type']:
                            row['Type'] = "double"
                        else:
                            row['Type'] = "string"
                        create_head += "  "+row['Field'] + ' ' + row['Type'] + ' comment \'' + row['Comment'] + '\',\n'
                except Exception as e:
                    print('===========================pasre error===================================')
                    print(e)
        except Exception as e:
            print('===========================pasre error===================================')
            print(e)
        else:
            create_str = create_head[:-2] + '\n' + ')'+"\n" +comment+ create_tail
            table_conf="mysql_tbl|"+str(table)+'\n'+"mysql_cols|"+",".join(cols)+'\n'+"mysql_cond|"+'\n'+"is_partition|"+str(ispartition)
            print("===================================================================================================================================")
            print(create_str)
            return table_conf,create_str
        finally:
            connect.close()

#生成对应的sql和conf文件
def mysql2hive(database,name,host="127.0.0.1",username="root",password="root",dbsource="python"):
    dir_conf = "tbl_conf"
    #如果目录不存在就创建
    if not (os.path.isdir("tbl_conf")):
        os.mkdir("tbl_conf")

    create_tbl_sql = "create_tbl_sql"
    if not (os.path.isdir("create_tbl_sql")):
        os.mkdir("create_tbl_sql")

    table=name
    str1 = get_table_info(database,table,host,username,password,dbsource)
    if(str1 is None ):
        print("未生成结果，表名为：{0}",name)
    else:
        file_conf_dir=str(dir_conf)+"/"+"src_"+table+".conf"
        file_sql_dir =str(create_tbl_sql) + "/" +"src_"+ table + ".sql"
        file_conf = open(file_conf_dir, 'w')
        file_sql = open(file_sql_dir, 'w')
        file_conf.write(str1[0])
        file_sql.write(str1[1])
        file_conf.write('\n')
        file_sql.write('\n')

if __name__ == '__main__':
    dbsource="kingcall"
    databasename = "music" #music 87(82) music_school 1(1)  music_log 1(1)  management 1(1)
    sql="select source_tablename from source_target_ralation2 where source_database='"+databasename+"'"
    #查询源表名
    resList=qryTableName(sql,dbsource=dbsource)
    for name in resList:
        print(name)
        mysql2hive(databasename, name)