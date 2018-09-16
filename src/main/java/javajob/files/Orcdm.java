package javajob.files;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.io.orc.OrcFile;
import org.apache.hadoop.hive.ql.io.orc.OrcInputFormat;
import org.apache.hadoop.hive.ql.io.orc.OrcSerde;
import org.apache.hadoop.hive.ql.io.orc.Reader;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Copyright (C)
 *
 * @program: workjob
 * @description: java 读取 Orc格式的文件（HIVE）
 * @author: 刘文强  kingcall
 * @create: 2018-04-16 15:53
 **/
public class Orcdm {
    public static void main(String[] args) throws SerDeException, IOException {

    }

}
