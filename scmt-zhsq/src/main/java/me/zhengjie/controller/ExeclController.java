package me.zhengjie.controller;


import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 小区信息数据接口
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/excel")
public class ExeclController {
    /**
     * 模板下载
     * @param
     * @author kaima2
     */
    @RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
    @ResponseBody
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response,String type) throws IOException {
        InputStream is = null;
        String fileName = "";
        if(StringUtils.isNotBlank(type)){
            switch (type){
                case "1":{
                    is= this.getClass().getResourceAsStream("/excelmodel/人口管理.xls");
                    fileName = "人口管理.xls";
                    break;
                }
                case "2":{
                    is= this.getClass().getResourceAsStream("/excelmodel/小区档案.xls");
                    fileName = "小区档案.xls";
                    break;
                }
                case "3":{
                    is= this.getClass().getResourceAsStream("/excelmodel/物业档案.xls");
                    fileName = "物业档案.xls";
                    break;
                }
                case "4":{
                    is= this.getClass().getResourceAsStream("/excelmodel/网格档案.xls");
                    fileName = "网格档案.xls";
                    break;
                }
                case "5":{
                    is= this.getClass().getResourceAsStream("/excelmodel/楼栋档案.xls");
                    fileName = "楼栋档案.xls";
                    break;
                }
                case "6":{
                    is= this.getClass().getResourceAsStream("/excelmodel/网格员.xls");
                    fileName = "网格员.xls";
                    break;
                }
                case "7":{
                    is= this.getClass().getResourceAsStream("/excelmodel/网格长.xls");
                    fileName = "网格长.xls";
                    break;
                }
                case "8":{
                    is= this.getClass().getResourceAsStream("/excelmodel/社区干部.xls");
                    fileName = "社区干部.xls";
                    break;
                }
                case "9":{
                    is= this.getClass().getResourceAsStream("/excelmodel/街道志愿者.xls");
                    fileName = "街道志愿者.xls";
                    break;
                }
                case "10":{
                    is= this.getClass().getResourceAsStream("/excelmodel/房屋档案.xls");
                    fileName = "房屋档案.xls";
                    break;
                }
                default:{
                    is= this.getClass().getResourceAsStream("/excelmodel/人口管理.xls");
                    fileName = "人口管理.xls";
                    break;
                }
            }


            if(is!=null){
                byte[] fileData = input2byte(is);
                downloadFile(response, request, fileName, fileData);
            }

        }

    }


    /**
     * inputstream转Byte[]
     * @param inStream
     * @return
     * @throws IOException
     */
    private  byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    /**
     * 下载
     * @param response
     * @param request
     * @param filename
     * @param fileData
     * @return
     */
    private boolean downloadFile(HttpServletResponse response,
                                 HttpServletRequest request, String filename, byte[] fileData) {
        try {
            OutputStream myout = null;
            // 检查时候获取到数据
            if (fileData == null) {
                response.sendError(HttpStatus.HTTP_NO_CONTENT);
                return false;
            }
            try {
                if(request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                    filename = new String(filename.getBytes("GBK"),"iso-8859-1");
                }else{
                    filename = URLEncoder.encode(filename, "utf-8");
                }
                response.setHeader("content-Type", "application/ms-excel");
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
                response.setHeader("Content-disposition", "attachment;filename="
                        + filename + ";" + "filename*=utf-8''" + filename);
                // 写明要下载的文件的大小
                response.setContentLength(fileData.length);
                // 从response对象中得到输出流,准备下载
                myout = response.getOutputStream();
                myout.write(fileData);
                myout.flush();
            } catch (Exception e) {
            } finally {
                if (myout != null) {
                    try {
                        myout.close();
                    } catch (Exception e) {
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
