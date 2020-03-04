package com.tosin.enl.flume.source;

import com.tosin.enl.flume.constant.ConstantFields;
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
public class FolderSource extends AbstractSource implements Configurable, PollableSource {
    private static Logger LOG = Logger.getLogger(FolderSource.class);

    String folderDir;   //文件监控目录
    String succDir;     //文件处理成功存放的根目录
    private int fileNum;    //每批最多处理多少文件
    List<Event> events; //用于批量处理事件

    @Override
    public void configure(Context context) {
        //提取配置
        LOG.info("提取配置文件...");
    }

    @Override
    public Status process() throws EventDeliveryException {
        //编写所有业务逻辑
        Status status = null;

        //休眠，方便查看日志
        try {
            LOG.info("休眠3秒");
            Thread.currentThread().sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //TODO ■ 实时解析处理传送过来的FTP格式WIFI数据(txt)
        String folderDir = "/usr/chl/dir";
        Collection<File> files = null;
        try {
            files = FileUtils.listFiles(new File(folderDir), new String[]{"txt"}, true);
            //当文件数量特别多时可能会导致异常，需要分批处理


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String yearMonthDayInfo = sdf.format(new Date());
            LOG.info(yearMonthDayInfo);
            //TODO ■ FTP文件备份(数据备份，数据重跑)  最开始的源头是wifi设备。
            for(File file :files){
                String filename = file.getName();
                //定义备份根目录
                String succDir = "/usr/chl/succ";
                //我们进行备份的时候最好有一个索引，按日期建立目录
                String destDirPathName = succDir + File.separator+yearMonthDayInfo;
                String destFilePathName = destDirPathName + File.separator  + filename;
                LOG.info("最终文件备份 " + destFilePathName);
                try {
                    if(!new File(destFilePathName).exists()){
                        //如果不存在，说明此文件没有被处理过
                        //1.先读取文件内容（是要发送到后面的channel中）
                        LOG.info(file.getAbsolutePath());
                        List<String> infoList = FileUtils.readLines(file);
                        infoList.forEach(line -> {
                            // flume进行数据传输的时候 都是封装为event进行传送的
                            Event e = new SimpleEvent();
                            e.setBody(line.getBytes());
                            //设置消息头。可以设置一些说明，参数之类
                            //可以把当前文件和 移动之后存放的地址传送到channel
                            Map headersMap = new HashMap();
                            headersMap.put(ConstantFields.FILE_NAME, filename);
                            headersMap.put(ConstantFields.ABSOLUTE_PATH, destFilePathName);
                            e.setHeaders(headersMap);
                            getChannelProcessor().processEvent(e);

                            LOG.info("数据内容："+line);
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
                    LOG.error("异常："+e.getMessage());
                    status = Status.BACKOFF;
                }

                //TODO ■ FTP异常文件备份(查错和重跑使用)
                LOG.info(file.getName());
            }
        } catch (Exception e) {
//            e.printStackTrace();
            LOG.error("外层异常："+e.getMessage());
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
