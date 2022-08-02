package me.zhengjie.util;

import me.zhengjie.utils.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadFile {
    /**
     * 将文件存储在 nginx代理路劲下
     * @param multipartFile 文件
     * @return
     */
    public static String uploadFile(MultipartFile multipartFile){
        String path = "E:\\static";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String DataStr = format.format(new Date());
        path = path + "/uploadfile/tempfile/" + DataStr + "/";
        File upload = FileUtil.upload(multipartFile, path);
        String name = upload.getName();
        return DataStr+"/"+name;
    }
}
