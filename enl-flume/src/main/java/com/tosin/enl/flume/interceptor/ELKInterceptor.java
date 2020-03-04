package com.tosin.enl.flume.interceptor;

import com.tosin.enl.flume.constant.ConstantFields;
import com.tosin.enl.flume.service.DataCheck;
import org.apache.commons.io.Charsets;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.interceptor.Interceptor;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ELKInterceptor implements Interceptor {
    private static final Logger LOG = Logger.getLogger(ELKInterceptor.class);

    public static class Builder implements Interceptor.Builder{

        @Override
        public Interceptor build() {
            return new ELKInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }

    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        if(event == null){
            return null;
        }
        Event eventRet = new SimpleEvent();
        //获取并解析event中的信息  header  body，对应FolderSource中的处理
        Map<String, String> headers = event.getHeaders();
        String fileName = headers.get(ConstantFields.FILE_NAME);
        String absolutePath = headers.get(ConstantFields.ABSOLUTE_PATH);
        String line = new String(event.getBody(), Charsets.UTF_8);
        //TODO 进行数据清洗
        Map<String, String> map = DataCheck.txtParseAndValidation(fileName, absolutePath, line);

        LOG.info("拦截器执行--->"+line);
        eventRet.setBody(line.getBytes());
        return eventRet;
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        List<Event> eventList = new ArrayList<>();
        list.forEach(event -> {
            Event e = intercept(event);
            if(e != null){
                eventList.add(e);
            }
        });
        return eventList;
    }

    @Override
    public void close() {

    }
}
