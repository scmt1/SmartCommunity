package me.zhengjie.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.common.utils.ResultUtil;

import me.zhengjie.entity.BasicFile;

import me.zhengjie.service.IBasicFileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

//import  me.zhengjie.service.LogService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;


/**
 * basicFile数据接口
 * @author
 **/
@RestController
@RequestMapping("/api/basicFile")
@AllArgsConstructor
public class BasicFileController {

    private final IBasicFileService basicFileService;
//    @Autowired
//    private LogService logService;
//
//    @Autowired
//    private SecurityUtil securityUtil;

    /**
     * 功能描述：新增basicFile数据
     *
     * @param basicFile 实体
     * @return 返回新增结果
     */
//    @Log("新增basicFile数据")

    /**
     * 新增basicFile数据
     * @param basicFile
     * @return
     */
    @PostMapping("addBasicFile")
    @ResponseBody
    public Result<Object> addBasicFile(@RequestBody BasicFile basicFile){
        //MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(basicFile.getFile().toString());
        //文件存储在nginx代理路径下
        //String s = UploadFile.uploadFile(imgFile);
        //String substring = s.substring(0, s.lastIndexOf("."));

        //BigDecimal b = new BigDecimal(imgFile.getSize() / 1024);
        //double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //basicFile.setFileSize(f1);
        String suffix = basicFile.getFileName().substring(basicFile.getFileName().lastIndexOf(".") + 1);
        basicFile.setFileType(suffix);

        //substring = substring + '.' + suffix;

        //basicFile.setFilePath(substring);
        Long time = System.currentTimeMillis();
        try {
            basicFile.setIsDelete(0);
//            basicFile.setCreateId(securityUtil.getCurrUser().getId().toString());
            basicFile.setCreateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = basicFileService.save(basicFile);
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
//            logService.addErrorLog("保存异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据主键来删除数据
     *
     * @param map 主键集合
     * @return 返回删除结果
     */
//    @Log("根据主键来删除basicFile数据")

    /**
     * 根据主键来删除basicFile数据
     * @param map
     * @return
     */
    @PostMapping("deleteBasicFile")
    public Result<Object> deleteBasicFile(@RequestBody Map<String ,Object> map) {
        if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            List<String> ids = (List<String>) map.get("ids");
            if (ids == null || ids.size() == 0 ) {
                return ResultUtil.error("参数为空，请联系管理员！！");
            }
            boolean res = basicFileService.removeByIds(ids);
            if (res) {
                return ResultUtil.data(res, "删除成功");
            } else {
                return ResultUtil.error( "删除失败");
            }
        } catch (Exception e) {
//            logService.addErrorLog("删除异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("删除异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据主键来获取数据
     *
     * @param id 主键
     * @return 返回获取结果
     */
//    @Log("根据主键来获取basicFile数据")

    /**
     * 根据主键来获取basicFile数据
     * @param id
     * @return
     */
    @GetMapping("getBasicFile")
    public Result<Object> getBasicFile(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            BasicFile res = basicFileService.getById(id);
            if (res != null) {
                return ResultUtil.data(res, "查询成功");
            } else {
                return ResultUtil.error("查询失败");
            }
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：实现分页查询
     *
     * @param searchVo 查询参数
     * @param pageVo   分页参数
     * @return 返回获取结果
     */
//    @Log("分页查询basicFile数据")

    /**
     * 分页查询basicFile数据
     * @param basicFile
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryBasicFileList")
    public Result<Object> queryBasicFileList(BasicFile basicFile, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            return basicFileService.queryBasicFileListByPage(basicFile, searchVo, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：更新数据
     *
     * @param basicFile 实体
     * @return 返回更新结果
     */
//    @Log("更新basicFile数据")

    /**
     * 更新basicFile数据
     * @param basicFile
     * @return
     */
    @PostMapping("updateBasicFile")
    public Result<Object> updateBasicFile(@RequestBody BasicFile basicFile) {
        if (StringUtils.isBlank(basicFile.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        if(basicFile.getFile() != null && StringUtils.isNotBlank(basicFile.getFile().toString())) {
            //MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(basicFile.getFile().toString());
            //文件存储在nginx代理路径下
            //String s = UploadFile.uploadFile(imgFile);
            //String substring = s.substring(0, s.lastIndexOf("."));
           // BigDecimal b = new BigDecimal(imgFile.getSize() / 1024);
            //double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //basicFile.setFileSize(f1);
            String suffix = basicFile.getFileName().substring(basicFile.getFileName().lastIndexOf(".") + 1);
            basicFile.setFileType(suffix);
            //substring = substring + '.' + suffix;
            //basicFile.setFilePath(substring);
        }
        try {
            basicFile.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = basicFileService.updateById(basicFile);
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
//            logService.addErrorLog("保存异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：导出数据
     *
     * @param response  请求参数
     * @param basicFile 查询参数
     * @return
     */
//    @Log("导出basicFile数据")

    /**
     * 导出basicFile数据
     * @param response
     * @param basicFile
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, BasicFile basicFile) {
        Long time = System.currentTimeMillis();
        try {
            basicFileService.download(basicFile, response);
        } catch (Exception e) {
//            logService.addErrorLog("导出异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }


    /**
     * 功能描述：查询所有数据
     *
     * @return 返回获取结果
     */
//    @Log("查询所有数据")

    /**
     * 查询所有数据
     * @return
     */
    @GetMapping("/queryAllBasicFileList")
    public Result<Object> queryAllBasicFileList() {
        Long time = System.currentTimeMillis();
        try {
            List<BasicFile> list = basicFileService.list();
            return ResultUtil.data(list);
        } catch (Exception e) {
//            logService.addErrorLog("查询所有数据异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询所有数据异常:" + e.getMessage());
        }
    }

}
