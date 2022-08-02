package me.zhengjie.controller;

import java.util.*;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.mapper.RelaVolunteerActivityMapper;
import me.zhengjie.mapper.TZhsqVolunteerMapper;
import me.zhengjie.entity.RelaVolunteerActivity;
import me.zhengjie.entity.TZhsqVolunteer;
import me.zhengjie.entity.TZhsqVolunteerActivity;
import me.zhengjie.service.IRelaVolunteerActivityService;
import me.zhengjie.service.ITZhsqVolunteerActivityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * 志愿者活动数据接口
 * @author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/tZhsqVolunteerActivity")
public class TZhsqVolunteerActivityController {

    private final ITZhsqVolunteerActivityService tZhsqVolunteerActivityService;

    private final IRelaVolunteerActivityService realVolunteerActivityService;

    private final RelaVolunteerActivityMapper relaVolunteerActivityMapper;

    private final TZhsqVolunteerMapper tZhsqVolunteerMapper;

    /**
     * 新增志愿者活动数据
     * @param tZhsqVolunteerActivity
     * @return
     */
    @PostMapping("addTZhsqVolunteerActivity")
    public Result<Object> addTZhsqVolunteerActivity(@RequestBody TZhsqVolunteerActivity tZhsqVolunteerActivity) {
        Long time = System.currentTimeMillis();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        try {
            tZhsqVolunteerActivity.setId(id);
            tZhsqVolunteerActivity.setIsDelete(0);
//            tZhsqVolunteerActivity.setCreateId(securityUtil.getCurrUser().getId().toString());
            tZhsqVolunteerActivity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            int i = tZhsqVolunteerActivityService.insertData(tZhsqVolunteerActivity);
            if (i > 0) {
                if (StringUtils.isNotBlank(tZhsqVolunteerActivity.getParticipant())) {
                    String[] split = tZhsqVolunteerActivity.getParticipant().split(",");
                    for (String s : split) {
                        RelaVolunteerActivity relaVolunteerActivity = new RelaVolunteerActivity();
                        relaVolunteerActivity.setActivityId(id);
                        relaVolunteerActivity.setVolunteerId(s);
                        realVolunteerActivityService.save(relaVolunteerActivity);
                    }
                }
                return ResultUtil.data(i, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
//            logService.addErrorLog("保存异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 根据主键来删除志愿者活动数据
     * @param map
     * @return
     */
    @PostMapping("deleteTZhsqVolunteerActivity")
    public Result<Object> deleteTZhsqVolunteerActivity(@RequestBody Map<String ,Object> map) {
        if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            List<String> ids = (List<String>) map.get("ids");
            if (ids == null || ids.size() == 0 ) {
                return ResultUtil.error("参数为空，请联系管理员！！");
            }
            boolean res = tZhsqVolunteerActivityService.removeByIds(ids);
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
     * 根据主键来获取志愿者活动数据
     * @param id
     * @return
     */
    @GetMapping("getTZhsqVolunteerActivity")
    public Result<Object> getTZhsqVolunteerActivity(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            TZhsqVolunteerActivity res = tZhsqVolunteerActivityService.getById(id);
            QueryWrapper<RelaVolunteerActivity> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.lambda().and(x -> x.eq(RelaVolunteerActivity::getActivityId, id));
            List<RelaVolunteerActivity> relaVolunteerActivities = relaVolunteerActivityMapper.selectList(objectQueryWrapper);

            ArrayList<String> strings = new ArrayList<>();
            for (int j = 0; j < relaVolunteerActivities.size(); j++) {
                strings.add(relaVolunteerActivities.get(j).getVolunteerId());
            }

            if (strings.size() > 0) {
                QueryWrapper<TZhsqVolunteer> wrapper = new QueryWrapper<>();
                wrapper.lambda().and(x -> x.in(TZhsqVolunteer::getId, strings.toArray()));
                List<TZhsqVolunteer> tZhsqVolunteers = tZhsqVolunteerMapper.selectList(wrapper);
                String name = "";
                for (int j = 0; j < tZhsqVolunteers.size(); j++) {
                    name += "," + tZhsqVolunteers.get(j).getName();
                }
                if (!"".equals(name)) {
                    name = name.substring(1);
                }
                res.setParticipantName(name);
            }
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
     * 分页查询志愿者活动数据
     * @param tZhsqVolunteerActivity
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryTZhsqVolunteerActivityList")
    public Result<Object> queryTZhsqVolunteerActivityList( TZhsqVolunteerActivity tZhsqVolunteerActivity, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            Result<Object> objectResult = tZhsqVolunteerActivityService.queryTZhsqVolunteerActivityListByPage(tZhsqVolunteerActivity, searchVo, pageVo);


            return objectResult;
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 更新志愿者活动数据
     * @param tZhsqVolunteerActivity
     * @return
     */
    @PostMapping("updateTZhsqVolunteerActivity")
    public Result<Object> updateTZhsqVolunteerActivity(@RequestBody TZhsqVolunteerActivity tZhsqVolunteerActivity) {
        if (StringUtils.isBlank(tZhsqVolunteerActivity.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
//            tZhsqVolunteerActivity.setUpdateId(securityUtil.getCurrUser().getId().toString());
            tZhsqVolunteerActivity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = tZhsqVolunteerActivityService.updateById(tZhsqVolunteerActivity);
            if (res) {

                QueryWrapper<RelaVolunteerActivity> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().and(i -> i.eq(RelaVolunteerActivity::getActivityId, tZhsqVolunteerActivity.getId()));
                realVolunteerActivityService.remove(queryWrapper);

                if (StringUtils.isNotBlank(tZhsqVolunteerActivity.getParticipant())) {
                    String[] split = tZhsqVolunteerActivity.getParticipant().split(",");
                    for (String s : split) {
                        RelaVolunteerActivity relaVolunteerActivity = new RelaVolunteerActivity();
                        relaVolunteerActivity.setActivityId(tZhsqVolunteerActivity.getId());
                        relaVolunteerActivity.setVolunteerId(s);
                        realVolunteerActivityService.save(relaVolunteerActivity);
                    }
                }
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
     * 导出志愿者活动数据
     * @param response
     * @param tZhsqVolunteerActivity
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, SearchVo searchVo, TZhsqVolunteerActivity tZhsqVolunteerActivity) {
        Long time = System.currentTimeMillis();
        try {
            tZhsqVolunteerActivityService.download(tZhsqVolunteerActivity,searchVo, response);
        } catch (Exception e) {
//            logService.addErrorLog("导出异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }
}
