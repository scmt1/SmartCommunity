package me.zhengjie.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.*;
import me.zhengjie.service.IBasicGridPersonPointService;
import me.zhengjie.service.IBasicGridsService;
import me.zhengjie.service.IRelaGridsPersonGridsService;
import me.zhengjie.service.ITZhsqGridMemberService;
import me.zhengjie.system.service.dto.UserDto;
import me.zhengjie.util.PointUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import  me.zhengjie.aop.log.Log;


/**
 * 网格员档案数据接口
 *
 * @author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/tZhsqGridMember")
public class TZhsqGridMemberController {

    private final ITZhsqGridMemberService tZhsqGridMemberService;

    private final IRelaGridsPersonGridsService relaGridsPersonGridsService;

    private final IBasicGridsService basicGridsService;

    private final ITZhsqGridMemberService itZhsqGridMemberService;

    private final IBasicGridPersonPointService basicGridPersonPointService;

    private final SecurityUtil securityUtil;

    /**
     * 新增网格员档案数据
     *
     * @param tZhsqGridMember
     * @return
     */
    @PostMapping("addTZhsqGridMember")
    public Result<Object> addTZhsqGridMember(@RequestBody TZhsqGridMember tZhsqGridMember) {
        Long time = System.currentTimeMillis();
        try {
            if (tZhsqGridMember.getGrid() == null && tZhsqGridMember.getGrid().length == 0) {
                return ResultUtil.error("请选择对应的网格！");
            }

            boolean flag = false;
            if (StringUtils.isNotBlank(tZhsqGridMember.getPost()) && tZhsqGridMember.getPost().contains("网格长")) {
                String[] grid = tZhsqGridMember.getGrid();
                for (String s : grid) {
                    //判断网格是否已有网格长
                    List<TZhsqGridMember> list = tZhsqGridMemberService.existsGrid(s);
                    if (list.size() > 0) {
                        return ResultUtil.error("该网格已有网格长，请重新选择！");
                    }
                }
                flag = true;
            }
            //身份证验证重复
            if (StringUtils.isNotBlank(tZhsqGridMember.getIdCard())) {
                //根据id和身份证获取信息
                QueryWrapper<TZhsqGridMember> queryWrapper = new QueryWrapper();
                queryWrapper.lambda().and(j -> j.eq(TZhsqGridMember::getIdCard, tZhsqGridMember.getIdCard()));
                queryWrapper.lambda().and(j -> j.eq(TZhsqGridMember::getIsDelete, 0));
                int res = tZhsqGridMemberService.count(queryWrapper);
                if (res >= 1) {
                    return ResultUtil.error("身份证号码重复,请检查");
                }
            }
            //手机号验证重复
            if (StringUtils.isNotBlank(tZhsqGridMember.getPhone())) {
                //根据id和手机号获取信息
                QueryWrapper<TZhsqGridMember> queryWrapper = new QueryWrapper();
                queryWrapper.lambda().and(j -> j.eq(TZhsqGridMember::getPhone, tZhsqGridMember.getPhone()));
                queryWrapper.lambda().and(j -> j.eq(TZhsqGridMember::getIsDelete, 0));
                int res = tZhsqGridMemberService.count(queryWrapper);
                if (res >= 1) {
                    return ResultUtil.error("手机号重复,请检查");
                }
            }

            tZhsqGridMember.setIsDelete(0);
//            if (StringUtils.isNotBlank(tZhsqGridMember.getHeadPortrait())) {
//                //base64 转文件
//                MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(tZhsqGridMember.getHeadPortrait());
//                //文件存储在nginx代理路径下
//                String fileName = UploadFile.uploadFile(imgFile);
//                tZhsqGridMember.setHeadPortrait(fileName);
//            }
            tZhsqGridMember.setCreateTime(new Timestamp(System.currentTimeMillis()));
            tZhsqGridMember.setBirthday(new Timestamp(System.currentTimeMillis()));
            boolean res = tZhsqGridMemberService.insertEntity(tZhsqGridMember);
            if (res) {
                //更新网格中的网格长
                if (flag) {
                    String[] grid = tZhsqGridMember.getGrid();
                    BasicGrids basicGrids = basicGridsService.getById(grid[0]);
                    basicGrids.setId(grid[0]);
                    basicGrids.setGridPersonId(tZhsqGridMember.getId());
                    basicGrids.setGridPersonName(tZhsqGridMember.getName());
                    basicGridsService.updateById(basicGrids);
                }
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            logService.addErrorLog("保存异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 根据主键来删除网格员档案数据
     *
     * @param map
     * @return
     */
    @PostMapping("deleteTZhsqGridMember")
    public Result<Object> deleteTZhsqGridMember(@RequestBody Map<String, Object> map) {
        if (map == null || map.size() == 0 || !map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            List<String> ids = (List<String>) map.get("ids");
            if (ids == null || ids.size() == 0) {
                return ResultUtil.error("参数为空，请联系管理员！！");
            }
            boolean res = tZhsqGridMemberService.removeByIds(ids);
            if (res) {
                for (String id : ids) {
                    if (StringUtils.isNotBlank(id)) {
                        QueryWrapper<RelaGridsPersonGrids> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().and(i -> i.eq(RelaGridsPersonGrids::getGridsPersonId, id));
                        relaGridsPersonGridsService.remove(queryWrapper);
                    }
                }
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
     * 修改电话号码
     *
     * @param tZhsqGridMember
     * @param
     * @param
     * @return
     */
    @PostMapping("updatePhone")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> queryBasicPersonListByAnyWayWhere(@RequestBody TZhsqGridMember tZhsqGridMember) {
        Long time = System.currentTimeMillis();
        if(StringUtils.isBlank(tZhsqGridMember.getId())){
            return ResultUtil.error("id参数为空,请检查");
        }
        //手机号验证重复
        if (StringUtils.isNotBlank(tZhsqGridMember.getIdCard())) {
            //根据id和手机号获取信息
            QueryWrapper<TZhsqGridMember> queryWrapper = new QueryWrapper();
            queryWrapper.lambda().and(j -> j.eq(TZhsqGridMember::getPhone, tZhsqGridMember.getPhone()));
            queryWrapper.lambda().and(j -> j.ne(TZhsqGridMember::getId, tZhsqGridMember.getId()));
            queryWrapper.lambda().and(j -> j.eq(TZhsqGridMember::getIsDelete, 0));
            int res = tZhsqGridMemberService.count(queryWrapper);
            if (res >= 1) {
                return ResultUtil.error("手机号重复,请检查");
            }

        }
        boolean res = tZhsqGridMemberService.updateById(tZhsqGridMember);
        if (res) {
            return ResultUtil.data(res, "保存成功");
        } else {
            return ResultUtil.error("保存失败");
        }

    }

    /**
     * 根据主键来获取网格员档案数据
     *
     * @param id
     * @return
     */
    @GetMapping("getTZhsqGridMember")
    public Result<Object> getTZhsqGridMember(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            TZhsqGridMember res = tZhsqGridMemberService.getById(id);
            List<RelaGridsPersonGrids> gridsPersonGrids = relaGridsPersonGridsService.getDataByPersonId(id);
            res.setGridsPersonGrids(gridsPersonGrids);
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
     * 分页查询网格员档案数据
     *
     * @param tZhsqGridMember
     * @param gridId
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryTZhsqGridMemberList")
    public Result<Object> queryTZhsqGridMemberList(TZhsqGridMember tZhsqGridMember, String gridId, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            //用户数据权限
            UserDto user = securityUtil.getCurrUser();
            if (user.getPower() != null){
                if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                    String deptId = user.getPower().getDeptId();
                    Integer attribute = user.getPower().getAttribute();
                    if (attribute == 1){// 街道
                        tZhsqGridMember.setStreetId(deptId);
                    }else if (attribute == 2) {// 社区
                        tZhsqGridMember.setCommunityId(deptId);
                    }else if (attribute == 3) {// 网格
                        gridId = deptId;
                    }
                }
            }
            return tZhsqGridMemberService.queryTZhsqGridMemberListByPage(tZhsqGridMember, gridId, searchVo, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 查询所有网格长
     *
     * @param tZhsqGridMember
     * @return
     */
    @GetMapping("queryAllTZhsqGridMemberList")
    public Result<Object> queryAllTZhsqGridMemberList(TZhsqGridMember tZhsqGridMember) {
        Long time = System.currentTimeMillis();
        try {
            tZhsqGridMember.setPost("网格长");
            return tZhsqGridMemberService.queryGridMemberList(tZhsqGridMember, null);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }


    /**
     * 查询当前网格下的网格长
     *
     * @param gridId
     * @return
     */
    @GetMapping("queryAllTZhsqGridMemberListByGridId")
    public Result<Object> queryAllTZhsqGridMemberListByGridId(String gridId) {
        Long time = System.currentTimeMillis();
        try {
            return tZhsqGridMemberService.queryAllTZhsqGridMemberListByGridId(gridId);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 查询当前网格下的网格员
     *
     * @param gridId
     * @return
     */
    @GetMapping("queryAllGridMemberListByGridId")
    public Result<Object> queryAllGridMemberListByGridId(String gridId) {
        Long time = System.currentTimeMillis();
        try {
            return tZhsqGridMemberService.queryAllGridMemberListByGridId(gridId);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 更新网格员档案数据
     *
     * @param tZhsqGridMember
     * @return
     */
    @PostMapping("updateTZhsqGridMember")
    @Transactional
    public Result<Object> updateTZhsqGridMember(@RequestBody TZhsqGridMember tZhsqGridMember) {
        if (StringUtils.isBlank(tZhsqGridMember.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }

        Long time = System.currentTimeMillis();
        try {
            boolean flag = false;
            if (StringUtils.isNotBlank(tZhsqGridMember.getPost()) && tZhsqGridMember.getPost().contains("网格长")) {
                String[] grid = tZhsqGridMember.getGrid();
                for (String s : grid) {
                    //判断网格是否已有网格长
                    List<TZhsqGridMember> list = tZhsqGridMemberService.existsGrid(s);
                    for (TZhsqGridMember zhsqGridMember : list) {
                        if (!zhsqGridMember.getId().equals(tZhsqGridMember.getId())) {
                            return ResultUtil.error("该网格已有网格长，请重新选择！");
                        }
                    }
                }
                flag = true;
            }
            //身份证验证重复
            if (StringUtils.isNotBlank(tZhsqGridMember.getIdCard())) {
                //根据id和身份证获取信息
                QueryWrapper<TZhsqGridMember> queryWrapper = new QueryWrapper();
                queryWrapper.lambda().and(j -> j.eq(TZhsqGridMember::getIdCard, tZhsqGridMember.getIdCard()));
                queryWrapper.lambda().and(j -> j.ne(TZhsqGridMember::getId, tZhsqGridMember.getId()));
                queryWrapper.lambda().and(j -> j.eq(TZhsqGridMember::getIsDelete, 0));
                int res = tZhsqGridMemberService.count(queryWrapper);
                if (res >= 1) {
                    return ResultUtil.error("身份证号码重复,请检查");
                }

            }
            //手机号验证重复
            if (StringUtils.isNotBlank(tZhsqGridMember.getIdCard())) {
                //根据id和手机号获取信息
                QueryWrapper<TZhsqGridMember> queryWrapper = new QueryWrapper();
                queryWrapper.lambda().and(j -> j.eq(TZhsqGridMember::getPhone, tZhsqGridMember.getPhone()));
                queryWrapper.lambda().and(j -> j.ne(TZhsqGridMember::getId, tZhsqGridMember.getId()));
                queryWrapper.lambda().and(j -> j.eq(TZhsqGridMember::getIsDelete, 0));
                int res = tZhsqGridMemberService.count(queryWrapper);
                if (res >= 1) {
                    return ResultUtil.error("手机号重复,请检查");
                }

            }

//            if (StringUtils.isNotBlank(tZhsqGridMember.getHeadPortrait()) && tZhsqGridMember.getImageIsUpdate()) {
//                //base64 转文件
//                MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(tZhsqGridMember.getHeadPortrait());
//                //文件存储在nginx代理路径下
//                String fileName = UploadFile.uploadFile(imgFile);
//                tZhsqGridMember.setHeadPortrait(fileName);
//            }
            tZhsqGridMember.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            if (StringUtils.isBlank(tZhsqGridMember.getCommunityName())) {
                tZhsqGridMember.setCommunityName(null);
            }
            List<RelaGridsPersonGrids> gridsPersonGrids = relaGridsPersonGridsService.getDataByPersonId(tZhsqGridMember.getId());
            {
                JSONObject user1 = new JSONObject();
                user1.put("personId", tZhsqGridMember.getId());
                user1.put("realName", tZhsqGridMember.getName());
                user1.put("account", tZhsqGridMember.getPhone());
                user1.put("sex", tZhsqGridMember.getSex());
                user1.put("idNumber", tZhsqGridMember.getIdCard());
                user1.put("birthday", tZhsqGridMember.getBirthday());
                StringBuffer grids = new StringBuffer();
                String[] grid = tZhsqGridMember.getGrid();
                if (grid.length > 0) {
                    for (String s : grid) {
                        grids.append(s).append(",");
                    }
                    int i = grids.length() - 1;
                }
                user1.put("gridId", grids.toString());
                user1.put("phone", tZhsqGridMember.getPhone());
                user1.put("communityId", tZhsqGridMember.getCommunityId());
//                JSONObject jsonObject = baseClient.synchronModify(user1);
//                if (jsonObject.getInteger("code") != 200) {
//                    throw new BusinessErrorException("同步异常");
//                }
            }
            boolean res = tZhsqGridMemberService.updateByEntity(tZhsqGridMember);
            if (res) {
                //更新网格中的网格
                if (flag) {
                    String[] grid = tZhsqGridMember.getGrid();
                    BasicGrids basicGrids = basicGridsService.getById(grid[0]);
                    if(basicGrids!=null){
                        basicGrids.setGridPersonId(tZhsqGridMember.getId());
                        basicGrids.setGridPersonName(tZhsqGridMember.getName());
                        basicGridsService.updateById(basicGrids);

                        String[] gridOld = new String[1];
                        gridOld[0] = gridsPersonGrids.get(0).getGridsId();
                        if (gridOld != null && !gridOld[0].equals(grid[0])) {
                            basicGrids = basicGridsService.getById(gridOld[0]);
                            if(basicGrids!=null){
                                basicGrids.setGridPersonId("");
                                basicGrids.setGridPersonName("");
                                basicGridsService.updateById(basicGrids);
                            }

                        }
                    }
                }
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            logService.addErrorLog("保存异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 导出网格员档案数据
     *
     * @param response
     * @param tZhsqGridMember
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, TZhsqGridMember tZhsqGridMember,SearchVo searchVo) {
        Long time = System.currentTimeMillis();
        try {
            tZhsqGridMemberService.download(tZhsqGridMember, response,searchVo);
        } catch (Exception e) {
//            logService.addErrorLog("导出异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }

    /**
     * 统计网格员档案数据
     *
     * @param gridMember
     * @param pageVo
     * @return
     */
    @GetMapping("getStatisticsData")
    public Result<Object> getStatisticsData(TZhsqGridMember gridMember, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            //用户数据权限
            UserDto user = securityUtil.getCurrUser();
            if (user.getPower() != null){
                if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                    String deptId = user.getPower().getDeptId();
                    Integer attribute = user.getPower().getAttribute();
                    if (attribute == 1){// 街道
                        gridMember.setStreetId(deptId);
                    }else if (attribute == 2) {// 社区
                        gridMember.setCommunityId(deptId);
                    }else if (attribute == 3) {// 网格
                        gridMember.setOwnedGrid(deptId);
                    }
                }
            }
            Result<Object> res = tZhsqGridMemberService.getStatisticsData(gridMember, pageVo);
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
     * 网格干部模糊查询所有
     *
     * @param tZhsqGridMember
     * @param gridId
     * @return
     */
    @GetMapping("queryGridMemberList")
    public Result<Object> querywgzList(TZhsqGridMember tZhsqGridMember, String gridId) {
        Long time = System.currentTimeMillis();
        try {
            return tZhsqGridMemberService.queryGridMemberList(tZhsqGridMember, gridId);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }


    /**
     * 查询全部网格人口档案
     *
     * @param tZhsqGridMember
     * @param key
     * @return
     */
    @GetMapping("/queryAllTZhsqGridMemberListByAnyWayWhere")
    public Result<Object> queryAllTZhsqGridMemberListByAnyWayWhere(TZhsqGridMember tZhsqGridMember, String key) {
        Long time = System.currentTimeMillis();
        try {
            //用户数据权限
            UserDto user = securityUtil.getCurrUser();
            if (user.getPower() != null){
                if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                    String deptId = user.getPower().getDeptId();
                    Integer attribute = user.getPower().getAttribute();
                    if (attribute == 1){// 街道
                        tZhsqGridMember.setStreetId(deptId);
                    }else if (attribute == 2) {// 社区
                        tZhsqGridMember.setCommunityId(deptId);
                    }else if (attribute == 3) {// 网格
                        tZhsqGridMember.setGridId(deptId);
                    }
                }
            }
            return tZhsqGridMemberService.queryAllTZhsqGridMemberListByAnyWayWhere(tZhsqGridMember, key);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }


    /**
     * 查询网格人员轨迹
     *
     * @param gridPersonId
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/gridPersonTrackQuery")
    public Result<Object> gridPersonTrackQuery(String gridPersonId, String startTime, String endTime) {
        Long time = System.currentTimeMillis();
        try {
            List<BasicGridPersonPoint> list = tZhsqGridMemberService.gridPersonTrackQuery(gridPersonId, startTime, endTime);
            return ResultUtil.data(list);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 根据网格员坐标查询周边网格人员
     *
     * @param pointX
     * @param pointY
     * @return
     */
    @GetMapping("/queryPeripheryGridsPersonByPoint")
    public Result<Object> queryPeripheryGridsPersonByPoint(String pointX, String pointY) {
        if (StringUtils.isBlank(pointX)) {
            return ResultUtil.error("坐标X不能为空");
        }
        if (StringUtils.isBlank(pointY)) {
            return ResultUtil.error("坐标Y不能为空");
        }

        try {
            double x = Double.parseDouble(pointX);
            double y = Double.parseDouble(pointY);

            List<BasicGrids> list = basicGridsService.list();

            Point[] pointArr = null;

            for (BasicGrids basicGrids : list) {
                String position = basicGrids.getPosition();
                if (StringUtils.isNotBlank(position)) {
                    position = position.substring(1);
                    position = position.substring(0, position.length() - 1);

                    String[] split = position.split(",");
                    List<Point> pointList = new ArrayList<>();
                    Point point = new Point();
                    for (int i = 0; i < split.length; i += 2) {
                        point = new Point();
                        point.setX(Double.parseDouble(split[i]));
                        point.setY(Double.parseDouble(split[i + 1]));
                        pointList.add(point);
                    }

                    Point[] objects = pointList.toArray(new Point[]{});
                    if (PointUtils.isPtInPoly(x, y, objects)) {
                        //aboutGrid = basicGrids;//得到所在范围的网格
                        pointArr = objects;
                        break;
                    }
                }
            }

            List<TZhsqGridMember> gridMemberList = new ArrayList<>();

            //根据网格、查询该网格范围内所有的网格员
            if (pointArr != null) {
                List<TZhsqGridMember> list1 = itZhsqGridMemberService.list();//查询所有网格员  筛选出在范围内的网格员
                for (TZhsqGridMember tZhsqGridMember : list1) {
                    BasicGridPersonPoint currentGridPersonPoint = basicGridPersonPointService.getCurrentGridPersonPoint(tZhsqGridMember.getId());
                    if (currentGridPersonPoint != null) {
                        String position = currentGridPersonPoint.getPosition();
                        position = position.substring(1);
                        position = position.substring(0, position.length() - 1);
                        String[] split = position.split(",");

                        if (PointUtils.isPtInPoly(Double.parseDouble(split[0]), Double.parseDouble(split[1]), pointArr)) {
                            gridMemberList.add(tZhsqGridMember);
                        }
                    }
                }
            }

            return ResultUtil.data(gridMemberList);
        } catch (Exception e) {
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 根据社区id和网格id查询网格
     *
     * @param communityId
     * @param gridId
     * @return
     */
    @GetMapping("/selectByCommunityAndGrid")
    public Result<Object> selectByCommunityAndGrid(String communityId, String gridId) {
        try {
            int i = itZhsqGridMemberService.selectByCommunityIdAndGridId(communityId, gridId);
            return ResultUtil.data(i, "查询成功");
        } catch (Exception e) {
            return ResultUtil.error("查询失败");
        }
    }

    /**
     * 获取人员树菜单
     *
     * @return
     */
    @GetMapping("getGridMemberTree")
    public Result<Object> getGridMemberTree() {
        //用户数据权限
//        UserDto user = securityUtil.getCurrUser();
//        Map<String,Object> map = null;
//        if (user.getPower() != null){
//            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
//                map = new HashMap<>();
//                String deptId = user.getPower().getDeptId();
//                Integer attribute = user.getPower().getAttribute();
//                if (attribute == 1){// 街道
//                    map.put("streetId",deptId);
//                }else if (attribute == 2) {// 社区
//                    map.put("communityId",deptId);
//                }else if (attribute == 3) {// 网格
//                    map.put("gridId",deptId);
//                }
//            }
//        }
        try{
            return ResultUtil.data(itZhsqGridMemberService.selectGridMemberTree(), "查询成功");
        }catch (Exception e) {
            return ResultUtil.error("查询失败");
        }
    }
    /**
     * Excel导入网格员信息数据
     *
     * @param file
     * @return
     */
    @PostMapping("/importGridMemberExcel")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> importGridMemberExcel(@RequestBody MultipartFile file) {
        //@RequestParam("file") MultipartFile file) throws IOException, Exception
        Long time = System.currentTimeMillis();
        try {
            return itZhsqGridMemberService.importExcel(file,false);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.error("导入异常" + e.getMessage());
        }
    }

    /**
     * Excel导入网格长信息数据
     *
     * @param file
     * @return
     */
    @PostMapping("/importGridLengthExcel")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> importGridLengthExcel(@RequestBody MultipartFile file) {
        //@RequestParam("file") MultipartFile file) throws IOException, Exception
        Long time = System.currentTimeMillis();
        try {
            return itZhsqGridMemberService.importExcel(file,true);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.error("导入异常" + e.getMessage());
        }
    }
}
