package com.tosin.enl.flume.source;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;

/**
 * http://flume.apache.org/releases/content/1.9.0/FlumeDeveloperGuide.html#source
 *
 */
public class MySource extends AbstractSource implements Configurable, PollableSource {
    private String myProp;

    /**
     * 读取flume配置文件 flume.conf
     */
    @Override
    public void configure(Context context) {
        String myProp = context.getString("myProp", "defaultValue");

        // Process the myProp value (e.g. validation, convert to another type, ...)

        // Store myProp for later retrieval by process() method
        this.myProp = myProp;
    }

    @Override
    public void start() {
        // Initialize the connection to the external client
    }

    @Override
    public void stop () {
        // Disconnect from external client and do any additional cleanup
        // (e.g. releasing resources or nulling-out field values) ..
    }

    /**
     * 轮询调度
     */
    @Override
    public Status process() throws EventDeliveryException {
        //编写所有业务逻辑
        //TODO ■ 实时解析处理传送过来的FTP格式WIFI数据(txt)
        //TODO ■ FTP文件备份(数据备份，数据重跑)  最开始的源头是wifi设备。
        //TODO ■ FTP异常文件备份(查错和重跑使用)

        Status status = null;

        try {
            // This try clause includes whatever Channel/Event operations you want to do

            // Receive new data
//            Event e = getSomeData();
            Event e = new SimpleEvent();

            // Store the Event into this Source's associated Channel(s)
            e.setBody("MySource".getBytes());
            getChannelProcessor().processEvent(e);

            status = Status.READY;
        } catch (Throwable t) {
            // Log exception, handle individual exceptions as needed

            status = Status.BACKOFF;

            // re-throw all Errors
            if (t instanceof Error) {
                throw (Error)t;
            }
        } finally {
//            txn.close();
        }
        return status;
    }

    @Override
    public long getBackOffSleepIncrement() {
        return 0;
    }

    @Override
    public long getMaxBackOffSleepInterval() {
        return 0;
    }
}