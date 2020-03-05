package com.tosin.enl.flume.sink;

import com.tosin.enl.kafka.producer.StringProducer;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.apache.log4j.Logger;

/**
 * MySink1
 *
 */
public class FolderSink extends AbstractSink implements Configurable {
    private static final Logger LOG = Logger.getLogger(FolderSink.class);

    private String topic;

    @Override
    public void configure(Context context) {
        topic = context.getString("topic");
    }

    @Override
    public void start() {
        // Initialize the connection to the external repository (e.g. HDFS) that
        // this Sink will forward Events to ..
    }

    @Override
    public void stop () {
        // Disconnect from the external respository and do any
        // additional cleanup (e.g. releasing resources or nulling-out
        // field values) ..
    }

    @Override
    public Status process() throws EventDeliveryException {
        Status status = null;

        // Start transaction
        Channel ch = getChannel();
        Transaction txn = ch.getTransaction();
        txn.begin();
        try {
            // This try clause includes whatever Channel operations you want to do

            Event event = ch.take();

            String line = new String(event.getBody());
            LOG.info("MySink->"+line);
            //数据已经拿到，直接往kafka 里面写入就可以了，缺一个写kakfa的API
            StringProducer.producer(topic, line);

            // Send the Event to the external repository.
            // storeSomeData(e);

            txn.commit();
            status = Status.READY;
        } catch (Throwable t) {
            txn.rollback();

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
}
