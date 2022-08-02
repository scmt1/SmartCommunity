package me.zhengjie.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.system.service.UserService;
import me.zhengjie.system.service.dto.UserDto;
import me.zhengjie.util.BusinessErrorException;

import me.zhengjie.entity.BasicHousingEstate;
import me.zhengjie.entity.RelaPersonHouse;
import me.zhengjie.entity.TBuildingArchives;
import me.zhengjie.service.IBasicHousingEstateService;
import me.zhengjie.service.IRelaPersonHouseService;

import me.zhengjie.service.ITBuildingArchivesService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 小区信息数据接口
 *
 * @author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/basicHousingEstate")
public class BasicHousingEstateController {

    private final IBasicHousingEstateService basicHousingEstateService;

    private final IRelaPersonHouseService relaPersonHouseService;

    private final ITBuildingArchivesService tBuildingArchivesService;

    private final SecurityUtil securityUtil;

    /**
     * 新增小区信息数据
     * @param basicHousingEstate
     * @return
     */
    @PostMapping("addBasicHousingEstate")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> addBasicHousingEstate(@RequestBody BasicHousingEstate basicHousingEstate) {
        Long time = System.currentTimeMillis();


            QueryWrapper<BasicHousingEstate> basicHouseQueryWrapper = new QueryWrapper<>();
            basicHouseQueryWrapper.lambda().and(i -> i.eq(BasicHousingEstate::getName, basicHousingEstate.getName()));
            basicHouseQueryWrapper.lambda().and(i -> i.ne(BasicHousingEstate::getId, basicHousingEstate.getId()));
            basicHouseQueryWrapper.lambda().and(j -> j.eq(BasicHousingEstate::getIsDelete, 0));
            int count = basicHousingEstateService.count(basicHouseQueryWrapper);
            if (count > 0) {
                return ResultUtil.error("小区名重复");
            }
            basicHousingEstate.setIsDelete(0);
            basicHousingEstate.setCreateTime(new Timestamp(System.currentTimeMillis()));

            String id = UUID.randomUUID().toString().replaceAll("-", "");
            basicHousingEstate.setId(id);
            boolean res = basicHousingEstateService.save(basicHousingEstate);
            if (res) {
//				HttpClient httpClient = new HttpClient();
                JSONObject jsonObjectRes = new JSONObject();
                jsonObjectRes.put("companyUuid", basicHousingEstate.getPropertyNameId());
                jsonObjectRes.put("gridUid", id);
                jsonObjectRes.put("gridId", basicHousingEstate.getGridId());
                jsonObjectRes.put("name", basicHousingEstate.getName());
                jsonObjectRes.put("buildYear", "");
                jsonObjectRes.put("provinceCityDistrict", basicHousingEstate.getGrid());
                jsonObjectRes.put("adress", basicHousingEstate.getAddress());
                jsonObjectRes.put("phone", basicHousingEstate.getPropertyPhone());
                //todo 合并代码后直接调用方法
                //httpClient.exchange(PropertyClient.synchroProperty, HttpMethod.POST,jsonObjectRes ,restTemplate);
//                JSONObject jsonObject = entityClient.synchroProperty(jsonObjectRes);
//                if (jsonObject.containsKey("code") && jsonObject.get("code").toString().equals("200")) {
//                } else {
//                    throw new RuntimeException("同步异常");
//                }
                JSONObject dnkParams = new JSONObject();
                dnkParams.put("communityName",basicHousingEstate.getName());
                dnkParams.put("thirdPartyId",basicHousingEstate.getId());
//                JSONObject result = thirdPartyClient.addOrUpdateCommunity(dnkParams);
//                if ("200".equals(result.getString("code"))){
                    return ResultUtil.data(res, "保存成功");
//                } else {
//                    throw new BusinessErrorException("同步失败");
//                }
            } else {
                return ResultUtil.error("保存失败");
            }

    }

    /**
     * 根据主键来删除小区信息数据
     * @param map
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("deleteBasicHousingEstate")
    public Result<Object> deleteBasicHousingEstate(@RequestBody Map<String, Object> map) {
        if (map == null || map.size() == 0 || !map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        List<String> ids = (List<String>) map.get("ids");

        if (ids == null || ids.size() == 0) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        QueryWrapper<TBuildingArchives> basicHouseQueryWrapper = new QueryWrapper<>();
        basicHouseQueryWrapper.lambda().and(i -> i.in(TBuildingArchives::getHousingEstate, ids));
        basicHouseQueryWrapper.lambda().and(j -> j.eq(TBuildingArchives::getIsDelete, 0));
        int count = tBuildingArchivesService.count(basicHouseQueryWrapper);
        if (count > 0) {
            return ResultUtil.error("您删除的小区名下 拥有楼栋数据，您不能删除此小区，请先删除小区的楼栋");
        }

        boolean res = basicHousingEstateService.removeByIds(ids);
        if (res) {
            for (String id : ids) {
//                JSONObject jsonObject = entityClient.deleteByGridUuid(id);
//                if (!jsonObject.containsKey("code") || !jsonObject.get("code").toString().equals("200")) {
//                    throw new RuntimeException("同步异常");
//                }
//                JSONObject dnkParams = new JSONObject();
//                dnkParams.put("uuid",id);
//                thirdPartyClient.deleteCommunity(dnkParams);
            }
            return ResultUtil.data(res, "删除成功");
        } else {
            return ResultUtil.error( "删除失败");
        }
    }

    /**
     * 根据主键来获取小区信息数据
     * @param id
     * @return
     */
    @GetMapping("getBasicHousingEstate")
    public Result<Object> getBasicHousingEstate(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            BasicHousingEstate res = basicHousingEstateService.getBasicHousingEstateById(id);

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
     * 分页查询小区信息数据
     * @param basicHousingEstate
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryBasicHousingEstateList")
    public Result<Object> queryBasicHousingEstateList(BasicHousingEstate basicHousingEstate, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            //用户数据权限
            UserDto user = securityUtil.getCurrUser();
            if (user.getPower() != null){
                if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                    String deptId = user.getPower().getDeptId();
                    Integer attribute = user.getPower().getAttribute();
                    if (attribute == 1){// 街道
                        basicHousingEstate.setStreetId(deptId);
                    }else if (attribute == 2) {// 社区
                        basicHousingEstate.setCommunityId(deptId);
                    }else if (attribute == 3) {// 网格
                        basicHousingEstate.setGridId(deptId);
                    }
                }
            }
            return basicHousingEstateService.queryBasicHousingEstateListByPage(basicHousingEstate, searchVo, pageVo);
        } catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 更新小区信息数据
     * @param basicHousingEstate
     * @return
     */
    @PostMapping("updateBasicHousingEstate")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> updateBasicHousingEstate(@RequestBody BasicHousingEstate basicHousingEstate) {
        if (StringUtils.isBlank(basicHousingEstate.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            QueryWrapper<BasicHousingEstate> basicHouseQueryWrapper = new QueryWrapper<>();
            basicHouseQueryWrapper.lambda().and(i -> i.eq(BasicHousingEstate::getName, basicHousingEstate.getName()));
            basicHouseQueryWrapper.lambda().and(i -> i.ne(BasicHousingEstate::getId, basicHousingEstate.getId()));
            basicHouseQueryWrapper.lambda().and(j -> j.eq(BasicHousingEstate::getIsDelete, 0));
            int count = basicHousingEstateService.count(basicHouseQueryWrapper);
            if (count > 0) {
                return ResultUtil.error("小区名重复");
            }
            basicHousingEstate.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = basicHousingEstateService.updateById(basicHousingEstate);
            if (res) {
                JSONObject jsonObjectRes = new JSONObject();
                jsonObjectRes.put("companyUuid", basicHousingEstate.getPropertyNameId());
                jsonObjectRes.put("gridUid", basicHousingEstate.getId());
                jsonObjectRes.put("gridId", basicHousingEstate.getGridId());
                jsonObjectRes.put("name", basicHousingEstate.getName());
                jsonObjectRes.put("buildYear", null);
                jsonObjectRes.put("provinceCityDistrict", basicHousingEstate.getGrid());
                jsonObjectRes.put("address", basicHousingEstate.getAddress());
                jsonObjectRes.put("phone", basicHousingEstate.getPropertyPhone());


                //httpClient.exchange(PropertyClient.synchroUpdateProperty, HttpMethod.POST,jsonObjectRes ,restTemplate);
//                JSONObject jsonObject = entityClient.synchroUpdateProperty(jsonObjectRes);
//                if (jsonObject.containsKey("code") && jsonObject.get("code").toString().equals("200")) {
//                } else {
//                    throw new RuntimeException("同步异常");
//                }
                JSONObject dnkParams = new JSONObject();
                dnkParams.put("communityName",basicHousingEstate.getName());
                dnkParams.put("thirdPartyId",basicHousingEstate.getId());
//                JSONObject result = thirdPartyClient.addOrUpdateCommunity(dnkParams);
//                if ("200".equals(result.getString("code"))){
                    return ResultUtil.data(res, "保存成功");
//                } else {
//                    throw new BusinessErrorException("同步失败");
//                }
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

//			logService.addErrorLog("保存异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 导出小区信息数据
     * @param response
     * @param basicHousingEstate
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, BasicHousingEstate basicHousingEstate) {
        Long time = System.currentTimeMillis();
        try {
            basicHousingEstateService.download(basicHousingEstate, response);
        } catch (Exception e) {
//			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
        }
    }

    /**
     * 模糊查询全部小区信息数据
     * @param basicHousingEstate
     * @param flag 是否限制权限 true限制权限 false查询所有
     * @return
     */
    @GetMapping("queryAllList")
    public Result<Object> queryAllList(BasicHousingEstate basicHousingEstate,
                                       @RequestParam(name = "flag",required = false,defaultValue = "true")Boolean flag) {
        Long time = System.currentTimeMillis();
        try {
            if (flag != null){
                if (flag){
                    UserDto user = securityUtil.getCurrUser();
                    if (user.getPower() != null){
                        if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                            String deptId = user.getPower().getDeptId();
                            Integer attribute = user.getPower().getAttribute();
                            if (attribute == 1){// 街道
                                basicHousingEstate.setStreetId(deptId);
                            }else if (attribute == 2) {// 社区
                                basicHousingEstate.setCommunityId(deptId);
                            }else if (attribute == 3) {// 网格
                                basicHousingEstate.setGridId(deptId);
                            }
                        }
                    }
                }
            }
            return ResultUtil.data(basicHousingEstateService.queryAllList(basicHousingEstate));
        } catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * Excel导入小区信息数据
     * @param file
     * @return
     */
    @PostMapping("/importExcel")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> importExcel(@RequestBody MultipartFile file) {
        //@RequestParam("file") MultipartFile file) throws IOException, Exception
        Long time = System.currentTimeMillis();
        try {
            return basicHousingEstateService.importExcel(file);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.error("保存异常" + e.getMessage());
        }
    }

    @PostMapping("/loadAllBySelectFromGrid")
    @ApiOperation(value = "获取所有楼盘", notes = "用户下拉选择的列表，「包含id，name」")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "", name = "", value = "", required = true, dataType = "")
    })
    public Result<Object> loadAllBySelectFromGrid(@RequestBody JSONObject query) {
        //用户数据权限
        UserDto user = securityUtil.getCurrUser();
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){//街道
                    query.put("streetId",deptId);
                }else if (attribute == 2){//社区
                    query.put("communityId",deptId);
                }else if (attribute == 3){//网格
                    query.put("gridId",deptId);
                }
            }
        }
        List<Map<String, Object>> selectList = basicHousingEstateService.loadAllBySelectFromGrid(query);
        return ResultUtil.data(selectList,"" );
    }
}
