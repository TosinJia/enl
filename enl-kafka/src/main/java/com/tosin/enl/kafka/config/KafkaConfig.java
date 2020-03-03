package com.tosin.enl.kafka.config;

import com.tosin.enl.common.config.ConfigUtil;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * 单例模式
 */
public class KafkaConfig {
    private static Logger LOG = Logger.getLogger(KafkaConfig.class);

    //定义一个kafka配置路径
    private static final String KAFKA_CONFIG_PATH = "kafka/kafka-server-config.properties";
    //kfaka的配置文件
    private Properties properties;
    private ProducerConfig producerConfig;
    //定义一个私有的静态变量
    private static volatile KafkaConfig kafkaConfig;
    //私有构造方法
    private KafkaConfig(){
        //读取配置文件
        LOG.info("开始实例化ProducerConfig");
        properties = ConfigUtil.getInstance().getProperties(KAFKA_CONFIG_PATH);
        producerConfig = new ProducerConfig(properties);
        LOG.info("实例化ProducerConfig成功");
    }
    //对外获取对象的一个公共方法
    public static KafkaConfig getInstance(){
        if(kafkaConfig == null){
            synchronized (KafkaConfig.class){
                kafkaConfig = new KafkaConfig();
            }
        }
        return kafkaConfig;
    }

    public ProducerConfig getProducerConfig() {
        return producerConfig;
    }

    public Properties getProperties() {
        return properties;
    }
}
