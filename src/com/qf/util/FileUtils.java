package com.qf.util;

import java.io.File;
import java.util.UUID;

/**
 * 作者：SmallWood
 * 时间：2020/12/25 15:58
 */
public class FileUtils {

    public static String getNewFileName(String fileName){
        //1、获取UUID
        String uuid = UUID.randomUUID().toString();
        //2、去掉uuid中的特殊字符-
        uuid = uuid.replace("-","");
        //3、取出原文件名的后缀
        String fileExt = fileName.substring(fileName.lastIndexOf("."));
        //4、返回最终的文件名
        return uuid+fileExt;
    }

    public static String getNewFilePath(String filePath,String fileName){
        int hashCode = fileName.hashCode();
        //得到2级目录
        int path2 = hashCode & 15;
        //得到3级目录
        int path3 = (hashCode>>4) & 15;
        //得到最终目录
        filePath = filePath + "\\" + path2 + "\\" + path3;
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }
        return filePath;
    }

    public static String getSavePath(String filePath,String fileName){
        //1、获取这个路径中upload以后的路径
        filePath = filePath.substring(filePath.lastIndexOf("\\upload"));
        //2、获取最终的路径
        return filePath +"\\"+ fileName;
    }


}
