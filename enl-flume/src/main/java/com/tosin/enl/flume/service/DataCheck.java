package com.tosin.enl.flume.service;

import com.tosin.enl.common.config.ConfigUtil;
import com.tosin.enl.flume.constant.ConstantFields;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DataCheck {
    private static Logger LOG = Logger.getLogger(DataCheck.class);

    private static Properties properties;
    static{
        String path = "common/datatype.properties";
        properties = ConfigUtil.getInstance().getProperties(path);
    }

    /**
     * 数据解析并且校验
     */
    public static Map<String, String> txtParseAndValidation(String fileName, String absolutePath, String line){
        //存放解析数据的map
        Map<String, String> map = new HashMap<>();
        //异常数据需要进行存储，查询，分析   用于异常数据监控，提升数据质量
        Map<String, String> errorMap = new HashMap<>();

        //数据清洗，转换，加工
        //定义一个数据字典
        //imei,imsi,longitude,latitude,phone_mac,device_mac,device_number,collect_time,username,phone,object_username,send_message,accept_message,message_time
        //000000000000000,000000000000000,24.000000,25.000000,aa-aa-aa-aa-aa-aa,bb-bb-bb-bb-bb-bb	32109231	1257305985	andiy	18609765435	judy			1789098763
        //获取数据类型
        String dataType = fileName.split("_")[0].toLowerCase();
        //根据数据类型获取 数据字典数组
        String[] fields = properties.getProperty(dataType).split(",");
        String[] datas = line.split("\t");

        if(fields.length == datas.length){
            //字段和值映射
            for(int i=0; i<fields.length; i++){
                map.put(fields[i], datas[i]);
            }
            //数据加工  主要是为了满足后续的业务需求
            //这个数据最终要进入ES，需求，要根据表来进行查询，要查询文件最终存放的目录。
            //文件名，文件绝对路径会丢失，信息丢失掉了
            map.put("table", dataType);
            map.put("rksj", String.valueOf(System.currentTimeMillis()/1000));
            map.put(ConstantFields.FILE_NAME, fileName);
            map.put(ConstantFields.ABSOLUTE_PATH, absolutePath);
        }else{
            errorMap.put("length_error", "字段和数据的个数不匹配");
            errorMap.put("length", "字段长度"+fields.length+"，数据长度"+datas.length);
        }
        //如果数据有错误，那么data中数据有问题，将data置为空，不推送到kafka
        if(errorMap.size()>0){
            map = null;
        }
        return map;
    }
}
