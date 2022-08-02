package me.zhengjie.controller;

import java.util.*;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicHousingEstate;
import me.zhengjie.service.IBasicHousingEstateService;
import me.zhengjie.service.ITZhsqPropertyManagementService;
import org.apache.commons.httpclient.HttpClient;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import me.zhengjie.entity.TZhsqPropertyManagement;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


/**
 * 物业信息数据接口
 *
 * @author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/tZhsqPropertyManagement")
public class TZhsqPropertyManagementController {

    private final ITZhsqPropertyManagementService tZhsqPropertyManagementService;

    private final IBasicHousingEstateService basicHousingEstateService;
//    private final RestTemplate restTemplate;

    /**
     * 新增物业信息数据
     *
     * @param tZhsqPropertyManagement
     * @return
     */
    @PostMapping("addTZhsqPropertyManagement")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> addTZhsqPropertyManagement(TZhsqPropertyManagement tZhsqPropertyManagement) {
        Long time = System.currentTimeMillis();
        String id = UUID.randomUUID().toString().replaceAll("-", "");

            tZhsqPropertyManagement.setId(id);
            tZhsqPropertyManagement.setIsDelete(0);
            tZhsqPropertyManagement.setCreateTime(new Timestamp(System.currentTimeMillis()));
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            Boolean same = tZhsqPropertyManagementService.isSame(tZhsqPropertyManagement.getPropertyName(), "");
            System.out.println(id);
            if (same) {
                int res = tZhsqPropertyManagementService.insert(tZhsqPropertyManagement);
                map.put("res", res);
                if (res > 0) {
                    HttpClient httpClient = new HttpClient();
                    JSONObject jsonObjectRes = new JSONObject();
                    jsonObjectRes.put("uuid", id);
                    jsonObjectRes.put("name", tZhsqPropertyManagement.getPropertyName());
                    jsonObjectRes.put("principalName", tZhsqPropertyManagement.getPropertyPrincipal());
                    jsonObjectRes.put("principalPhone", tZhsqPropertyManagement.getPropertyPrincipalPhone());

                    //todo 合并代码后直接调用方法
                    //httpClient.exchange(PropertyClient.initPropertyCompanyByGrid, HttpMethod.POST,jsonObjectRes ,restTemplate);
//                    JSONObject jsonObject = entityClient.initPropertyCompanyByGrid(jsonObjectRes);
//                    if (jsonObject.containsKey("code") && jsonObject.get("code").toString().equals("200")) {
//                    } else {
//                        throw new RuntimeException("同步异常");
//                    }
                    return ResultUtil.data(map, "保存成功");

                } else {
                    return ResultUtil.data(map, "保存失败");
                }
            } else {
                return ResultUtil.error("物业名称重复！");
            }

    }

    /**
     * 根据主键来删除物业信息数据
     *
     * @param map
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("deleteTZhsqPropertyManagement")
    public Result<Object> deleteTZhsqPropertyManagement(@RequestBody Map<String, Object> map) {
        if (map == null || map.size() == 0 || !map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();

        List<String> ids = (List<String>) map.get("ids");
        if (ids == null || ids.size() == 0) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        QueryWrapper<BasicHousingEstate> basicHouseQueryWrapper = new QueryWrapper<>();
        basicHouseQueryWrapper.lambda().and(i -> i.in(BasicHousingEstate::getPropertyNameId, ids));

        int count  = basicHousingEstateService .count(basicHouseQueryWrapper);
        if(count>0){
            return ResultUtil.error("您删除的物业名下 拥有小区数据，您不能删除此物业，请先删除物业下的小区");
        }
        boolean res = tZhsqPropertyManagementService.removeByIds(ids);

        if (res) {
            return ResultUtil.data(res, "删除成功");
        } else {
            return ResultUtil.error( "删除失败");
        }

    }

    /**
     * 根据主键来获取物业信息数据
     *
     * @param id
     * @return
     */
    @GetMapping("getTZhsqPropertyManagement")
    public Result<Object> getTZhsqPropertyManagement(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            TZhsqPropertyManagement res = tZhsqPropertyManagementService.getById(id);
            if (res != null) {
                return ResultUtil.data(res, "查询成功");
            } else {
                return ResultUtil.error("查询失败");
            }
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 分页查询物业信息数据
     *
     * @param tZhsqPropertyManagement
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryTZhsqPropertyManagementList")
    public Result<Object> queryTZhsqPropertyManagementList(TZhsqPropertyManagement tZhsqPropertyManagement, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            return tZhsqPropertyManagementService.queryTZhsqPropertyManagementListByPage(tZhsqPropertyManagement, searchVo, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 分页查询物业信息数据
     *
     * @param tZhsqPropertyManagement
     * @param gridId
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryTZhsqPropertyManagementListByGrid")
    public Result<Object> queryTZhsqPropertyManagementListByGrid(TZhsqPropertyManagement tZhsqPropertyManagement, String gridId, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            return tZhsqPropertyManagementService.queryTZhsqPropertyManagementListByPageWithGridId(tZhsqPropertyManagement, gridId, searchVo, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 更新物业信息数据
     *
     * @param tZhsqPropertyManagement
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("updateTZhsqPropertyManagement")
    public Result<Object> updateTZhsqPropertyManagement(TZhsqPropertyManagement tZhsqPropertyManagement) {
        if (StringUtils.isBlank(tZhsqPropertyManagement.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();

        tZhsqPropertyManagement.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        Boolean same = tZhsqPropertyManagementService.isSame(tZhsqPropertyManagement.getPropertyName(), tZhsqPropertyManagement.getId());
        if (same) {
            boolean res = tZhsqPropertyManagementService.updateById(tZhsqPropertyManagement);
            HttpClient httpClient = new HttpClient();
            JSONObject jsonObjectRes = new JSONObject();
            jsonObjectRes.put("uuid", tZhsqPropertyManagement.getId());
            jsonObjectRes.put("name", tZhsqPropertyManagement.getPropertyName());
            jsonObjectRes.put("principalName", tZhsqPropertyManagement.getPropertyPrincipal());
            jsonObjectRes.put("principalPhone", tZhsqPropertyManagement.getPropertyPrincipalPhone());

            if (res) {
                //todo 合并代码后直接调用方法
                //httpClient.exchange(PropertyClient.updatePropertyCompanyByGrid, HttpMethod.POST,jsonObjectRes ,restTemplate);
//                JSONObject jsonObject = entityClient.updatePropertyCompanyByGrid(jsonObjectRes);
//                if (jsonObject.containsKey("code") && jsonObject.get("code").toString().equals("200")) {
//                } else {
//                    throw new RuntimeException("同步异常");
//                }
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } else {
            return ResultUtil.error("物业名称已存在！");
        }

    }

    /**
     * 导出物业信息数据
     *
     * @param response
     * @param tZhsqPropertyManagement
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, TZhsqPropertyManagement tZhsqPropertyManagement) {
        Long time = System.currentTimeMillis();
        try {
            tZhsqPropertyManagementService.download(tZhsqPropertyManagement, response);
        } catch (Exception e) {
//            logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
        }
    }

    /**
     * 模糊查询全部物业信息数据
     *
     * @param propertyManagement
     * @return
     */
    @GetMapping("queryAllList")
    public Result<Object> queryAllList(TZhsqPropertyManagement propertyManagement) {
        Long time = System.currentTimeMillis();
        try {
            return tZhsqPropertyManagementService.queryAllList(propertyManagement);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * Excel导入物业信息数据
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
            return tZhsqPropertyManagementService.importExcel(file);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.error("保存异常" + e.getMessage());
        }
    }
}
