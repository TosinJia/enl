package com.tosin.enl.common.config;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件读取工具类
 * 单例模式
 */
public class ConfigUtil {
    private static Logger LOG = Logger.getLogger(ConfigUtil.class);

    //单例模式
    //定义一个私有volatile 静态变量
    private static volatile ConfigUtil configUtil;
    //提供一个私有构造方法
    private ConfigUtil(){}
    //提供一个公用的静态方法获取
    public static ConfigUtil getInstance(){
        //双重否定
        if(configUtil == null){
            synchronized (ConfigUtil.class){
                if(configUtil == null){
                    configUtil = new ConfigUtil();
                }
            }
        }
        return configUtil;
    }

    public Properties getProperties(String path){
        Properties properties = new Properties();
        try {
            LOG.info("开始加载配置文件[" +path+"]");
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
            properties.load(inputStream);
            LOG.info("成功加载配置文件[" +path+"].");
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("加载配置文件[" +path+"]失败");
        }
        return properties;
    }

    public static void main(String[] args) {
        String path = "kafka/kafka-server-config.properties";
        Properties properties = ConfigUtil.getInstance().getProperties(path);
        properties.keySet().forEach(key -> {
            LOG.info(key+"--->"+properties.getProperty(key.toString()));
            System.out.println(key+"--->"+properties.getProperty(key.toString()));
        });
    }
}
