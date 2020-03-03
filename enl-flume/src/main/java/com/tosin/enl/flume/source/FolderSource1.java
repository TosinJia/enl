package com.tosin.enl.flume.source;

import org.apache.commons.io.FileUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件夹source
 * 参考MySource
 *
 */
public class FolderSource1 extends AbstractSource implements Configurable, PollableSource {
    private static Logger LOG = Logger.getLogger(FolderSource1.class);

    @Override
    public void configure(Context context) {
        //提取配置
    }

    @Override
    public Status process() throws EventDeliveryException {
        //编写所有业务逻辑
        Status status = Status.BACKOFF;
        //TODO ■ 实时解析处理传送过来的FTP格式WIFI数据(txt)
        String pathname = "/usr/chl/dir";
        Collection<File> files = null;
        try {
            files = FileUtils.listFiles(new File(pathname), new String[]{"txt"}, true);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String yearMonthDayInfo = sdf.format(new Date());
            System.out.println(yearMonthDayInfo);
            //TODO ■ FTP文件备份(数据备份，数据重跑)  最开始的源头是wifi设备。
            for(File file :files){
                String filename = file.getName();
                //定义备份根目录
                String successDirPathNme = "/usr/chl/succ";
                //我们进行备份的时候最好有一个索引，按日期建立目录
                String destDirPathName = successDirPathNme + File.separator+yearMonthDayInfo;
                String destFilePathName = destDirPathName + File.separator  + filename;
                System.out.println("最终文件备份 " + destFilePathName);
                try {
                    if(!new File(destFilePathName).exists()){
                        //如果不存在，说明此文件没有被处理过
                        //1.先读取文件内容（是要发送到后面的channel中）
                        System.out.println(file.getAbsolutePath());
                        List<String> infoList = FileUtils.readLines(file);
                        infoList.forEach(line -> {
                            // flume进行数据传输的时候 都是封装为event进行传送的
                            Event e = new SimpleEvent();
                            e.setBody(line.getBytes());
                            //设置消息头。可以设置一些说明，参数之类
                            //可以把当前文件和 移动之后存放的地址传送到channel
                            Map headersMap = new HashMap();
                            headersMap.put("file", filename);
                            headersMap.put("absolute_filename", destFilePathName);
                            e.setHeaders(headersMap);
                            getChannelProcessor().processEvent(e);

                            System.out.println("数据内容："+line);
                        });
                        //2. 进行备份
                        FileUtils.moveToDirectory(file, new File(destDirPathName), true);
//                        FileUtils.copyFileToDirectory(file, new File(destDirPathName), true);
                        status = Status.READY;
                    }else{
                        //目标文件已经存在，无需处理
                    }
                } catch (IOException e) {
//                    e.printStackTrace();
                    System.out.println("异常："+e.getMessage());
                    status = Status.BACKOFF;
                }

                //TODO ■ FTP异常文件备份(查错和重跑使用)
                System.out.println(file.getName());
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("外层异常："+e.getMessage());
            status = Status.BACKOFF;
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
