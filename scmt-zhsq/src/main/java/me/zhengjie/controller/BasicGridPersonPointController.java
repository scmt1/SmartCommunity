package me.zhengjie.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

//import  me.zhengjie.aop.log.Log;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicGridPersonPoint;

import me.zhengjie.service.IBasicGridPersonPointService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

//import  me.zhengjie.service.LogService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;


/**
 * 网格员数据接口
 * @author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/basicGridPersonPoint")
public class BasicGridPersonPointController {

    private final IBasicGridPersonPointService basicGridPersonPointService;
//    @Autowired
//    private LogService logService;

    /**
     * 功能描述：新增basic数据
     *
     * @param basicGridPersonPoint 实体
     * @return 返回新增结果
     */
//    @Log("新增网格员数据")

    /**
     * 新增网格员数据
     * @param basicGridPersonPoint
     * @return
     */
    @PostMapping("addBasicGridPersonPoint")
    public Result<Object> addBasicGridPersonPoint(BasicGridPersonPoint basicGridPersonPoint) {
        Long time = System.currentTimeMillis();
        try {
            basicGridPersonPoint.setIsDelete(0);
            basicGridPersonPoint.setCreateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = basicGridPersonPointService.save(basicGridPersonPoint);
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
//    @Log("根据主键来删除网格员数据")

    /**
     * 根据主键来删除网格员数据
     * @param map
     * @return
     */
    @PostMapping("deleteBasicGridPersonPoint")
    public Result<Object> deleteBasicGridPersonPoint(@RequestBody Map<String ,Object> map) {
        if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            List<String> ids = (List<String>) map.get("ids");
            if (ids == null || ids.size() == 0 ) {
                return ResultUtil.error("参数为空，请联系管理员！！");
            }
            boolean res = basicGridPersonPointService.removeByIds(ids);
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
//    @Log("根据主键来获取网格员数据")

    /**
     * 根据主键来获取网格员数据
     * @param id
     * @return
     */
    @GetMapping("getBasicGridPersonPoint")
    public Result<Object> getBasicGridPersonPoint(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            BasicGridPersonPoint res = basicGridPersonPointService.getById(id);
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
//    @Log("分页查询basic数据")

    /**
     * 分页查询网格员数据
     * @param basicGridPersonPoint
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryBasicGridPersonPointList")
    public Result<Object> queryBasicGridPersonPointList(BasicGridPersonPoint basicGridPersonPoint, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            return basicGridPersonPointService.queryBasicGridPersonPointListByPage(basicGridPersonPoint, searchVo, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：更新数据
     *
     * @param basicGridPersonPoint 实体
     * @return 返回更新结果
     */
//    @Log("更新basic数据")

    /**
     * 更新网格员数据
     * @param basicGridPersonPoint
     * @return
     */
    @PostMapping("updateBasicGridPersonPoint")
    public Result<Object> updateBasicGridPersonPoint(BasicGridPersonPoint basicGridPersonPoint) {
        if (StringUtils.isBlank(basicGridPersonPoint.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            basicGridPersonPoint.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = basicGridPersonPointService.updateById(basicGridPersonPoint);
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
     * @param response             请求参数
     * @param basicGridPersonPoint 查询参数
     * @return
     */
//    @Log("导出basic数据")

    /**
     * 导出网格员数据
     * @param response
     * @param basicGridPersonPoint
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, BasicGridPersonPoint basicGridPersonPoint) {
        Long time = System.currentTimeMillis();
        try {
            basicGridPersonPointService.download(basicGridPersonPoint, response);
        } catch (Exception e) {
//            logService.addErrorLog("导出异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }

//    @Log("查询当天数据")

    /**
     * 查询当天数据
     * @param gridPersonId
     * @param gridPersonName
     * @param date
     * @return
     */
    @PostMapping("/getToDayGridPersonPoint")
    public Result<Object> getToDayGridPersonPoint(String gridPersonId,String gridPersonName, String date) {
		Long time = System.currentTimeMillis();
		try {
			List<BasicGridPersonPoint> list = basicGridPersonPointService.getToDayGridPersonPoint(gridPersonId,gridPersonName, date);
			return ResultUtil.data(list);
		} catch (Exception e) {
//			logService.addErrorLog("查询当天数据异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
			return ResultUtil.error(e.getMessage());
		}
    }

//    @Log("查询网格人员最新的坐标数据")

    /**
     * 查询网格人员最新的坐标数据
     * @param gridPersonId
     * @return
     */
    @PostMapping("/getCurrentGridPersonPoint")
    public Result<Object> getCurrentGridPersonPoint(String gridPersonId) {
        Long time = System.currentTimeMillis();
        try {
            BasicGridPersonPoint basicGridPersonPoint = basicGridPersonPointService.getCurrentGridPersonPoint(gridPersonId);
            return ResultUtil.data(basicGridPersonPoint);
        } catch (Exception e) {
//            logService.addErrorLog("查询当天数据异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error(e.getMessage());
        }
    }

}
