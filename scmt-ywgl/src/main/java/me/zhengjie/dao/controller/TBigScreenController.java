package me.zhengjie.dao.controller;

import java.util.Arrays;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletResponse;

import me.zhengjie.utils.BASE64DecodedMultipartFile;
import me.zhengjie.utils.UploadFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.aop.log.Log;
import me.zhengjie.dao.entity.TBigScreen;
import me.zhengjie.dao.service.ITBigScreenService;

import me.zhengjie.service.LogService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author
 **/
@RestController
@Api(tags = " 大屏数据接口")
@RequestMapping("/api/tBigScreen")
public class TBigScreenController {
    @Autowired
    private ITBigScreenService tBigScreenService;
    @Autowired
    private LogService logService;

    /**
     * 功能描述：新增大屏数据
     *
     * @param tBigScreen 实体
     * @return 返回新增结果
     */
    @Log("新增大屏数据")
    @ApiOperation("新增大屏数据")
    @PostMapping("addTBigScreen")
    public Result<Object> addTBigScreen(@RequestBody TBigScreen tBigScreen) {
        Long time = System.currentTimeMillis();
        try {
            tBigScreen.setIsDelete(0);
            tBigScreen.setCreateTime(new Timestamp(System.currentTimeMillis()));
            if (tBigScreen.getImg() != null) {//图片保存
                //base64 转文件
                MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(tBigScreen.getImg() + "");
                //文件存储在nginx代理路径下
                String fileName = UploadFile.uploadFile(imgFile);
                tBigScreen.setImg(fileName);
            }
            boolean res = tBigScreenService.save(tBigScreen);
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("保存异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据主键来删除数据
     *
     * @param ids 主键集合
     * @return 返回删除结果
     */
    @Log("根据主键来删除大屏数据")
    @ApiOperation("根据主键来删除大屏数据")
    @PostMapping("deleteTBigScreen")
    public Result<Object> deleteTBigScreen(@RequestParam(name = "ids[]") String[] ids) {
        if (ids == null || ids.length == 0) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            boolean res = tBigScreenService.removeByIds(Arrays.asList(ids));
            if (res) {
                return ResultUtil.data(res, "删除成功");
            } else {
                return ResultUtil.error( "删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("删除异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("删除异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据主键来获取数据
     *
     * @param id 主键
     * @return 返回获取结果
     */
    @Log("根据主键来获取大屏数据")
    @ApiOperation("根据主键来获取大屏数据")
    @GetMapping("getTBigScreen")
    public Result<Object> getTBigScreen(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            TBigScreen res = tBigScreenService.getById(id);
            if (res != null) {
                return ResultUtil.data(res, "查询成功");
            } else {
                return ResultUtil.error("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：实现分页查询
     *
     * @param tBigScreen 需要模糊查询的信息
     * @param searchVo   查询参数
     * @param pageVo     分页参数
     * @return 返回获取结果
     */
    @Log("分页查询大屏数据")
    @ApiOperation("分页查询大屏数据")
    @GetMapping("queryTBigScreenList")
    public Result<Object> queryTBigScreenList(TBigScreen tBigScreen, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            return tBigScreenService.queryTBigScreenListByPage(tBigScreen, searchVo, pageVo);
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：更新数据
     *
     * @param tBigScreen 实体
     * @return 返回更新结果
     */
    @Log("更新大屏数据")
    @ApiOperation("更新大屏数据")
    @PostMapping("updateTBigScreen")
    public Result<Object> updateTBigScreen(@RequestBody TBigScreen tBigScreen) {
        if (StringUtils.isBlank(tBigScreen.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }

        Long time = System.currentTimeMillis();
        try {
            tBigScreen.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            if (tBigScreen.getImg() != null && tBigScreen.isUpdate()) {//图片保存
                //base64 转文件
                MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(tBigScreen.getImg() + "");
                //文件存储在nginx代理路径下
                String fileName = UploadFile.uploadFile(imgFile);
                tBigScreen.setImg(fileName);
            }
            boolean res = tBigScreenService.updateById(tBigScreen);
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("保存异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：导出数据
     *
     * @param response   请求参数
     * @param tBigScreen 查询参数
     * @return
     */
    @Log("导出大屏数据")
    @ApiOperation("导出大屏数据")
    @PostMapping("/download")
    public void download(HttpServletResponse response, TBigScreen tBigScreen) {
        Long time = System.currentTimeMillis();
        try {
            tBigScreenService.download(tBigScreen, response);
        } catch (Exception e) {
            e.printStackTrace();
            logService.addErrorLog("导出异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }
}