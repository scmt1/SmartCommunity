package me.zhengjie.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

//import  me.zhengjie.aop.log.Log;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicHealthFile;
import me.zhengjie.service.IBasicHealthFileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

//import  me.zhengjie.service.LogService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * basic数据接口
 * @author
 **/
@RestController

@RequestMapping("/api/basicHealthFile")
@AllArgsConstructor
public class BasicHealthFileController {

    private final IBasicHealthFileService basicHealthFileService;

    /**
     * 新增老人健康档案数据
     * @param basicHealthFile
     * @return
     */
    @PostMapping("addBasicHealthFile")
    public Result<Object> addBasicHealthFile(@RequestBody BasicHealthFile basicHealthFile) {
        Long time = System.currentTimeMillis();
        try {
            basicHealthFile.setIsDelete(0);
            basicHealthFile.setCreateTime(new Timestamp(System.currentTimeMillis()));

//            if (StringUtils.isNotBlank(basicHealthFile.getMedicalReport())) {
//                //base64 转文件
//                MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(basicHealthFile.getMedicalReport());
//                //文件存储在nginx代理路径下
//                String fileName = UploadFile.uploadFile(imgFile);
//                basicHealthFile.setMedicalReport(fileName);
//            }
            boolean res = basicHealthFileService.save(basicHealthFile);
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
            //logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 更新老人健康档案数据
     * @param basicHealthFile
     * @return
     */
    @PostMapping("updateBasicHealthFile")
    public Result<Object> updateBasicHealthFile(BasicHealthFile basicHealthFile) {
        if (StringUtils.isBlank(basicHealthFile.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            basicHealthFile.setUpdateTime(new Timestamp(System.currentTimeMillis()));
//            if (StringUtils.isNotBlank(basicHealthFile.getMedicalReport()) && basicHealthFile.getIsUpdateMedicalReport() == 1) {
//                //base64 转文件
//                MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(basicHealthFile.getMedicalReport());
//                //文件存储在nginx代理路径下
//                String fileName = UploadFile.uploadFile(imgFile);
//                basicHealthFile.setMedicalReport(fileName);
//            }

            boolean res = basicHealthFileService.updateById(basicHealthFile);
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
            //logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 根据主键来删除basic数据
     * @param map
     * @return
     */
    @PostMapping("deleteBasicHealthFile")
    public Result<Object> deleteBasicHealthFile(@RequestBody Map<String ,Object> map) {
        if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            List<String> ids = (List<String>) map.get("ids");
            if (ids == null || ids.size() == 0 ) {
                return ResultUtil.error("参数为空，请联系管理员！！");
            }
            boolean res = basicHealthFileService.removeByIds(ids);
            if (res) {
                return ResultUtil.data(res, "删除成功");
            } else {
                return ResultUtil.error( "删除失败");
            }
        } catch (Exception e) {
//            logService.addErrorLog("删除异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("删除异常:" + e.getMessage());
        }
    }

    /**
     * 根据主键来获取basic数据
     * @param id
     * @return
     */
    @GetMapping("getBasicHealthFile")
    public Result<Object> getBasicHealthFile(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            BasicHealthFile res = basicHealthFileService.getById(id);
            if (res != null) {
                return ResultUtil.data(res, "查询成功");
            } else {
                return ResultUtil.error("查询失败");
            }
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 分页查询basic数据
     * @param basicHealthFile
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryBasicHealthFileList")
    public Result<Object> queryBasicHealthFileList(BasicHealthFile basicHealthFile, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            return basicHealthFileService.queryBasicHealthFileListByPage(basicHealthFile, searchVo, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 导出basic数据
     * @param response
     * @param basicHealthFile
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, BasicHealthFile basicHealthFile) {
        Long time = System.currentTimeMillis();
        try {
            basicHealthFileService.download(basicHealthFile, response);
        } catch (Exception e) {
//            logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
        }
    }

    /**
     * 根据personId查询最新的健康档案
     * @param personId
     * @return
     */
    @GetMapping("getBasicHealthFileByPersonId")
    public Result<Object> getBasicHealthFileByPersonId(@RequestParam(name = "personId") String personId) {
        if (StringUtils.isBlank(personId)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            BasicHealthFile res = basicHealthFileService.getTopOne(personId);
            if (res != null) {
                return ResultUtil.data(res, "查询成功");
            } else {
                return ResultUtil.error("查询失败");
            }
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }
}
