package me.zhengjie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicGrids;
import me.zhengjie.entity.BasicHousingEstate;
import me.zhengjie.entity.RelaGridsPersonGrids;
import me.zhengjie.service.IBasicGridsService;
import me.zhengjie.service.IBasicHousingEstateService;
import me.zhengjie.service.IRelaGridsPersonGridsService;
import me.zhengjie.service.LogService;
import me.zhengjie.system.service.dto.UserDto;
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
 * 网格数据接口
 *
 * @author
 **/
@RestController
@RequestMapping("/api/basicGrids")
@AllArgsConstructor
public class BasicGridsController {

    private final IBasicGridsService basicGridsService;

    private final IBasicHousingEstateService basicHousingEstateService;

    private final IRelaGridsPersonGridsService relaGridsPersonGridsService;

    private final SecurityUtil securityUtil;

    private final LogService logService;

    /**
     * 新增网格数据
     *
     * @param basicGrids
     * @return
     */
    @PostMapping("addBasicGrids")
    public Result<Object> addBasicGrids(@RequestBody BasicGrids basicGrids) {
        Long time = System.currentTimeMillis();
        try {
            if(basicGrids==null || StringUtils.isBlank(basicGrids.getName())|| StringUtils.isBlank(basicGrids.getName())||StringUtils.isBlank(basicGrids.getCommunityId())||StringUtils.isBlank(basicGrids.getStreetId())){
                return ResultUtil.error("参数为空，请联系管理员！！");
            }
            QueryWrapper<BasicGrids> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().and(i -> i.eq(BasicGrids::getName, basicGrids.getName()));
            queryWrapper.lambda().and(i -> i.eq(BasicGrids::getStreetId, basicGrids.getStreetId()));
            queryWrapper.lambda().and(i -> i.eq(BasicGrids::getCommunityId, basicGrids.getCommunityId()));
            queryWrapper.lambda().and(j -> j.eq(BasicGrids::getIsDelete, 0));
            int count = basicGridsService.count(queryWrapper);
            if(count>0){
                return ResultUtil.error("网格名重复");
            }
            basicGrids.setIsDelete(0);
            basicGrids.setCreateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = basicGridsService.save(basicGrids);
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
     * 根据主键来删除网格数据
     *
     * @param map
     * @return
     */
    @PostMapping("deleteBasicGrids")
    @Transactional
    public Result<Object> deleteBasicGrids(@RequestBody Map<String, Object> map) {
        if (map == null || map.size() == 0 || !map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            List<String> ids = (List<String>) map.get("ids");
            if (ids == null || ids.size() == 0) {
                return ResultUtil.error("参数为空，请联系管理员！！");
            }
            QueryWrapper<BasicHousingEstate> basicHouseQueryWrapper = new QueryWrapper<>();
            basicHouseQueryWrapper.lambda().and(i -> i.in(BasicHousingEstate::getGridId, ids));
            basicHouseQueryWrapper.lambda().and(j -> j.eq(BasicHousingEstate::getIsDelete, 0));

            int count = basicHousingEstateService.count(basicHouseQueryWrapper);
            if (count > 0) {
                return ResultUtil.error("您删除的网格下拥有小区数据，您不能删除此网格，请先删除网格的小区");
            }

            QueryWrapper<RelaGridsPersonGrids> basicGridPersonWrapper = new QueryWrapper<>();
            basicGridPersonWrapper.lambda().and(i -> i.in(RelaGridsPersonGrids::getGridsId, ids));

            count = relaGridsPersonGridsService.count(basicGridPersonWrapper);
            if (count > 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.error("您删除的网格下拥有网格员数据，您不能删除此网格，请先删除网格的网格员");
            }
            boolean res = basicGridsService.removeByIds(ids);
            if (res) {
                return ResultUtil.data(res, "删除成功");
            } else {
                return ResultUtil.error( "删除失败");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//			logService.addErrorLog("删除异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("删除异常:" + e.getMessage());
        }
    }

    /**
     * 根据主键来获取网格数据
     *
     * @param id
     * @return
     */
    @GetMapping("getBasicGrids")
    public Result<Object> getBasicGrids(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            BasicGrids res = basicGridsService.getById(id);
            List<Map<String, String>> map = new ArrayList<>();

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
     * 分页查询网格数据
     *
     * @param basicGrids
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryBasicGridsList")
    public Result<Object> queryBasicGridsList(BasicGrids basicGrids, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            //用户数据权限
            UserDto user = securityUtil.getCurrUser();
            if (user.getPower() != null){
                if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                    String deptId = user.getPower().getDeptId();
                    Integer attribute = user.getPower().getAttribute();
                    if (attribute == 1){// 街道
                        basicGrids.setStreetId(deptId);
                    }else if (attribute == 2) {// 社区
                        basicGrids.setCommunityId(deptId);
                    }else if (attribute == 3) {// 网格
                        basicGrids.setId(deptId);
                    }
                }
            }
            return basicGridsService.queryBasicGridsListByPage(basicGrids, searchVo, pageVo);
        } catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 更新网格数据
     *
     * @param basicGrids
     * @return
     */
    @PostMapping("updateBasicGrids")
    public Result<Object> updateBasicGrids(@RequestBody BasicGrids basicGrids) {
        if (StringUtils.isBlank(basicGrids.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            QueryWrapper<BasicGrids> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().and(i -> i.eq(BasicGrids::getName, basicGrids.getName()));
            queryWrapper.lambda().and(i -> i.eq(BasicGrids::getStreetId, basicGrids.getStreetId()));
            queryWrapper.lambda().and(i -> i.eq(BasicGrids::getCommunityId, basicGrids.getCommunityId()));
            queryWrapper.lambda().and(j -> j.ne(BasicGrids::getId, basicGrids.getId()));
            queryWrapper.lambda().and(j -> j.eq(BasicGrids::getIsDelete, 0));
            int count = basicGridsService.count(queryWrapper);
            if(count>0){
                return ResultUtil.error("保存异常:" + "网格名重复");
            }
            basicGrids.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = basicGridsService.updateById(basicGrids);
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
     * 导出网格数据
     *
     * @param response
     * @param basicGrids
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, BasicGrids basicGrids) {
        Long time = System.currentTimeMillis();
        try {
            basicGridsService.download(basicGrids, response);
        } catch (Exception e) {
//			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
        }
    }

    /**
     * 模糊查询全部网格数据
     * @param basicGrids
     * @param flag 是否限制权限 true限制权限 false查询所有
     * @return
     */
    @GetMapping("queryAllListByAnyWhere")
    public Result<Object> queryAllListByAnyWhere(BasicGrids basicGrids,
                                       @RequestParam(name = "flag",required = false,defaultValue = "true")Boolean flag) {
        Long time = System.currentTimeMillis();
        try {
            if (flag != null){
                if (flag){
                    //用户数据权限
                    UserDto user = securityUtil.getCurrUser();
                    if (user.getPower() != null){
                        if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                            String deptId = user.getPower().getDeptId();
                            Integer attribute = user.getPower().getAttribute();
                            if (attribute == 1){// 街道
                                basicGrids.setStreetId(deptId);
                            }else if (attribute == 2) {// 社区
                                basicGrids.setCommunityId(deptId);
                            }else if (attribute == 3) {// 网格
                                basicGrids.setId(deptId);
                            }
                        }
                    }
                }
            }
            return basicGridsService.queryAllList(basicGrids);
        } catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 模糊查询全部网格数据
     * @return
     */
    @GetMapping("queryAllList")
    public Result<Object> queryAllList() {
        Long time = System.currentTimeMillis();
        try {
            return basicGridsService.queryAllList(null);
        } catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }
    /**
     * 根据人员Id查询所管理的网格
     *
     * @param personId
     * @return
     */
    @GetMapping("queryMyManagedGridsList")
    public Result<Object> queryMyManagedGridsList(String personId) {
        Long time = System.currentTimeMillis();
        try {
            List<BasicGrids> list = basicGridsService.queryMyManagedGridsList(personId);
            return ResultUtil.data(list);
        } catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 根据网格Id查询该网格下个网格员、建筑、房屋等。。
     *
     * @param gridsId
     * @return
     */
    @GetMapping("queryGridsOwnInformation")
    public Result<Object> queryGridsOwnInformation(String gridsId) {
        Long time = System.currentTimeMillis();
        try {
            List<Map<String, Object>> list = basicGridsService.queryGridsOwnInformation(gridsId);
            //查询该网格下有多少个网格员
            List<Map<String, Object>> gridMemberList = basicGridsService.queryGridmanList(gridsId);
                for (int i = 0; i < list.size(); i++) {
                list.get(i).put("gridMemberList", gridMemberList);
            }
            return ResultUtil.data(list.get(0));
        } catch (Exception e) {
            e.printStackTrace();
//			logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
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
        //@RequestParam("file") MultipartFile file) throws IOException, Exception
        Long time = System.currentTimeMillis();
        try {
            return basicGridsService.importExcel(file);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.error("导入异常" + e.getMessage());
        }
    }

    /**
     * 查询网格区域划分树
     *
     * @return
     */
    @GetMapping("queryAllGridsTree")
    public Result<Object> queryAllGridsTree() {
        Long time = System.currentTimeMillis();
        try {
            List<Map<String, Object>> list = basicGridsService.queryAllGridsTree();
            return ResultUtil.data(list);
        } catch (Exception e) {
            e.printStackTrace();
			logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }
}
