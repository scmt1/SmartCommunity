package me.zhengjie.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.service.ITZhsqMerchantProfileService;
import lombok.AllArgsConstructor;
import me.zhengjie.system.service.dto.UserDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TZhsqMerchantProfile;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;


/**
 * 商户数据接口
 * @author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/tZhsqMerchantProfile")
public class TZhsqMerchantProfileController {

    private final ITZhsqMerchantProfileService tZhsqMerchantProfileService;

    private final SecurityUtil securityUtil;

    /**
     * 新增商户数据数据
     * @param tZhsqMerchantProfile
     * @return
     */
    @PostMapping("addTZhsqMerchantProfile")
    public Result<Object> addTZhsqMerchantProfile(@RequestBody TZhsqMerchantProfile tZhsqMerchantProfile) {
        Long time = System.currentTimeMillis();
        try {
            tZhsqMerchantProfile.setIsDelete(0);
//            if (StringUtils.isNotBlank(tZhsqMerchantProfile.getHeadPortrait())) {
//                //base64 转文件
//                MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(tZhsqMerchantProfile.getHeadPortrait());
//                //文件存储在nginx代理路径下
//                String fileName = UploadFile.uploadFile(imgFile);
//                tZhsqMerchantProfile.setHeadPortrait(fileName);
//            }
            tZhsqMerchantProfile.setCreateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = tZhsqMerchantProfileService.save(tZhsqMerchantProfile);
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
     * 根据主键来删除商户数据数据
     * @param map
     * @return
     */
    @PostMapping("deleteTZhsqMerchantProfile")
    public Result<Object> deleteTZhsqMerchantProfile(@RequestBody Map<String ,Object> map) {
        if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            List<String> ids = (List<String>) map.get("ids");
            if (ids == null || ids.size() == 0 ) {
                return ResultUtil.error("参数为空，请联系管理员！！");
            }
            boolean res = tZhsqMerchantProfileService.removeByIds(ids);
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
     * 根据主键来获取merchant_profile数据
     * @param id
     * @return
     */
    @GetMapping("getTZhsqMerchantProfile")
    public Result<Object> getTZhsqMerchantProfile(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            TZhsqMerchantProfile res = tZhsqMerchantProfileService.getById(id);
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
     * 分页查询商户数据数据
     * @param tZhsqMerchantProfile
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryTZhsqMerchantProfileList")
    public Result<Object> queryTZhsqMerchantProfileList(TZhsqMerchantProfile tZhsqMerchantProfile, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            return tZhsqMerchantProfileService.queryTZhsqMerchantProfileListByPage(tZhsqMerchantProfile, searchVo, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 分页查询商户数据数据
     * @param tZhsqMerchantProfile
     * @param key
     * @param pageVo
     * @return
     */
    @GetMapping("queryTZhsqMerchantProfileListByAnyWayWhere")
    public Result<Object> queryTZhsqMerchantProfileListByAnyWayWhere(TZhsqMerchantProfile tZhsqMerchantProfile, String key, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            //用户数据权限
            UserDto user = securityUtil.getCurrUser();
            if (user.getPower() != null){
                if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null){
                    String deptId = user.getPower().getDeptId();
                    Integer attribute = user.getPower().getAttribute();
                    if (attribute == 1){//街道
                        tZhsqMerchantProfile.setStreetId(deptId);
                    }else if (attribute == 2){//社区
                        tZhsqMerchantProfile.setLegalCommunityId(deptId);
                    }else if (attribute == 3){//网格
                        tZhsqMerchantProfile.setGridId(deptId);
                    }
                }
            }
            return tZhsqMerchantProfileService.queryTZhsqMerchantProfileListByAnyWayWhere(tZhsqMerchantProfile, key, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 更新商户数据数据
     * @param tZhsqMerchantProfile
     * @return
     */
    @PostMapping("updateTZhsqMerchantProfile")
    public Result<Object> updateTZhsqMerchantProfile(@RequestBody TZhsqMerchantProfile tZhsqMerchantProfile) {
        if (StringUtils.isBlank(tZhsqMerchantProfile.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
//            if (StringUtils.isNotBlank(tZhsqMerchantProfile.getHeadPortrait()) && tZhsqMerchantProfile.getImageIsUpdate()) {
//                //base64 转文件
//                MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(tZhsqMerchantProfile.getHeadPortrait());
//                //文件存储在nginx代理路径下
//                String fileName = UploadFile.uploadFile(imgFile);
//                tZhsqMerchantProfile.setHeadPortrait(fileName);
//            }
            tZhsqMerchantProfile.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = tZhsqMerchantProfileService.updateById(tZhsqMerchantProfile);
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
     * 导出商户数据数据
     * @param response
     * @param tZhsqMerchantProfile
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, TZhsqMerchantProfile tZhsqMerchantProfile) {
        Long time = System.currentTimeMillis();
        try {
            tZhsqMerchantProfileService.download(tZhsqMerchantProfile, response);
        } catch (Exception e) {
//            logService.addErrorLog("导出异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }
    /**
     * 根据社区id 统计各个专题数据
     * @param communityId
     * @param gridId
     * @return
     */
    @GetMapping("/selectByCommunityAndGrid")
    public Result<Object> selectByCommunityAndGrid(String communityId,String gridId) {
        try {
            //用户数据权限
            UserDto user = securityUtil.getCurrUser();
            if (user.getPower() != null){
                if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null){
                    String deptId = user.getPower().getDeptId();
                    Integer attribute = user.getPower().getAttribute();
                    if (attribute == 1){//街道
                        //街道查询所有
                    }else if (attribute == 2){//社区
                        communityId = deptId;
                    }else if (attribute == 3){//网格
                        gridId = deptId;
                    }
                }
            }
            Map<String, Object> stringObjectMap = tZhsqMerchantProfileService.selectByCommunity(communityId,gridId);

            return ResultUtil.data(stringObjectMap,"查询成功");

        } catch (Exception e) {
            return ResultUtil.error("查询失败");
        }

    }
}
