package me.zhengjie.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.*;
import me.zhengjie.service.*;
import me.zhengjie.system.service.UserService;
import me.zhengjie.system.service.dto.UserDto;
import me.zhengjie.util.BusinessErrorException;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 建筑设施档案数据接口
 * @author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/tBuildingArchives")
public class TBuildingArchivesController {
   
    private final ITBuildingArchivesService tBuildingArchivesService;
   
    private final IBasicPersonService basicPersonService;
   
    private final IBasicHousingService basicHousingService;

    private final IBasicGridsService basicGridsService;
   
    private final IBasicHousingEstateService basicHousingEstateService;

    private final IBasicUnitService basicUnitService;

    private final SecurityUtil securityUtil;

    /**
     * 新增建筑设施档案数据
     * @param tBuildingArchives
     * @return
     */
    @PostMapping("addTBuildingArchives")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> addTBuildingArchives(@RequestBody TBuildingArchives tBuildingArchives) {
        Long time = System.currentTimeMillis();
        //判断名称重复
        QueryWrapper<TBuildingArchives> buildingArchivesQueryWrapper = new QueryWrapper<>();
        buildingArchivesQueryWrapper.lambda().and(i -> i.eq(TBuildingArchives::getHousingEstate, tBuildingArchives.getHousingEstate()));
        buildingArchivesQueryWrapper.lambda().and(i -> i.eq(TBuildingArchives::getBuildingName, tBuildingArchives.getBuildingName()));
        buildingArchivesQueryWrapper.lambda().and(i -> i.eq(TBuildingArchives::getIsDelete, 0));
        int count  = tBuildingArchivesService .count(buildingArchivesQueryWrapper);
        if(count>0){
            return ResultUtil.error("建筑名称重复");
        }
        List<BasicUnit> unitList = tBuildingArchives.getUnitList();
        boolean find = false;
        if (unitList != null) {
            for(int i = 0; i < unitList.size(); i++){
                if(unitList.get(i).getName()==0){
                    return ResultUtil.error("单元名有为空的，请检查输入的单元名是否为空或者为0!!！");
                }
                for(int j = i + 1; j < unitList.size(); j++){
                    if(unitList.get(i).getName().equals(unitList.get(j).getName())){
                        find = true;
                        break;
                    }
                }
                if (find) break;
            }
        }
        if(find){
            return ResultUtil.error("单元名有重复的，请检查输入的单元名有重复的!!！");
        }
        tBuildingArchives.setIsDelete(0);
        tBuildingArchives.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String id = UUID.randomUUID().toString().replaceAll("-", "");

        tBuildingArchives.setId(id);
        boolean res = tBuildingArchivesService.insert(tBuildingArchives);
        if (res) {
            JSONObject dnkBuildingParams = new JSONObject();
            dnkBuildingParams.put("communityUuid",tBuildingArchives.getHousingEstate());
            dnkBuildingParams.put("buildingName",tBuildingArchives.getBuildingName()+"栋");
            dnkBuildingParams.put("buildingCode",tBuildingArchives.getBuildingName());
            dnkBuildingParams.put("thirdPartyId",tBuildingArchives.getId());
//            JSONObject dnkResponse = thirdPartyClient.saveUpdateBuilding(dnkBuildingParams);
//            if (!"200".equals(dnkResponse.getString("code"))){
//                throw new BusinessErrorException("同步新增楼栋失败");
//            }
            List<JSONObject> list = new ArrayList<>();
            if(unitList!=null && unitList.size()>0){
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = formatter.format(tBuildingArchives.getBuildingYear());
                for (int i = 0; i < unitList.size(); i++) {
                    BasicUnit basicUnit = unitList.get(i);
                    basicUnit.setBuildArchiveId(id);
                    basicUnit.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    basicUnit.setIsDelete(0);
                    String basicUnitId="";
                    synchronized (basicUnitId) {
                        basicUnitId = UUID.randomUUID().toString().replaceAll("-", "");
                    }
                    basicUnit.setId(basicUnitId);
                    basicUnitService.save(basicUnit);

                    //新增单元
                    JSONObject dnkUnitingParams = new JSONObject();
                    dnkUnitingParams.put("buildingUuid",tBuildingArchives.getId());
                    dnkUnitingParams.put("unitName",basicUnit.getName());
                    dnkUnitingParams.put("unitCode",i+1);
                    dnkUnitingParams.put("thirdPartyId",basicUnit.getId());
//                    JSONObject dnkUnitResponse = thirdPartyClient.saveUpdateUnit(dnkUnitingParams);
//                    if (!"200".equals(dnkUnitResponse.getString("code"))){
//                        throw new BusinessErrorException("同步新增单元失败");
//                    }

                    JSONObject jsonObjectRes = new JSONObject();

                    jsonObjectRes.put("propertyUuid",tBuildingArchives.getHousingEstate());
                    jsonObjectRes.put("buildingCode",tBuildingArchives.getBuildingName());
                    jsonObjectRes.put("unitCode",basicUnit.getName().toString());
                    jsonObjectRes.put("buildYear",dateString);
                    jsonObjectRes.put("elevatorNumber",tBuildingArchives.getElevatorNumber());
                    jsonObjectRes.put("floor",basicUnit.getFloorNumber());
                    jsonObjectRes.put("uuid",basicUnitId);
                    jsonObjectRes.put("type",1);
                    list.add(jsonObjectRes);

                }
                //todo 合并代码后直接调用方法
//                JSONObject jsonObject = entityClient.syncBuilding(list);
//                if(jsonObject.containsKey("code") && jsonObject.get("code").toString().equals("200")){
//                }
//                else {
//                    throw new RuntimeException("同步异常");
//                }
            }
            return ResultUtil.data(res, "保存成功");
        } else {
            return ResultUtil.error("保存失败");
        }
    }

    /**
     * 根据主键来删除建筑设施档案数据
     * @param map
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("deleteTBuildingArchives")
    public Result<Object> deleteTBuildingArchives(@RequestBody Map<String ,Object> map) {
        if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        List<String> ids = (List<String>) map.get("ids");
        if (ids == null || ids.size() == 0 ) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        QueryWrapper<BasicHousing> basicHouseQueryWrapper = new QueryWrapper<>();
        basicHouseQueryWrapper.lambda().and(i -> i.in(BasicHousing::getBuildArchiveId, ids));
        basicHouseQueryWrapper.lambda().and(i -> i.in(BasicHousing::getIsDelete, 0));
        int count  = basicHousingService .count(basicHouseQueryWrapper);
        if(count>0){
            return ResultUtil.error("您删除的建筑设施 拥有房屋，您不能删除此建筑设施，请先删除建筑设施下的房屋");
        }

        boolean res = tBuildingArchivesService.removeByIds(ids);
        if (res) {
            List<JSONObject> list = new ArrayList<>();
            for(String id:ids){
                List<BasicUnit> basicUnits = basicUnitService.queryBasicUnitListByArchiveId(id);
                for(BasicUnit basicUnit:basicUnits){
                    JSONObject jsonObjectRes = new JSONObject();
                    jsonObjectRes.put("uuid",basicUnit.getId());
                    jsonObjectRes.put("type", 3);
                    list.add(jsonObjectRes);
                    //删除单元
                    JSONObject dnkUnitParams = new JSONObject();
                    dnkUnitParams.put("uuid",basicUnit.getId());
                   // thirdPartyClient.deleteUnit(dnkUnitParams);
                }
//                JSONObject jsonObject = entityClient.syncBuilding(list);
//                if(!jsonObject.containsKey("code") || !jsonObject.get("code").toString().equals("200")){
//                    throw new RuntimeException("同步异常");
//                }
//                //删除楼栋
//                JSONObject dnkBuildingParams = new JSONObject();
//                dnkBuildingParams.put("uuid",id);
//                thirdPartyClient.deleteBuilding(dnkBuildingParams);
            }
            return ResultUtil.data(res, "删除成功");
        } else {
            return ResultUtil.error( "删除失败");
        }
    }

    /**
     * 根据主键来获取建筑设施档案数据
     * @param id
     * @return
     */
    @GetMapping("getTBuildingArchives")
    public Result<Object> getTBuildingArchives(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            TBuildingArchives res = tBuildingArchivesService.getById(id);
            if (res != null) {
                BasicGrids byId = basicGridsService.getById(res.getGrid());

                if (byId != null) {
                    res.setGridName(byId.getName());
                }
                BasicHousingEstate byId1 = basicHousingEstateService.getBasicHousingEstateById(res.getHousingEstate());
                if (byId1 != null) {
                    res.setBasicHousingEstate(byId1);
                }

                List<BasicUnit> unitList = basicUnitService.queryBasicUnitListByArchiveId(id);
                res.setUnitList(unitList);
//                List<Map<String, Object>> units = new ArrayList<Map<String, Object>>();
//                for(int i=0; i<Integer.parseInt(res.getUnit()) ;i++){
//                    Map<String, Object>  item= new HashMap<>();
//                    item.put("name",(i+1)+"单元");
//                    item.put("number",Integer.parseInt(res.getFloor()) *Integer.parseInt(res.getDoorNumber()) +"户");
//                    units.add(item);
//                }
//
//                res.setUnits(units);


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
     * 分页查询建筑设施档案数据
     * @param tBuildingArchives
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryTBuildingArchivesList")
    public Result<Object> queryTBuildingArchivesList(TBuildingArchives tBuildingArchives, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            //用户数据权限
            UserDto user = securityUtil.getCurrUser();
            if (user.getPower() != null){
                if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                    String deptId = user.getPower().getDeptId();
                    Integer attribute = user.getPower().getAttribute();
                    if (attribute == 1){// 街道
                        tBuildingArchives.setStreetId(deptId);
                    }else if (attribute == 2) {// 社区
                        tBuildingArchives.setCommunityId(deptId);
                    }else if (attribute == 3) {// 网格
                        tBuildingArchives.setGrid(deptId);
                    }
                }
            }
            Page<TBuildingArchives> tBuildingArchivesPage = tBuildingArchivesService.queryTBuildingArchivesListByPage(tBuildingArchives, searchVo, pageVo);
            return ResultUtil.data(tBuildingArchivesPage, "查询成功");
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 分页查询建筑设施档案数据
     * @param tBuildingArchives
     * @param key
     * @param pageVo
     * @return
     */
    @GetMapping("queryTBuildingArchivesListByAnyWayWhere")
    public Result<Object> queryTBuildingArchivesListByAnyWayWhere(TBuildingArchives tBuildingArchives, String key, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            //用户数据权限
            UserDto user = securityUtil.getCurrUser();
            if (user.getPower() != null){
                if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                    String deptId = user.getPower().getDeptId();
                    Integer attribute = user.getPower().getAttribute();
                    if (attribute == 1){// 街道
                        tBuildingArchives.setStreetId(deptId);
                    }else if (attribute == 2) {// 社区
                        tBuildingArchives.setCommunityId(deptId);
                    }else if (attribute == 3) {// 网格
                        tBuildingArchives.setGrid(deptId);
                    }
                }
            }
            return tBuildingArchivesService.queryTBuildingArchivesListByAnyWayWhere(tBuildingArchives, key, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 查询建筑设施档案数据
     * @param tBuildingArchives
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryTBuildingArchivesAll")
    public Result<Object> queryTBuildingArchivesAll(TBuildingArchives tBuildingArchives, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            List<TBuildingArchives> list = tBuildingArchivesService.queryTBuildingArchivesAll(tBuildingArchives);
            return ResultUtil.data(list, "查询成功");
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 更新建筑设施档案数据
     * @param tBuildingArchives
     * @return
     */
    @PostMapping("updateTBuildingArchives")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> updateTBuildingArchives(@RequestBody TBuildingArchives tBuildingArchives) {
        if (StringUtils.isBlank(tBuildingArchives.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        System.out.println("入参：" + tBuildingArchives.toString());
        Long time = System.currentTimeMillis();

//            if (StringUtils.isNotBlank(tBuildingArchives.getImgPath()) && tBuildingArchives.getImageIsUpdate()) {
//                //base64 转文件
//                MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(tBuildingArchives.getImgPath());
//                //文件存储在nginx代理路径下
//                String fileName = UploadFile.uploadFile(imgFile);
//                tBuildingArchives.setImgPath(fileName);
//            }
            //判断名称重复
            QueryWrapper<TBuildingArchives> buildingArchivesQueryWrapper = new QueryWrapper<>();
            buildingArchivesQueryWrapper.lambda().and(i -> i.eq(TBuildingArchives::getHousingEstate, tBuildingArchives.getHousingEstate()));
            buildingArchivesQueryWrapper.lambda().and(i -> i.eq(TBuildingArchives::getBuildingName, tBuildingArchives.getBuildingName()));
            buildingArchivesQueryWrapper.lambda().and(i -> i.ne(TBuildingArchives::getId, tBuildingArchives.getId()));
            buildingArchivesQueryWrapper.lambda().and(i -> i.eq(TBuildingArchives::getIsDelete, 0));
            int countName  = tBuildingArchivesService .count(buildingArchivesQueryWrapper);
            if(countName>0){
                return ResultUtil.error("你选择的小区已有"+tBuildingArchives.getBuildingName()+"号楼");
            }
            QueryWrapper<BasicUnit> basicUnitQueryWrapperOld = new QueryWrapper<>();
            basicUnitQueryWrapperOld.lambda().and(i -> i.eq(BasicUnit::getBuildArchiveId, tBuildingArchives.getId()));
            List<BasicUnit> unitListOld = basicUnitService.list(basicUnitQueryWrapperOld);
            List<BasicUnit> unitList = tBuildingArchives.getUnitList();
            boolean find = false;
            if (unitList != null && unitList.size() >0) {
                for(int i = 0; i < unitList.size(); i++){
                    if(unitList.get(i).getName()==0){
                        return ResultUtil.error("单元名有为空的，请检查输入的单元名是否为空或者为0!!！");
                    }
                    for(int j = i + 1; j < unitList.size(); j++){
                        if(unitList.get(i).getName().equals(unitList.get(j).getName())){
                            find = true;
                            break;
                        }
                    }
                    if (find){
                        break;
                    }
                    String id= unitList.get(i).getId();
                    if(StringUtils.isNotBlank(unitList.get(i).getId())){
                        unitListOld = unitListOld.stream()
                                .filter((BasicUnit s) -> !s.getId().equals(id))
                                .collect(Collectors.toList());
                    }

                }
            }
            if(find){
                return ResultUtil.error("单元名有重复的，请检查输入的单元名是否有重复的!!！");
            }

            tBuildingArchives.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = tBuildingArchivesService.updateById(tBuildingArchives);
            if (res) {
                // 修改楼栋
                JSONObject dnkBuildingParams = new JSONObject();
                dnkBuildingParams.put("communityUuid",tBuildingArchives.getHousingEstate());
                dnkBuildingParams.put("buildingName",tBuildingArchives.getBuildingName()+"栋");
                dnkBuildingParams.put("buildingCode",tBuildingArchives.getBuildingName());
                dnkBuildingParams.put("thirdPartyId",tBuildingArchives.getId());
//                JSONObject dnkResponse = thirdPartyClient.saveUpdateBuilding(dnkBuildingParams);
//                if (!"200".equals(dnkResponse.getString("code"))){
//                    throw new BusinessErrorException("同步失败");
//                }
                if(tBuildingArchives.getIsUpdateLocation() == 1) {//如果不是列表修改坐标，才更新单元
                    if (unitList != null && unitList.size()>0) {
                        List<JSONObject> list = new ArrayList<>();
                        //先处理删除的
                        if(unitListOld.size()>0){

                            for (int delI = 0; delI < unitListOld.size(); delI++) {
                                QueryWrapper<BasicHousing> basicHouseQueryWrapper = new QueryWrapper<>();
                                String delID = unitListOld.get(delI).getId();
                                basicHouseQueryWrapper.lambda().and(i -> i.eq(BasicHousing::getUnitId, delID));
                                basicHouseQueryWrapper.lambda().and(i -> i.eq(BasicHousing::getIsDelete, 0));
                                int count  = basicHousingService .count(basicHouseQueryWrapper);
                                if(count>0){
                                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                    return ResultUtil.error("您删除的"+unitList.get(delI).getName()+"单元 拥有房屋，您不能删除此单元，请先改单元下的房屋");
                                }
                                boolean b = basicUnitService.removeById(delID);
                                if(b){
                                    JSONObject dnkUnitParams = new JSONObject();
                                    dnkUnitParams.put("uuid",delID);
                                   // thirdPartyClient.deleteUnit(dnkUnitParams);
                                    JSONObject jsonObjectRes = new JSONObject();
                                    jsonObjectRes.put("uuid", delID);
                                    jsonObjectRes.put("type", 3);
                                    list.add(jsonObjectRes);
                                }
                                else{
                                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                    return ResultUtil.error("删除"+unitList.get(delI).getName()+"单元时失败 ");

                                }

                            }

                        }
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String dateString = formatter.format(tBuildingArchives.getBuildingYear());
                        //更新和删除
                        for (int i = 0; i < unitList.size(); i++) {
                            String basicUnitId="";
                            synchronized (basicUnitId) {
                                basicUnitId = UUID.randomUUID().toString().replaceAll("-", "");
                            }
                            BasicUnit basicUnit = unitList.get(i);
//                            HttpClient httpClient = new HttpClient();
                            JSONObject jsonObjectRes = new JSONObject();
                            if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(basicUnit.getId()) ){
                                basicUnit.setId(basicUnitId);
                                jsonObjectRes.put("type",1);
                                basicUnit.setBuildArchiveId(tBuildingArchives.getId());
                                basicUnit.setCreateTime(new Timestamp(System.currentTimeMillis()));
                                basicUnit.setIsDelete(0);
                            }
                            else{
                                basicUnit.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                                jsonObjectRes.put("type",2);
                            }
//                            basicUnit.setId(basicUnitId);
//                            basicUnitService.saveOrUpdate(basicUnit);

                            jsonObjectRes.put("propertyUuid", tBuildingArchives.getHousingEstate());
                            jsonObjectRes.put("buildingCode", tBuildingArchives.getBuildingName());
                            jsonObjectRes.put("unitCode", basicUnit.getName());
                            jsonObjectRes.put("buildYear", dateString);
                            jsonObjectRes.put("elevatorNumber", tBuildingArchives.getElevatorNumber());
                            jsonObjectRes.put("floor", basicUnit.getFloorNumber());
                            jsonObjectRes.put("uuid", basicUnit.getId());
                            list.add(jsonObjectRes);
                        }

                        basicUnitService.saveOrUpdateBatch(unitList);
                        int num = 0;
                        for (BasicUnit basicUnit : unitList) {
                            //新增单元
                            JSONObject dnkUnitingParams = new JSONObject();
                            dnkUnitingParams.put("buildingUuid",tBuildingArchives.getId());
                            dnkUnitingParams.put("unitName",basicUnit.getName());
                            dnkUnitingParams.put("unitCode",num+1);
                            dnkUnitingParams.put("thirdPartyId",basicUnit.getId());
//                            JSONObject dnkUnitResponse = thirdPartyClient.saveUpdateUnit(dnkUnitingParams);
//                            if (!"200".equals(dnkUnitResponse.getString("code"))){
//                                throw new BusinessErrorException("同步失败");
//                            }
                            num ++;
                        }

                        //todo 合并代码后直接调用方法
                        //httpClient.exchange(PropertyClient.initSyncBuilding, HttpMethod.POST,jsonObjectRes ,restTemplate);
//                        System.out.println("请输入一个整数：" + list.toString());
//                        JSONObject jsonObject = entityClient.syncBuilding(list);
//                        if (jsonObject.containsKey("code") && jsonObject.get("code").toString().equals("200")) {
//                        } else {
//                            throw new RuntimeException("同步异常");
//                        }
                    }
                }

                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
    }

    /**
     * 导出建筑设施档案数据
     * @param response
     * @param tBuildingArchives
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, TBuildingArchives tBuildingArchives) {
        Long time = System.currentTimeMillis();
        try {
            tBuildingArchivesService.download(tBuildingArchives, response);
        } catch (Exception e) {
            e.printStackTrace();
//            logService.addErrorLog("导出异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }

    /**
     * 统计各人口类型数据和房屋类型数据
     * @param buildingArchiveId
     * @return
     */
    @GetMapping("statistics")
    public Result<Object> statisticsPersonType(@RequestParam(name = "buildingArchiveId") String buildingArchiveId) {
        Long time = System.currentTimeMillis();
        try {
            List<Object> total = new ArrayList<>();
            List<Map<String, Object>> personType = basicPersonService.statisticsPersonType(buildingArchiveId);
            for (Object person : personType) {
                total.add(person);
            }
            List<Map<String, Object>> housingType = basicHousingService.statisticsHousingType(buildingArchiveId);
            for (Object housing : housingType) {
                total.add(housing);
            }
            return ResultUtil.data(total, "统计成功");
        } catch (Exception e) {
//            logService.addErrorLog("统计异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("统计异常:" + e.getMessage());
        }
    }

    /**
     * 查询最大的单元数、楼层数和层户数
     * @param id
     * @return
     */
    @GetMapping("getMax")
    public Result<Object> getMaxOfFloorAndDoorAndUnit(@RequestParam(name = "id") String id) {
        Long time = System.currentTimeMillis();
        try {
            List<Map<String, Object>> max = tBuildingArchivesService.getMaxOfFloorAndDoorAndUnit(id);
            return ResultUtil.data(max, "查询成功");
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 查询最大的单元数
     * @param id
     * @return
     */
    @GetMapping("getMaxUnit")
    public Result<Object> getMaxUnit(@RequestParam(name = "id") String id) {
        Long time = System.currentTimeMillis();
        try {
            List<Map<String, Object>> max = basicUnitService.getMaxUnit(id);
            return ResultUtil.data(max, "查询成功");
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 查询单元中楼层数和层户数
     * @param id
     * @param name
     * @return
     */
    @GetMapping("getFloorAndDoor")
    public Result<Object> getFloorAndDoor(@RequestParam(name = "id") String id,@RequestParam(name = "name")Integer name) {
        Long time = System.currentTimeMillis();
        try {
            List<Map<String, Object>> list = basicUnitService.getFloorAndDoor(id,name);
            return ResultUtil.data(list, "查询成功");
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 查询除当前标绘面以外的所有数据
     * @param id
     * @return
     */
    @GetMapping("getOtherPolygonData")
    public Result<Object> getOtherPolygonData(String id) {
        try {
            List<Map<String,Object>> max = tBuildingArchivesService.getOtherPolygonData(id);
            return ResultUtil.data(max, "查询成功");
        } catch (Exception e) {
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
            return tBuildingArchivesService.importExcel(file);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.error("导入异常" + e.getMessage());
        }
    }

}
