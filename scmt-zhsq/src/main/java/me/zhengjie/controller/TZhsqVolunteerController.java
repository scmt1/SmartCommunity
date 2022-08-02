package me.zhengjie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.TZhsqVolunteer;
import me.zhengjie.service.ITZhsqVolunteerService;
import me.zhengjie.system.service.dto.UserDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


/**
 * 志愿者数据接口
 * @author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/tZhsqVolunteer")
public class TZhsqVolunteerController {

    private final ITZhsqVolunteerService tZhsqVolunteerService;

    private final ITZhsqVolunteerService itZhsqVolunteerService;

    private final SecurityUtil securityUtil;

    /**
     * 新增志愿者数据
     * @param tZhsqVolunteer
     * @return
     */
    @PostMapping("addTZhsqVolunteer")
    public Result<Object> addTZhsqVolunteer(@RequestBody TZhsqVolunteer tZhsqVolunteer) {
        Long time = System.currentTimeMillis();
        try {
            tZhsqVolunteer.setIsDelete(0);
            tZhsqVolunteer.setState("0");
            tZhsqVolunteer.setCreateTime(new Timestamp(System.currentTimeMillis()));
            //身份证验证重复
            if (StringUtils.isNotBlank(tZhsqVolunteer.getIdCard())) {
                //根据id和身份证获取信息
                QueryWrapper<TZhsqVolunteer> queryWrapper = new QueryWrapper();
                queryWrapper.lambda().and(j -> j.eq(TZhsqVolunteer::getIdCard, tZhsqVolunteer.getIdCard()));
                int res = tZhsqVolunteerService.count(queryWrapper);
                if (res >= 1) {
                    return ResultUtil.error("身份证号码重复,请检查");
                }
            }
            //手机号验证重复
            if (StringUtils.isNotBlank(tZhsqVolunteer.getPhone())) {
                //根据id和手机号获取信息
                QueryWrapper<TZhsqVolunteer> queryWrapper = new QueryWrapper();
                queryWrapper.lambda().and(j -> j.eq(TZhsqVolunteer::getPhone, tZhsqVolunteer.getPhone()));
                int res = tZhsqVolunteerService.count(queryWrapper);
                if (res >= 1) {
                    return ResultUtil.error("手机号号码重复,请检查");
                }
            }

            boolean res = tZhsqVolunteerService.save(tZhsqVolunteer);
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
     * 根据主键来删除志愿者数据
     * @param map
     * @return
     */
    @RequestMapping("deleteTZhsqVolunteer")
    public Result<Object> deleteTZhsqVolunteer(@RequestBody  Map<String ,Object> map) {
        if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            List<String> ids = (List<String>) map.get("ids");
            if (ids == null || ids.size() == 0 ) {
                return ResultUtil.error("参数为空，请联系管理员！！");
            }
            boolean res = tZhsqVolunteerService.removeByIds(ids);
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
     * 根据主键来获取志愿者数据
     * @param id
     * @return
     */
    @GetMapping("getTZhsqVolunteer")
    public Result<Object> getTZhsqVolunteer(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            TZhsqVolunteer res = tZhsqVolunteerService.getTZhsqVolunteerById(id);
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
     * 分页查询志愿者数据
     * @param tZhsqVolunteer
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryTZhsqVolunteerList")
    public Result<Object> queryTZhsqVolunteerList(TZhsqVolunteer tZhsqVolunteer, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            //用户数据权限
            UserDto user = securityUtil.getCurrUser();
            if (user.getPower() != null){
                if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                    String deptId = user.getPower().getDeptId();
                    Integer attribute = user.getPower().getAttribute();
                    if (attribute == 1){// 街道
                        tZhsqVolunteer.setStreetId(deptId);
                    }else if (attribute == 2) {// 社区
                        tZhsqVolunteer.setCommunityId(deptId);
                    }else if (attribute == 3) {// 网格
                        tZhsqVolunteer.setOwnedGrid(deptId);
                    }
                }
            }
            return tZhsqVolunteerService.queryTZhsqVolunteerListByPage(tZhsqVolunteer, searchVo, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }


    /**
     * 模糊查询全部志愿者数据
     * @param tZhsqVolunteer
     * @return
     */
    @GetMapping("queryAllList")
    public Result<Object> queryAllList(TZhsqVolunteer tZhsqVolunteer) {
        Long time = System.currentTimeMillis();
        try {
            return tZhsqVolunteerService.queryAllList(tZhsqVolunteer);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 更新志愿者数据
     * @param tZhsqVolunteer
     * @return
     */
    @PostMapping("updateTZhsqVolunteer")
    public Result<Object> updateTZhsqVolunteer(@RequestBody TZhsqVolunteer tZhsqVolunteer) {
        if (StringUtils.isBlank(tZhsqVolunteer.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
//            if(StringUtils.isNotBlank(tZhsqVolunteer.getImgPath()) && tZhsqVolunteer.getImageIsUpdate()){
//                //base64 转文件
//                MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(tZhsqVolunteer.getImgPath());
//                //文件存储在nginx代理路径下
//                String fileName = UploadFile.uploadFile(imgFile);
//                tZhsqVolunteer.setImgPath(fileName);
//            }
            //身份证验证重复
            if (StringUtils.isNotBlank(tZhsqVolunteer.getIdCard())) {
                //根据id和身份证获取信息
                QueryWrapper<TZhsqVolunteer> queryWrapper = new QueryWrapper();
                queryWrapper.lambda().and(j -> j.eq(TZhsqVolunteer::getIdCard, tZhsqVolunteer.getIdCard()));
                queryWrapper.lambda().and(j -> j.ne(TZhsqVolunteer::getId, tZhsqVolunteer.getId()));
                int res = tZhsqVolunteerService.count(queryWrapper);
                if (res >= 1) {
                    return ResultUtil.error("身份证号码重复,请检查");
                }
            }
            //手机号验证重复
            if (StringUtils.isNotBlank(tZhsqVolunteer.getPhone())) {
                //根据id和手机号获取信息
                QueryWrapper<TZhsqVolunteer> queryWrapper = new QueryWrapper();
                queryWrapper.lambda().and(j -> j.eq(TZhsqVolunteer::getPhone, tZhsqVolunteer.getPhone()));
                queryWrapper.lambda().and(j -> j.ne(TZhsqVolunteer::getId, tZhsqVolunteer.getId()));
                int res = tZhsqVolunteerService.count(queryWrapper);
                if (res >= 1) {
                    return ResultUtil.error("手机号重复,请检查");
                }
            }
            tZhsqVolunteer.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = tZhsqVolunteerService.updateById(tZhsqVolunteer);
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
     * 导出志愿者数据
     * @param response
     * @param tZhsqVolunteer
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, TZhsqVolunteer tZhsqVolunteer) {
        Long time = System.currentTimeMillis();
        try {
            tZhsqVolunteerService.download(tZhsqVolunteer, response);
        } catch (Exception e) {
//            logService.addErrorLog("导出异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }

    /**
     * 根据社区id和网格id查询志愿者人数
     * @param communityId
     * @param gridId
     * @return
     */
    @GetMapping("/selectByCommunityAndGrid")
    public Result<Object> selectByCommunityAndGrid(String communityId,String gridId) {
        try {
            QueryWrapper<TZhsqVolunteer> queryWrapper = new QueryWrapper();
            if(StringUtils.isNotBlank(communityId)){
                queryWrapper.lambda().and(i -> i.eq(TZhsqVolunteer::getCommunityId, communityId));
            }
            if(StringUtils.isNotBlank(gridId)){
                queryWrapper.lambda().and(i -> i.eq(TZhsqVolunteer::getOwnedGrid, gridId));
            }
            int count = tZhsqVolunteerService.count(queryWrapper);

            return ResultUtil.data(count,"查询成功");

        } catch (Exception e) {
            return ResultUtil.error("查询失败");
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
            return tZhsqVolunteerService.importExcel(file);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.error("导入异常" + e.getMessage());
        }
    }
}
