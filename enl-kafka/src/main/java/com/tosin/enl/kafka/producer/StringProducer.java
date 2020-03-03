package com.tosin.enl.kafka.producer;


import com.tosin.enl.kafka.config.KafkaConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

import java.util.Properties;

public class StringProducer {
    private static Logger LOG = Logger.getLogger(StringProducer.class);

    //定义一个发送数据方法
    public static void producer(String topic, String line){
        try {
            //构造一个消息
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, line);
            //构造一个生产者
            //需要一个ProducerConfig
            Properties kafkaProperties = KafkaConfig.getInstance().getProperties();
            KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(kafkaProperties);
            kafkaProducer.send(producerRecord);
            LOG.info("向kafka["+topic+"]成功发送数据！");
            kafkaProducer.close();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("向kafka["+topic+"]发送数据失败！");
        }
    }

    public static void main(String[] args) {
        producer("test1", "1line");
    }
}
