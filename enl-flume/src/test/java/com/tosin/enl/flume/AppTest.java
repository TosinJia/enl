package com.tosin.enl.flume;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    /**
     * com.tosin.enl.flume.source.FolderSource Test
     */
    @Test
    public void folderSourceTest(){

        //TODO ■ 实时解析处理传送过来的FTP格式WIFI数据(txt)
        String pathname = "E:\\BaiduNetdiskDownload\\1-企业网络日志\\测试数据1";
        Collection<File> files = null;
        try {
            files = FileUtils.listFiles(new File(pathname), new String[]{"txt"}, true);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String yearMonthDayInfo = sdf.format(new Date());
            System.out.println(yearMonthDayInfo);
            for(File file :files){
                //TODO ■ FTP文件备份(数据备份，数据重跑)  最开始的源头是wifi设备。
                String filename = file.getName();
                //定义备份根目录
                String successDirPathNme = "E:\\BaiduNetdiskDownload\\1-企业网络日志\\测试成功数据1";
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
                            System.out.println(line);
                        });
                        //2. 进行备份
                        FileUtils.moveToDirectory(file, new File(destDirPathName), true);
//                        FileUtils.copyFileToDirectory(file, new File(destDirPathName), true);
                    }else{
                        //目标文件已经存在，无需处理
                    }
                } catch (IOException e) {
//                    e.printStackTrace();
                    System.out.println("异常："+e.getMessage());
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("外层异常："+e.getMessage());
        }
    }

}
