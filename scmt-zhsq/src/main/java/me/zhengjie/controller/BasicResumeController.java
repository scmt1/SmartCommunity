package me.zhengjie.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicResume;
import me.zhengjie.service.IBasicResumeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;


/**
 * 履历数据接口
 * @author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/basicResume")
public class BasicResumeController {

    private final IBasicResumeService basicResumeService;


    /**
     * 新增履历数据
     * @param basicResume
     * @return
     */
    @PostMapping("addBasicResume")
    public Result<Object> addBasicResume(BasicResume basicResume) {
        Long time = System.currentTimeMillis();
        try {
            basicResume.setIsDelete(0);
            basicResume.setCreateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = basicResumeService.save(basicResume);
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
//			logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 根据主键来删除履历数据
     * @param map
     * @return
     */
    @PostMapping("deleteBasicResume")
    public Result<Object> deleteBasicResume(@RequestBody Map<String ,Object> map) {
        if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            List<String> ids = (List<String>) map.get("ids");
            if (ids == null || ids.size() == 0 ) {
                return ResultUtil.error("参数为空，请联系管理员！！");
            }
            boolean res = basicResumeService.removeByIds(ids);
            if (res) {
                return ResultUtil.data(res, "删除成功");
            } else {
                return ResultUtil.error( "删除失败");
            }
        } catch (Exception e) {
//			logService.addErrorLog("删除异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("删除异常:" + e.getMessage());
        }
    }

    /**
     * 根据主键来获取履历数据
     * @param id
     * @return
     */
    @GetMapping("getBasicResume")
    public Result<Object> getBasicResume(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            BasicResume res = basicResumeService.getById(id);
            if (res != null) {
                return ResultUtil.data(res, "查询成功");
            } else {
                return ResultUtil.error("查询失败");
            }
        } catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 分页查询履历数据
     * @param basicResume
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryBasicResumeList")
    public Result<Object> queryBasicResumeList(BasicResume basicResume, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            return basicResumeService.queryBasicResumeListByPage(basicResume, searchVo, pageVo);
        } catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 更新履历数据
     * @param basicResume
     * @return
     */
    @PostMapping("updateBasicResume")
    public Result<Object> updateBasicResume(BasicResume basicResume) {
        if (StringUtils.isBlank(basicResume.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            basicResume.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = basicResumeService.updateById(basicResume);
            if (res) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
//			logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 导出履历数据
     * @param response
     * @param basicResume
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, BasicResume basicResume) {
        Long time = System.currentTimeMillis();
        try {
            basicResumeService.download(basicResume, response);
        } catch (Exception e) {
//			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
        }
    }

    /**
     * 根据社区干部Id,查询履历
     * @param personId
     * @return
     */
    @GetMapping("/queryBasicResumeListByPersonId")
    public Result<Object> queryBasicResumeListByPersonId(String personId) {
        try {
            List<BasicResume> list = basicResumeService.queryBasicResumeListByPersonId(personId);
            return ResultUtil.data(list);
        } catch (Exception e) {
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }
}
