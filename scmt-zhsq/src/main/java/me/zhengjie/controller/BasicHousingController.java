package me.zhengjie.controller;

import java.util.*;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
//import  me.zhengjie.aop.log.Log;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.entity.BasicHousing;
import me.zhengjie.entity.BasicPerson;
import me.zhengjie.entity.RelaPersonHouse;
import me.zhengjie.entity.TBuildingArchives;
//import org.springframework.security.core.parameters.P;
import me.zhengjie.service.IBasicHousingService;
import me.zhengjie.service.IBasicPersonService;
import me.zhengjie.service.IRelaPersonHouseService;
import me.zhengjie.service.ITBuildingArchivesService;
import me.zhengjie.system.service.UserService;
import me.zhengjie.system.service.dto.UserDto;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.web.multipart.MultipartFile;


/**
 * 房屋管理数据接口
 * @author
 **/
@RestController
@RequestMapping("/api/basicHousing")
@AllArgsConstructor
public class BasicHousingController {
  
    private final IBasicHousingService basicHousingService;
  
    private final IBasicPersonService basicPersonService;
  
    private final IRelaPersonHouseService relaPersonHouseService;

    private final ITBuildingArchivesService itBuildingArchivesService;

    private final SecurityUtil securityUtil;

    /**
     * 新增房屋数据
     * @param basicHousing
     * @return
     */
    @PostMapping("addBasicHousing")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> addBasicHousing(@RequestBody BasicHousing basicHousing) {
        Long time = System.currentTimeMillis();
        //判断房间是否录入
        boolean houseMore = isHouseMore(basicHousing);
        if (houseMore) {
            return ResultUtil.error("该房间已录入，请检查后重试！");
        }
        //房屋表保存
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        basicHousing.setCreateTime(new Timestamp(System.currentTimeMillis()));
        basicHousing.setIsDelete(0);
        basicHousing.setId(id);
        String houseCode = setHouseCode(basicHousing);
        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(houseCode)) {
            basicHousing.setHouseCode(houseCode);
        } else {
            return ResultUtil.error("保存失败");
        }
        boolean row = basicHousingService.save(basicHousing);
        if (row) {
//            JSONObject houseParams = new JSONObject();
//            houseParams.put("roomNum",basicHousing.getFloor()+basicHousing.getDoorNumber());
//            houseParams.put("unitUuid",basicHousing.getUnitId());
//            houseParams.put("thirdPartyId",basicHousing.getId());
//            JSONObject result = thirdPartyClient.saveUpdateHousing(houseParams);
//            if (!"200".equals(result.getString("code"))){
//                throw new BusinessErrorException("同步第三方房屋失败");
//            }
            return ResultUtil.success("保存成功");
        } else {
            return ResultUtil.error("保存失败");
        }
    }

    /**
     * 根据主键来删除房屋数据
     * @param map
     * @return
     */
    @PostMapping("deleteBasicHousing")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> deleteBasicHousing(@RequestBody Map<String ,Object> map) {
        if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        List<String> ids = (List<String>) map.get("ids");
        if (ids == null || ids.size() == 0 ) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }

        QueryWrapper<RelaPersonHouse> basicHouseQueryWrapper = new QueryWrapper<>();
        basicHouseQueryWrapper.lambda().and(i -> i.in(RelaPersonHouse::getHouseId, ids));

        int count  = relaPersonHouseService .count(basicHouseQueryWrapper);
        if(count>0){
            return ResultUtil.error("您删除的房屋名下 拥有人口数据，您不能删除此房屋，请先删除房屋下的人口");
        }
        boolean res = basicHousingService.removeByIds(ids);
        if (res) {
            JSONObject houseParams;
//            for (String id : ids) {
//                houseParams = new JSONObject();
//                houseParams.put("uuid",id);
//                thirdPartyClient.saveUpdateHousing(houseParams);
//            }
            return ResultUtil.data(res, "删除成功");
        } else {
            return ResultUtil.error( "删除失败");
        }
    }

    /**
     * 根据主键来获取房屋数据
     * @param id
     * @returnqueryBasicHousingList
     */
    @GetMapping("getBasicHousing")
    public Result<Object> getBasicHousing(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            BasicHousing res = basicHousingService.getById(id);
            //查询房屋下人口
            QueryWrapper<RelaPersonHouse> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().and(i -> i.eq(RelaPersonHouse::getHouseId, id));
            List<RelaPersonHouse> list = relaPersonHouseService.list(queryWrapper);
            if (list != null && list.size() > 0) {
                //查找已经落户
                for (RelaPersonHouse rp : list) {
                    if ("是".equals(rp.getIsSettle())) {
                        if (StringUtils.isNotBlank(rp.getPersonId())) {
                            BasicPerson person = basicPersonService.getById(rp.getPersonId());
                            res.setIsSettle("是");
                            res.setAccNumber(person.getAccNumber());
                            res.setAccType(person.getAccType());
                            res.setAccName(person.getAccName());
                            res.setAccRelation(person.getAccRelation());
                            res.setAccCard(person.getAccCard());
                        }
                    }
                    break;
                }
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
     * 分页查询房屋数据
     * @param basicHousing
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryBasicHousingList")
    public Result<Object> queryBasicHousingList(BasicHousing basicHousing, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            //用户数据权限
            UserDto user = securityUtil.getCurrUser();
            if (user.getPower() != null){
                if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                    String deptId = user.getPower().getDeptId();
                    Integer attribute = user.getPower().getAttribute();
                    if (attribute == 1){// 街道
                        basicHousing.setStreetId(deptId);
                    }else if (attribute == 2) {// 社区
                        basicHousing.setCommunityId(deptId);
                    }else if (attribute == 3) {// 网格
                        basicHousing.setOwnedGridId(deptId);
                    }
                }
            }
            return basicHousingService.queryBasicHousingListByPage(basicHousing, searchVo, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 分页查询房屋数据
     * @param basicHousing
     * @param key
     * @param pageVo
     * @return
     */
    @GetMapping("queryBasicHousingListByAnyWayWhere")
    public Result<Object> queryBasicHousingListByAnyWayWhere(BasicHousing basicHousing, String key, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            return basicHousingService.queryBasicHousingListByAnyWayWhere(basicHousing, key, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 更新数据租客
     * @param basicHousing
     * @return
     */
    @PostMapping("updateBasicHousingCustomer")
    public Result<Object> updateBasicHousingCustomer(@RequestBody BasicHousing basicHousing) {
        return basicHousingService.updatePersonAndBasicHouse(basicHousing);
    }

    /**
     * 更新数据
     * @param basicHousing
     * @return
     */
    @PostMapping("updateBasicHousing")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> updateBasicHousing(@RequestBody BasicHousing basicHousing) {
        if (StringUtils.isBlank(basicHousing.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        String houseCode = setHouseCode(basicHousing);
        if (StringUtils.isNotBlank(houseCode)) {
            basicHousing.setHouseCode(houseCode);
        } else {
            return ResultUtil.error("保存失败");
        }
        Long time = System.currentTimeMillis();
        basicHousing.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        boolean res = basicHousingService.updateById(basicHousing);
        if (res) {
//            JSONObject houseParams = new JSONObject();
//            houseParams.put("roomNum",basicHousing.getFloor()+basicHousing.getDoorNumber());
//            houseParams.put("unitUuid",basicHousing.getUnitId());
//            houseParams.put("thirdPartyId",basicHousing.getId());
//            JSONObject result = thirdPartyClient.saveUpdateHousing(houseParams);
//            if (!"200".equals(result.getString("code"))){
//                throw new BusinessErrorException("同步第三方房屋失败");
//            }
            return ResultUtil.data(res, "保存成功");
        } else {
            return ResultUtil.error("保存失败");
        }
    }

    /**
     * 导出房屋数据
     * @param response
     * @param basicHousing
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, BasicHousing basicHousing) {
        Long time = System.currentTimeMillis();
        try {
            basicHousingService.download(basicHousing, response);
        } catch (Exception e) {
            e.printStackTrace();
//            logService.addErrorLog("导出异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }

    /**
     * 导出出租房屋数据
     * @param response
     * @param basicPerson
     */
    @PostMapping("/downloadRent")
    public void downloadRent(HttpServletResponse response, BasicPerson basicPerson) {
        Long time = System.currentTimeMillis();
        try {
            basicHousingService.downloadRent(basicPerson, response);
        } catch (Exception e) {
//            logService.addErrorLog("导出异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }

    /**
     * 根据楼栋寻找单元
     * @param buildArchiveId
     * @return
     */
    @GetMapping("getUnitByBuildArchiveId")
    public Result<Object> getUnitByBuildArchiveId(@RequestParam(name = "buildArchiveId") String buildArchiveId) {
        Long time = System.currentTimeMillis();
        try {
            List<Map<String, Object>> list = basicHousingService.getUnitByBuildArchiveId(buildArchiveId);
            return ResultUtil.data(list, "查询成功");
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 根据楼栋寻找单元
     * @param buildArchiveId
     * @param unit
     * @return
     */
    @GetMapping("getDoor")
    public Result<Object> getDoor(@RequestParam(name = "buildArchiveId") String buildArchiveId, @RequestParam(name = "unit") String unit) {
        Long time = System.currentTimeMillis();
        try {
            List<Map<String, Object>> list = basicHousingService.getDoor(buildArchiveId, unit);
            return ResultUtil.data(list, "查询成功");
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 获取真实存在房屋和人
     * @param buildArchiveId
     * @param unit
     * @return
     */
    @GetMapping("getRealOfHouseAndDoor")
    public Result<Object> getRealOfHouseAndDoor(@RequestParam(name = "buildArchiveId") String buildArchiveId, @RequestParam(name = "unit") String unit) {
        Long time = System.currentTimeMillis();
        try {
            List<Map<String, Object>> total = new ArrayList<>();
            List<Map<String, Object>> houseNums = basicHousingService.getRealHouse(buildArchiveId, unit);
            for (Map<String, Object> houseNum : houseNums) {
                total.add(houseNum);
            }
            List<Map<String, Object>> doors = basicHousingService.getRealDoor(buildArchiveId, unit);
            for (Map<String, Object> door : doors) {
                total.add(door);
            }
            return ResultUtil.data(total, "查询成功");
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 获取最大楼层数和所有楼层中最多门牌数、房屋的人数、真实存在房屋
     * @param buildArchiveId
     * @param unit
     * @return
     */
    @GetMapping("getMaxOfFloorAndDoor")
    public Result<Object> getMaxOfFloorAndDoor(@RequestParam(name = "buildArchiveId") String buildArchiveId, @RequestParam(name = "unit") String unit) {
        Long time = System.currentTimeMillis();
        try {
            List<Map<String, Object>> total = new ArrayList<>();
            List<Map<String, Object>> maxs = basicHousingService.getMaxOfFloorAndDoor(buildArchiveId, unit);
            for (Map<String, Object> max : maxs) {
                total.add(max);
            }
            List<Map<String, Object>> houseNums = basicHousingService.getHouseNum(buildArchiveId, unit);
            for (Map<String, Object> houseNum : houseNums) {
                total.add(houseNum);
            }
            List<Map<String, Object>> doors = basicHousingService.getDoor(buildArchiveId, unit);
            for (Map<String, Object> door : doors) {
                total.add(door);
            }
            return ResultUtil.data(total, "查询成功");
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 根据中间表获取所有租客
     * @param person
     * @param pageVo
     * @return
     */
    @GetMapping("getAllPersonByRela")
    public Result<Object> getAllPersonByRela(BasicPerson person, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            IPage<Map<String, Object>> pagePerson = basicHousingService.getAllPersonByRela(person, pageVo);
            if (pagePerson != null) {
                return ResultUtil.data(pagePerson, "查询成功");
            } else {
                return ResultUtil.error("查询失败");
            }
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 模糊查询全部房屋数据
     * @param basicHousing
     * @return
     */
    @GetMapping("queryHousing")
    public Result<Object> queryHousing(BasicHousing basicHousing) {
        Long time = System.currentTimeMillis();
        try {
            return basicHousingService.queryHousingManage(basicHousing);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 根据楼栋id查找房屋信息
     * @param buildArchiveId
     * @param personId
     * @param type
     * @return
     */
    @GetMapping("getRoomByBuildArchiveId")
    public Result<Object> getRoomByBuildArchiveId(String buildArchiveId, String personId, String type) {
        Long time = System.currentTimeMillis();
        try {
            List<BasicHousing> res = basicHousingService.getRoomByBuildArchiveId(buildArchiveId, personId, type);
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
     * 加载楼栋单元数
     * @param buildArchiveId
     * @return
     */
    @GetMapping("getUnits")
    public Result<Object> getUnits(String buildArchiveId) {
        Long time = System.currentTimeMillis();
        try {
            List<String> res = basicHousingService.getUnits(buildArchiveId);
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
     * 加载楼栋楼层数
     * @param buildArchiveId
     * @param unit
     * @return
     */
    @GetMapping("getFloors")
    public Result<Object> getFloors(String buildArchiveId, String unit) {
        Long time = System.currentTimeMillis();
        try {
            List<String> res = basicHousingService.getFloors(buildArchiveId, unit);
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
     * 加载门牌号
     * @param buildArchiveId
     * @param unit
     * @param floor
     * @return
     */
    @GetMapping("getDoorNumbers")
    public Result<Object> getDoorNumbers(String buildArchiveId, String unit, String floor) {
        Long time = System.currentTimeMillis();
        try {
            List<Map<String, Object>> res = basicHousingService.getDoorNumbers(buildArchiveId, unit, floor);
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
     * 查询房屋信息
     * @param personId
     * @return
     */
    @GetMapping("getHouseByPersonId")
    public Result<Object> getHouseByPersonId(String personId) {
        Long time = System.currentTimeMillis();
        try {
            List<BasicHousing> res = basicHousingService.getHouseByPersonId(personId);
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
     * 判断房屋是否重复
     *
     * @param basicHousing
     * @return
     */
    public boolean isHouseMore(BasicHousing basicHousing) {
        boolean flag = false;
        QueryWrapper<BasicHousing> queryWrapper = new QueryWrapper<>();
        if (basicHousing != null) {
            if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicHousing.getBuildArchiveId())) {
                queryWrapper.lambda().and(i -> i.like(BasicHousing::getBuildArchiveId, basicHousing.getBuildArchiveId()));
            }
            if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicHousing.getUnit())) {
                queryWrapper.lambda().and(i -> i.like(BasicHousing::getUnit, basicHousing.getUnit()));
            }
            if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicHousing.getFloor())) {
                queryWrapper.lambda().and(i -> i.like(BasicHousing::getFloor, basicHousing.getFloor()));
            }
            if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicHousing.getDoorNumber())) {
                queryWrapper.lambda().and(i -> i.like(BasicHousing::getDoorNumber, basicHousing.getDoorNumber()));
            }
            int count = basicHousingService.count(queryWrapper);
            if (count>0) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     *
     * @param str
     *         需要过滤的字符串
     * @return
     * @Description:过滤数字以外的字符
     */
    public static String filterUnNumber(String str) {
        // 只允数字
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        //替换与模式匹配的所有字符（即非数字的字符将被""替换）
        return m.replaceAll("").trim();

    }

    /**
     * 生成房屋代码
     *
     * @param basicHousing
     * @return
     */
    public String setHouseCode(BasicHousing basicHousing) {
        String houseCode = "";
        if (basicHousing != null) {
            if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicHousing.getBuildArchiveName())) {
                String number = filterUnNumber(basicHousing.getBuildArchiveName());
                if(StringUtils.isNotBlank(number)){
                    int i = Integer.parseInt(number);
                    if (i < 10) {
                        houseCode += "0" + i;
                    } else {
                        houseCode += i + "";
                    }
                }
            } else {
                return "";
            }
            if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicHousing.getUnit())) {
                int i = Integer.parseInt(basicHousing.getUnit());
                if (i < 10) {
                    houseCode += "0" + i;
                } else {
                    houseCode += i + "";
                }
            } else {
                return "";
            }
            if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicHousing.getFloor())) {
                int i = Integer.parseInt(basicHousing.getFloor());
                if (i < 10) {
                    houseCode += "0" + i;
                } else {
                    houseCode += i + "";
                }
            } else {
                return "";
            }
            if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicHousing.getDoorNumber())) {
                int i = Integer.parseInt(basicHousing.getDoorNumber());
                if (i < 10) {
                    houseCode += "0" + i;
                } else {
                    houseCode += i + "";
                }
            } else {
                return "";
            }
        }
        return houseCode;
    }


    /**
     * 根据人员Id查询房屋信息和楼盘信息
     * @param personId
     * @return
     */
    @GetMapping("getHouseAndArchivesListByPersonId")
    public Result<Object> getHouseAndArchivesListByPersonId(String personId) {
        Long time = System.currentTimeMillis();
        try {
            List<BasicHousing> res = basicHousingService.getHouseByPersonId(personId);
            for (BasicHousing re : res) {
                TBuildingArchives byId = itBuildingArchivesService.getById(re.getBuildArchiveId());
                re.setBuildingArchives(byId);
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
     * 获取房屋结构图左侧树菜单
     *
     * @return
     */
    @GetMapping("getHouseTree")
    public Result<Object> getHouseTree() {
        //用户数据权限
        UserDto user = securityUtil.getCurrUser();
        Map<String,Object> map = null;
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                map = new HashMap<>();
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){// 街道
                    map.put("streetId",deptId);
                }else if (attribute == 2) {// 社区
                    map.put("communityId",deptId);
                }else if (attribute == 3) {// 网格
                    map.put("gridId",deptId);
                }
            }
        }
        return ResultUtil.data(basicHousingService.selectHouseTree(map), "查询成功");
    }

    /**
     * 获取真实存在房屋和人
     * @param buildArchiveId
     * @param unit
     * @return
     */
    @GetMapping("getRealHouseAndDoor")
    public Result<Object> getRealHouseAndDoor(@RequestParam(name = "buildArchiveId") String buildArchiveId, @RequestParam(name = "unit") String unit) {
        Long time = System.currentTimeMillis();
        try {

            Map<String, Object> realDoorAndFloor = basicHousingService.getRealDoorAndFloor(buildArchiveId, unit);
            return ResultUtil.data(realDoorAndFloor, "查询成功");
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }
    /**
     * 获取真实存在房屋和人
     * @param buildArchiveId
     * @param unit
     * @return
     */
    @GetMapping("getRealDoorAndPerson")
    public Result<Object> getRealDoorAndPerson(@RequestParam(name = "buildArchiveId") String buildArchiveId, @RequestParam(name = "unit") String unit) {
        Long time = System.currentTimeMillis();
        try {

            Map<String, Object> realDoorAndFloor = basicHousingService.getRealDoorAndPerson(buildArchiveId, unit);
            return ResultUtil.data(realDoorAndFloor, "查询成功");
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * Excel导入信息数据
     *
     * @param file
     * @return
     */
    @PostMapping("/importExcel")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> importExcel(@RequestBody MultipartFile file) {
        Long time = System.currentTimeMillis();
        try {
            return basicHousingService.importExcel(file);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.error("导入异常" + e.getMessage());
        }
    }
}
