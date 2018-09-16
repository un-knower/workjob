-- ========================================================= mysql 中的一些函数 ==============================================================================================
-- IFNULL(expr1,expr2) 如果 expr1为null则expr2 都为null则结果为null
-- COALESCE ("ac",NULL,"a") IFNULL(expr1,expr2) 的升级版本支持无限多参数

-- 各个班级的平均分
                                                SELECT class,avg(degree) from score a,student b WHERE a.sno=b.sno GROUP BY class ORDER BY class
-- 平均成绩大于八十分的同学学号和平均成绩
                                                SELECT sno,AVG( degree)FROM score GROUP BY sno HAVING avg(degree)>80
                                                SELECT a.sno  as 学号,b.sname as 姓名,count( degree) as 选课数,sum(degree) as 总成绩 FROM score a,student b WHERE a.sno=b.sno GROUP BY a.sno
                                                SELECT COUNT(tno) as 教师个数 FROM teacher WHERE tname like '李%'
                                                没有学过王萍
                                                SELECT * from score where sno NOT in(SELECT sno from score a,course b,teacher c WHERE c.tno=b.tno AND c.tname='王萍' AND  a.cno=b.cno)
                                                同时学过两门（特定）课的人
                                                SELECT * FROM(select * from score WHERE cno='3-105')a,(SELECT * from score WHERE cno='3-245') b WHERE a.sno=b.sno
                                                同时学过两门课的人
                                                SELECT sno AS 学号,COUNT(cno) as 学过的课程数目,GROUP_CONCAT(cno) FROM score GROUP BY sno
-- 某一门比另一门高
                                                SELECT * FROM(select * from score WHERE cno='3-105')a,(SELECT * from score WHERE cno='3-245') b WHERE a.sno=b.sno AND a.degree>=b.degree
                                                所有课程都小于60的人
                                                SELECT * from score WHERE sno in(SELECT sno from score GROUP BY sno HAVING avg(degree)>80)
                                                没有学完所有课程的同学
                                                SELECT sno as 学号,COUNT(cno) as 课程数目 FROM score GROUP BY sno HAVING COUNT(cno)!=(SELECT COUNT(cno) FROM course)

                                                SELECT student.SNO,student.SNAME,score.DEGREE,course.CNAME,grade.* FROM score,grade,student,course WHERE score.DEGREE>=grade.low and score.DEGREE<=grade.upp AND
                                                score.SNO=student.SNO AND course.CNO=score.CNO

                                                SELECT * FROM score WHERE CNO='3-105' AND DEGREE >=(SELECT DEGREE FROM score where SNO='109' AND CNO='3-105')

                                                SELECT * from score WHERE SNO in (SELECT SNO FROM score GROUP BY SNO HAVING COUNT(SNO)>1) AND degree <(select MAX(degree) FROM score) ORDER BY sno

SELECT * FROM student WHERE year(SBIRTHDAY)=(SELECT year(SBIRTHDAY) FROM student WHERE sno='109')

SELECT t.TNO ,t.Tname ,c.CNO,c.CNAME FROM teacher t ,course c WHERE t.Tname="张旭" AND c.TNO=t.TNO
SELECT score.*,course.CNAME,teacher.TNAME FROM score,course,teacher WHERE score.CNO=(SELECT c.CNO FROM teacher t ,course c WHERE t.Tname="张旭" AND c.TNO=t.TNO) AND
score.CNO=course.CNO AND teacher.TNO=course.TNO

SELECT * from teacher WHERE TNO in (SELECT TNO FROM course WHERE CNO in (SELECT(SELECT CNO FROM score GROUP BY CNO HAVING COUNT(SNO)>5)) )

SELECT * FROM student WHERE CLASS in(95033,95031)
SELECT * FROM student WHERE CLASS=95033
UNION ALL
SELECT * FROM student WHERE CLASS=95031
                                                SELECT CNO, degree FROM score WHERE degree>85
                                                SELECT score.*,course.CNAME from score,course WHERE score.CNO in(SELECT CNO from course WHERE TNO in(select TNO from  teacher WHERE depart='计算机系')) AND course.CNO=score.CNO

                                                SELECT tno,TNAME,prof from teacher WHERE depart in('计算机系','电子工程系') GROUP BY prof

                                                SELECT * FROM score WHERE sno in(SELECT sno FROM)

select * from score WHERE sno in(
  SELECT sno FROM(
    SELECT sno,
           MAX(
           CASE CNO
           WHEN "3-105" THEN degree
           ELSE 0
           END
           )
           AS s3105,
           MAX(
           CASE CNO
           WHEN "3-245" THEN degree
           ELSE 0
           END
           )
           AS s3245
    FROM score
    GROUP BY sno
  )x
 WHERE s3105>=s3245 AND s3245!=0
)
ORDER BY sno DESC
;
-- 这个操作解决一部分行列互转的问题
                                                SELECT a.* ,b.cno,b.degree FROM(SELECT * from score WHERE cno='3-105') a ,(SELECT * FROM score WHERE cno='3-245') b WHERE a.sno=b.sno AND a.degree>b.degree

                                                SELECT * FROM SCORE WHERE DEGREE>ANY(SELECT DEGREE FROM SCORE WHERE CNO='3-245') AND cno='3-105' ORDER BY DEGREE DESC;

                                                select tname as 姓名,tsex as 性别,tbirthday  as 生日, '教师' as 角色 from teacher
                                                union ALL
                                                select sname,ssex,sbirthday,'学生' as 角色 FROM student
SELECT b.* FROM score b INNER JOIN (SELECT cno,AVG(degree) as avgdegree from score GROUP BY cno) a ON a.cno=b.cno AND degree <=avgdegree
SELECT * from teacher WHERE tno in(SELECT tno from course)
SELECT * from teacher WHERE tno not in(SELECT tno from course)
SELECT  class from student WHERE ssex='男' GROUP BY class HAVING COUNT(sno)>=2
                                                SELECT * from student WHERE sname not LIKE "王%"
                                                SELECT sname,YEAR(NOW())-YEAR(sbirthday) as '年龄' from student
                                                SELECT date(MAX(sbirthday)) as 小,date(min(sbirthday)) as 大 from student
                                                SELECT * from student ORDER BY class DESC,sbirthday ASC
                                                SELECT a.tname,a.tsex,b.cname from teacher a, course b where a.tno=b.tno and a.tsex='男'
                                                SELECT sname,ssex,class from student WHERE ssex=(SELECT ssex from student WHERE sname='李军') AND class=(SELECT class from student WHERE sname='李军')
查询所有选修“计算机导论”课程的“男”同学的成绩表
                                                SELECT a.* from score a,course b,student c WHERE a.cno=b.cno and c.ssex='男' AND b.cname='计算机导论' AND c.sno=a.sno
                                                SELECT * FROM score WHERE sno in(SELECT sno from student where ssex='男') AND cno=(select cno from course where cname='计算机导论')

函数练习

                                                SELECT * from student
SELECT ow_number() FROM student

条件语句练习

SELECT * FROM studentscores
发现这个语句的执行结果是错误的 GROUP BY 的语句是英文的就可以但是中文就不行
SELECT
       CASE SUBJECT
       WHEN '数学' THEN '常科'
       WHEN '语文' THEN '常科'
       WHEN '英语' THEN '常科'
       WHEN '生物' THEN '理科'
       ELSE '杂科'
       END AS sub,
       SUM(score) AS 成绩
FROM studentscores GROUP BY sub

行转列的两种实现技巧
SELECT pref,sum(case WHEN sex=1 THEN population END) as cnt_man,sum(case WHEN sex=2 THEN population END) as cnt_wm FROM sex GROUP BY pref
SELECT a.pref,a.population as man,b.population as woman from(SELECT * FROM sex WHERE sex=1) a ,(SELECT  * FROM sex WHERE sex=2) b WHERE a.pref=b.pref

SELECT std_id,max(club_id) as club_id  FROM studentclub GROUP BY std_id HAVING COUNT(club_id)=1
UNION ALL
SELECT std_id,club_id as club_id  FROM studentclub WHERE main_club_flg='Y'

                                                SELECT std_id,
                                                CASE
                                                       WHEN COUNT(*)=1 THEN MAX(club_id)
                                                       ELSE MAX(
                                                CASE WHEN main_club_flg='Y' THEN club_id
                                                END
                                                )
                                                       END AS club_id

                                                FROM studentclub GROUP BY std_id
----------------------------------------------------------连接的学习--------------------------------------------------------
排列
                                                SELECT a.name,b.name from products a,products b WHERE a.name<>b.name
                                                组合
                                                SELECT a.name,b.name from products a,products b WHERE a.name>b.name
                                                等价组合
                                                SELECT a.name,b.name ,a.price ,b.price from products a,products b WHERE a.name>b.name AND a.price=b.price
                                                SELECT DISTINCT a.name,a.price  from products a,products b WHERE a.name<>b.name AND a.price=b.price   (显示效果不好)
使用相关子查询实现等价组合（失败了）
                                                SELECT * from products outs WHERE name>= ALL(SELECT name FROM products inners WHERE outs.price=inners.price)

三组合
SELECT a.name,b.name,c.name from products a,products b,products c WHERE a.name>b.name AND b.name>c.name

SELECT * FROM products p1 WHERE EXISTS (SELECT * FROM products p2 WHERE p1.name=p2.name AND p1.price=p2.price)

根据单列过滤（假设其他列是字符串呢）
SELECT name,MAX(price) FROM products GROUP BY NAME
全行过滤
                                                SELECT DISTINCT name,price from products

-----------------------------------null------------------------------------------------------
涉及null 的谓词运算（true false 都会产生 unknow）
                                                SELECT * FROM class_a WHERE  not age in(SELECT age FROM class_b WHERE city='东京')
                                                SELECT * FROM class_a WHERE  age not  in(22,23,NULL)
                                                SELECT * FROM class_a WHERE  not (age=22 OR age=23 or age=NULL)
                                                SELECT * FROM class_a WHERE  (age!=22) AND (age!=23) AND (age!=NULL)
not in 遇上了null 就用 not EXISTS 代替
SELECT * from class_a a where not EXISTS (SELECT * from class_b b WHERE b.city='东京' AND a.age=b.age)
极值函数的优势
SELECT * from class_a WHERE  age <all(SELECT age from class_b b WHERE b.city='东京')
SELECT * from class_a WHERE  age <(SELECT MIN(age) from class_b b WHERE b.city='东京')
限定谓词的优势（极值不怕null 但是怕空集合 会返回null  消灭null 的同时可能创造null）
SELECT * from class_a WHERE  age <all(SELECT age from class_b b WHERE b.city='南京')
SELECT * from class_a WHERE  age <(SELECT MIN(age) from class_b b WHERE b.city='南京')

SELECT * FROM seqtbl DESC
---------------------------------HAVING-----------------------------------------------------
没有 GROUP BY 则having 将整个表当成集合
SELECT "存在缺失" as gap from seqtbl HAVING  COUNT(*)<>max(seq)-min(seq)+1

SELECT (seq+1) from seqtbl WHERE (seq+1) NOT in(SELECT seq from seqtbl)
众数：发现All 在某些场合可以比 极值函数少选择一次
                                                SELECT * FROM graduates GROUP BY income HAVING COUNT(*) >=(SELECT MAX(个数) from (SELECT COUNT(*) as 个数  FROM graduates GROUP BY income) a)
                                                SELECT income,COUNT(*) as 个数 FROM graduates GROUP BY income HAVING COUNT(*) >=ALL(SELECT COUNT(*) as 个数  FROM graduates GROUP BY income)
中位数:

SELECT * FROM graduates a,graduates b ORDER BY a.income

SELECT * from addresses
SELECT * from addresses limit 0,2  替代其他数据库的top

---------------------------------字符串操作-------------------------------------------------
select LEFT(name,2)from addresses
                                                select MID(name,2,2)from addresses
                                                SELECT RIGHT(name,2) from addresses
 ----------------------------------外链接----------------------------------------------------
交叉表(第一种的方法是不准确的，需要加统计函数)

SELECT NAME,
       CASE course WHEN 'UNIX基础' THEN 'O' ELSE "" END as 'UNIX基础',
       CASE course WHEN 'Java中级' THEN 'O' ELSE "" END as 'Java中级',
       CASE course WHEN 'SQL入门' THEN 'O' ELSE "" END as 'SQL入门'
from courses GROUP BY NAME

SELECT NAME,
       CASE WHEN SUM(CASE course WHEN 'UNIX基础' THEN 1 ELSE 0 END)=1 THEN "O" ELSE "" END as 'UNIX基础',
       CASE WHEN SUM(CASE course WHEN 'Java中级' THEN 1 ELSE 0 END)=1 THEN "O" ELSE "" END  as 'Java中级',
       CASE WHEN SUM(CASE course WHEN 'SQL入门' THEN 1 ELSE 0 END )=1 THEN "O" ELSE "" END as 'SQL入门'
from courses GROUP BY NAME

# 将变量传入了子查询
SELECT c0.NAME as tmp,
       (SELECT 'O' FROM courses c1 WHERE course='UNIX基础' AND c1.name= tmp) as 'UNIX基础',
       (SELECT 'O' FROM courses c2 WHERE course='Java中级' AND c2.name=tmp ) as 'Java中级',
       (SELECT 'O' FROM courses c3 WHERE course='SQL入门' AND c3.name=tmp) as 'SQL入门'
from (SELECT DISTINCT NAME from courses) c0


SELECT c0.name,
       CASE WHEN c1.name  is NOT NULL THEN "O" ELSE "" END as "UNIX基础" ,
       CASE WHEN c2.name  is NOT NULL THEN "O" ELSE "" END as "Java中级" ,
       CASE WHEN c3.name  is NOT NULL THEN "O" ELSE "" END as "SQL入门"
from
(SELECT DISTINCT NAME from courses) c0
  LEFT OUTER JOIN
  (SELECT name from courses where course='UNIX基础') c1 ON c0.name=c1.name
  LEFT OUTER JOIN
  (SELECT name from courses where course='Java中级') c2 ON c0.name=c2.name
  LEFT OUTER JOIN
  (SELECT name from courses where course='SQL入门') c3 ON c0.name=c3.name
  列到行的转换操作
  # 不成熟1
SELECT employee,child_1 as children from Personnel
UNION ALL
SELECT employee,child_2 as children from Personnel
UNION ALL
SELECT employee,child_3 as children from Personnel
ORDER BY  employee desc
# 不成熟2
                                                SELECT employee,child_1 as children from Personnel
                                                UNION
                                                SELECT employee,child_2 as children from Personnel
                                                UNION
                                                SELECT employee,child_3 as children from Personnel
                                                ORDER BY  employee desc

# 成熟 没有子女的员工要输出，有子女的员工不能有null 的行存在
create VIEW Children(child) AS
  SELECT child_1 as child from Personnel
  UNION
  SELECT child_2 as child from Personnel
  UNION
  SELECT child_3 as child from Personnel

SELECT p.employee,c.child from Personnel as p LEFT JOIN Children c ON c.child in(p.child_1,p.child_2,p.child_3)
  ----------行元素的使用
                                                SELECT * from Personnel WHERE (child_1,child_2)=("春子","夏子")
                                                SELECT * FROM class_a a INNER JOIN class_b b ON a.id=b.id
-- 异或集
SELECT COALESCE(a.id,b.id),COALESCE(a.name,b.name) FROM class_a a LEFT JOIN class_b b ON a.id=b.id WHERE b.id IS NULL
UNION
SELECT COALESCE(a.id,b.id),COALESCE(a.name,b.name) FROM class_a a RIGHT JOIN class_b b ON a.id=b.id WHERE a.id IS NULL

1.6
===============================================================================================================================================
-- 关联子查询   在很多时候都是性能比较差的
                                                SELECT a.year,a.sale-b.sale FROM sales a,sales b where a.year-1=b.year            --其实内涵一个bug 就是 inner join
                                                --性能比较差
                                                SELECT year y,sale-(select sale from sales where year=y-1)az from sales
SELECT a.year,a.sale - b.sale from sales a LEFT JOIN sales b on a.year-1=b.year
SELECT a.year,a.sale - b.sale from sales a LEFT JOIN sales b on a.year-1=b.year WHERE a.sale - b.sale=0
--这个性能也差
SELECT year,sale,
       case
	WHEN sale=0 THEN '->'
	WHEN sale>0 THEN '^'
	WHEN sale<0 THEN '^^'
	else '-'
END
FROM(
                                                             SELECT a.year,a.sale - b.sale as sale from sales a LEFT JOIN sales b on a.year-1=b.year
)b
-- 性能比较好的
SELECT a.year,a.sale,
       case
       WHEN a.sale=b.sale THEN '->'
       WHEN a.sale>b.sale THEN '^'
       WHEN a.sale<b.sale THEN '^^'
       else '-' -- 销售量缺失
       END
       from sales a LEFT JOIN sales b ON a.year-1=b.year
-- 和最近的时间比较 万一没有去年数据呢
                                                SELECT year y,sale-(select sale from sales where year=(select max(year) from sales where year<y))az from sales
-- 针对缺失数据  在这里领悟到了join 的真谛(过滤数据也是通过关联子查询完成的)
SELECT a.year,a.sale,
                                                case
                                                WHEN a.sale=b.sale THEN '->'
                                                WHEN a.sale>b.sale THEN '^'
                                                WHEN a.sale<b.sale THEN '^^'
                                                else '-' -- 销售量缺失
                                                END
from sales a LEFT JOIN sales b ON b.year=(SELECT max(year) from sales WHERE a.year>year)

-- 移动累计值和移动平均值
select year y,(SELECT SUM(sale) FROM sales WHERE year<y)  from sales
                                                SELECT a.year,a.sale,sum(b.sale) from sales a LEFT JOIN sales b ON a.year>b.year GROUP BY a.year

