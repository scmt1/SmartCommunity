package me.zhengjie.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadFile {
    public static String basePath = "E:/static/uploadfile/tempfile/";

    public static String deletePath = "E:/static/uploadfile";
    /**
     * 将文件存储在 nginx代理路劲下
     * @param multipartFile 文件
     * @return
     */
    public static String uploadFile(MultipartFile multipartFile){
        String path = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String DataStr = format.format(new Date());
        path = basePath + "" + DataStr + "/";
        File upload = FileUtil.upload(multipartFile, path);
        String name = upload.getName();
        return "/tempfile/"+ DataStr+"/"+name;
    }

    /**
     * 将文件存储在
     * @param multipartFile
     * @return
     */
    public static String batchUpload(MultipartFile[] multipartFile){

        String res="";
        for(MultipartFile file:multipartFile){
            String path = "";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String DataStr = format.format(new Date());
            path = basePath + "" + DataStr + "/";
            File upload = FileUtil.upload(file, path);
            String name = upload.getName();
            res +="/tempfile/"+ DataStr+"/"+name+",";
        }

        return  res;

    }

    /**
     * 删除nginx代理路劲下的文件
     * @param path
     */
    public static void  deleteFile(String path){
        String resPath = deletePath+path;
        File delete = new File(resPath);
        if(delete.exists()){
            delete.delete();
        }
    }

}
