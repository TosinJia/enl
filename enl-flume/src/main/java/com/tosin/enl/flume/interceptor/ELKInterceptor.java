package com.tosin.enl.flume.interceptor;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.List;

public class ELKInterceptor implements Interceptor {
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
        return null;
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        return null;
    }

    @Override
    public void close() {

    }
}
