package me.zhengjie.util;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


/**
 * @author ly
 */
@Slf4j
@Scope("prototype")
public class OBSUploadUtil {

    private static final  String accessKeyId ="C2E20CB51FA1D9E3FAB2";        //华为云的 Access Key Id

    private static final String accessKeySecret ="SVwdYazvzEflJOtUeSgzT6sKe1oAAAF0H6HZ47Ix";    //华为云的 Access Key Secret

     private static final String obsEndpoint ="https://103.36.173.40";    //格式如 obs.cn-north-1.myhuaweicloud.com

    private static final String bucketName ="wisdom-city";        //obs桶名

    /**
     * 上传文件
     *
     * @return
     */
    public static String upload(MultipartFile multipartFile) throws Exception {


        try {
            // 上传文件，注意：上传内容大小不能超过5GB
            String objectKey = System.currentTimeMillis() + getExtensionName(multipartFile.getOriginalFilename());
            InputStream inputStream = multipartFile.getInputStream();

            String replace = "";
            inputStream.close();
            return replace;
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {

        }
        return null;
    }


    /**
     * 删除文件
     *
     * @return
     */
    public static void deleteObj(String url) throws Exception {

        try {

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {

        }
    }

    public static String batchUpload(MultipartFile[] multipartFiles) throws Exception {
        StringBuilder photos = new StringBuilder();
        if(multipartFiles.length==1){
            return upload( multipartFiles[0]);
        }else {
            for (int i = 0; i < multipartFiles.length; i++) {
                String upload = upload(multipartFiles[i]);
                photos.append(upload);
                if (i !=multipartFiles.length-1){
                    photos.append(",");
                }
            }

            return photos.toString();
        }

    }

    /**
     * 从url中解析对象名
     *
     * @param url
     * @return
     */
    private static String getObjectNameByUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return url;
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 获取后缀名
     *
     * @param filename
     * @return
     */
    private static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot);
            }
        }
        return filename;
    }

}
