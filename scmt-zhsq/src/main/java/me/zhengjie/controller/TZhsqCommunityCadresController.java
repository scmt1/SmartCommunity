package me.zhengjie.controller;

import java.security.Security;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.client.BaseClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.mapper.BasicGridsMapper;
import me.zhengjie.mapper.GridDeptMapper;
import me.zhengjie.system.service.UserService;
import me.zhengjie.system.service.dto.UserDto;
import me.zhengjie.util.BusinessErrorException;
import me.zhengjie.entity.BasicPerson;
import me.zhengjie.entity.BasicResume;
import me.zhengjie.entity.TZhsqCommunityCadres;

import me.zhengjie.entity.TZhsqVolunteer;
import me.zhengjie.service.IBasicGridsService;
import me.zhengjie.service.IBasicResumeService;
import me.zhengjie.service.ITZhsqCommunityCadresService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.web.multipart.MultipartFile;


/**
 * 社区干部档案数据接口
 * @author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/tZhsqCommunityCadres")
public class TZhsqCommunityCadresController {
   
    private final ITZhsqCommunityCadresService tZhsqCommunityCadresService;
   
    private final IBasicResumeService iBasicResumeService;

    private final SecurityUtil securityUtil;


    /**
     * 新增社区干部档案数据
     * @param tZhsqCommunityCadres
     * @return
     */
    @PostMapping("addTZhsqCommunityCadres")
    public Result<Object> addTZhsqCommunityCadres(@RequestBody TZhsqCommunityCadres tZhsqCommunityCadres) {
        Long time = System.currentTimeMillis();
        try {
            tZhsqCommunityCadres.setIsDelete(0);
//            if (StringUtils.isNotBlank(tZhsqCommunityCadres.getHeadPortrait())) {
//                //base64 转文件
//                MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(tZhsqCommunityCadres.getHeadPortrait());
//                //文件存储在nginx代理路径下
//                String fileName = UploadFile.uploadFile(imgFile);
//                tZhsqCommunityCadres.setHeadPortrait(fileName);
//            }
            //身份证验证重复
            if (StringUtils.isNotBlank(tZhsqCommunityCadres.getIdCard())) {
                //根据id和身份证获取信息
                QueryWrapper<TZhsqCommunityCadres> queryWrapper = new QueryWrapper();
                queryWrapper.lambda().and(j -> j.eq(TZhsqCommunityCadres::getIdCard, tZhsqCommunityCadres.getIdCard()));
                queryWrapper.lambda().and(j -> j.eq(TZhsqCommunityCadres::getIsDelete, 0));
                int res = tZhsqCommunityCadresService.count(queryWrapper);
                if (res >= 1) {
                    return ResultUtil.error("身份证号码重复,请检查");
                }
            }
            //手机号验证重复
            if (StringUtils.isNotBlank(tZhsqCommunityCadres.getPhone())) {
                //根据id和身份证获取信息
                QueryWrapper<TZhsqCommunityCadres> queryWrapper = new QueryWrapper();
                queryWrapper.lambda().and(j -> j.eq(TZhsqCommunityCadres::getPhone, tZhsqCommunityCadres.getPhone()));
                queryWrapper.lambda().and(j -> j.eq(TZhsqCommunityCadres::getIsDelete, 0));
                int res = tZhsqCommunityCadresService.count(queryWrapper);
                if (res >= 1) {
                    return ResultUtil.error("身份证号码重复,请检查");
                }
            }
            tZhsqCommunityCadres.setCreateTime(new Timestamp(System.currentTimeMillis()));
            tZhsqCommunityCadres.setBirthday(new Timestamp(System.currentTimeMillis()));
            boolean res = tZhsqCommunityCadresService.save(tZhsqCommunityCadres);
            if (res) {
                //添加个人履历
                BasicResume basicResume = new BasicResume();
                basicResume.setPersonId(tZhsqCommunityCadres.getId());
                basicResume.setPersonName(tZhsqCommunityCadres.getName());
                basicResume.setPost(tZhsqCommunityCadres.getPost());
                basicResume.setDepartment(tZhsqCommunityCadres.getDepartment());
                basicResume.setStartTime(tZhsqCommunityCadres.getCreateTime());
                basicResume.setCreateTime(tZhsqCommunityCadres.getCreateTime());
                basicResume.setIsDelete(0);
                iBasicResumeService.save(basicResume);
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
     * 修改电话号码
     *
     * @param tZhsqCommunityCadres
     * @param
     * @param
     * @return
     */
    @PostMapping("updatePhone")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> queryBasicPersonListByAnyWayWhere(@RequestBody TZhsqCommunityCadres tZhsqCommunityCadres) {
        Long time = System.currentTimeMillis();

        boolean res = tZhsqCommunityCadresService.updateById(tZhsqCommunityCadres);
        if (res) {
            return ResultUtil.data(res, "保存成功");
        } else {
            return ResultUtil.error("保存失败");
        }
    }


    /**
     * 根据主键来删除社区干部档案数据
     * @param map
     * @return
     */
    @PostMapping("deleteTZhsqCommunityCadres")
    public Result<Object> deleteTZhsqCommunityCadres(@RequestBody Map<String ,Object> map) {
        if (map == null || map.size() == 0 ||!map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            List<String> ids = (List<String>) map.get("ids");
            if (ids == null || ids.size() == 0 ) {
                return ResultUtil.error("参数为空，请联系管理员！！");
            }
            boolean res = tZhsqCommunityCadresService.removeByIds(ids);
            if (res) {
                return ResultUtil.data(res, "删除成功");
            } else {
                return ResultUtil.error( "删除失败");
            }
        } catch (Exception e) {
//			logService.addErrorLog("删除异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("删除异常:" + e.getMessage());
        }
    }

    /**
     * 根据主键来获取社区干部档案数据
     * @param id
     * @return
     */
    @GetMapping("getTZhsqCommunityCadres")
    public Result<Object> getTZhsqCommunityCadres(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            TZhsqCommunityCadres res = tZhsqCommunityCadresService.getById(id);

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
     * 分页查询社区干部档案数据
     * @param tZhsqCommunityCadres
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryTZhsqCommunityCadresList")
    public Result<Object> queryTZhsqCommunityCadresList(TZhsqCommunityCadres tZhsqCommunityCadres, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            //用户数据权限
            UserDto user = securityUtil.getCurrUser();
            if (user.getPower() != null){
                if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                    String deptId = user.getPower().getDeptId();
                    Integer attribute = user.getPower().getAttribute();
                    if (attribute == 1){// 街道
                        tZhsqCommunityCadres.setStreetId(deptId);
                    }else if (attribute == 2) {// 社区
                        tZhsqCommunityCadres.setCommunityId(deptId);
                    }else if (attribute == 3){// 网格
                        return  ResultUtil.data(null);
                    }
                }
            }
            return tZhsqCommunityCadresService.queryTZhsqCommunityCadresListByPage(tZhsqCommunityCadres, searchVo, pageVo);
        } catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 更新社区干部档案数据
     * @param tZhsqCommunityCadres
     * @return
     */
    @PostMapping("updateTZhsqCommunityCadres")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> updateTZhsqCommunityCadres(@RequestBody TZhsqCommunityCadres tZhsqCommunityCadres) {
        if (StringUtils.isBlank(tZhsqCommunityCadres.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        //身份证验证重复
        if (StringUtils.isNotBlank(tZhsqCommunityCadres.getIdCard())) {
            //根据id和身份证获取信息
            QueryWrapper<TZhsqCommunityCadres> queryWrapper = new QueryWrapper();
            queryWrapper.lambda().and(j -> j.eq(TZhsqCommunityCadres::getIdCard, tZhsqCommunityCadres.getIdCard()));
            queryWrapper.lambda().and(j -> j.ne(TZhsqCommunityCadres::getId, tZhsqCommunityCadres.getId()));
            queryWrapper.lambda().and(j -> j.eq(TZhsqCommunityCadres::getIsDelete, 0));
            int res = tZhsqCommunityCadresService.count(queryWrapper);
            if (res >= 1) {
                return ResultUtil.error("身份证号码重复,请检查");
            }
        }
        //手机号验证重复
        if (StringUtils.isNotBlank(tZhsqCommunityCadres.getPhone())) {
            //根据id和身份证获取信息
            QueryWrapper<TZhsqCommunityCadres> queryWrapper = new QueryWrapper();
            queryWrapper.lambda().and(j -> j.eq(TZhsqCommunityCadres::getPhone, tZhsqCommunityCadres.getPhone()));
            queryWrapper.lambda().and(j -> j.ne(TZhsqCommunityCadres::getId, tZhsqCommunityCadres.getId()));
            queryWrapper.lambda().and(j -> j.eq(TZhsqCommunityCadres::getIsDelete, 0));
            int res = tZhsqCommunityCadresService.count(queryWrapper);
            if (res >= 1) {
                return ResultUtil.error("手机号号码重复,请检查");
            }
        }
        //查询出原有数据
        TZhsqCommunityCadres oldTZhsqCommunityCadres = tZhsqCommunityCadresService.getById(tZhsqCommunityCadres.getId());
        if (oldTZhsqCommunityCadres == null) {
            return ResultUtil.error("数据异常，修改失败！");
        }
        {
            JSONObject user1 = new JSONObject();
            user1.put("personId", tZhsqCommunityCadres.getId());
            user1.put("realName", tZhsqCommunityCadres.getName());
            user1.put("account", tZhsqCommunityCadres.getPhone());
            user1.put("sex", tZhsqCommunityCadres.getSex());
            user1.put("idNumber", tZhsqCommunityCadres.getIdCard());
            user1.put("birthday", tZhsqCommunityCadres.getBirthday());
           // user1.put("gridId", tZhsqCommunityCadres.get());
            user1.put("phone", tZhsqCommunityCadres.getPhone());
            user1.put("communityId", tZhsqCommunityCadres.getCommunityId());
//            JSONObject jsonObject = baseClient.synchronModify(user1);
//            if(jsonObject.getInteger("code")!=200){
//                throw new BusinessErrorException("同步异常");
//            }
        }
        Long time = System.currentTimeMillis();
        try {
//            if (StringUtils.isNotBlank(tZhsqCommunityCadres.getHeadPortrait()) && tZhsqCommunityCadres.getImageIsUpdate()) {
//                //base64 转文件
//                MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(tZhsqCommunityCadres.getHeadPortrait());
//                //文件存储在nginx代理路径下
//                String fileName = UploadFile.uploadFile(imgFile);
//                tZhsqCommunityCadres.setHeadPortrait(fileName);
//            }
            tZhsqCommunityCadres.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = tZhsqCommunityCadresService.updateById(tZhsqCommunityCadres);
            if (res) {
                //如果本次的部门或者岗位跟之前不一致，需要更新履历
                if (!oldTZhsqCommunityCadres.getPost().equals(tZhsqCommunityCadres.getPost()) || !oldTZhsqCommunityCadres.getDepartment().equals(tZhsqCommunityCadres.getDepartment())) {
                    //查询最后一条履历
                    BasicResume lastBasicResume = iBasicResumeService.queryLastOneData(tZhsqCommunityCadres.getId());
                    if (lastBasicResume != null) {
                        //更新上一次的履历的结束任职时间
                        BasicResume resume = new BasicResume();
                        resume.setId(lastBasicResume.getId());
                        resume.setEndTime(tZhsqCommunityCadres.getUpdateTime());
                        iBasicResumeService.updateById(resume);
                    }
                    //添加个人履历
                    BasicResume basicResume = new BasicResume();
                    basicResume.setPersonId(tZhsqCommunityCadres.getId());
                    basicResume.setPersonName(tZhsqCommunityCadres.getName());
                    basicResume.setPost(tZhsqCommunityCadres.getPost());
                    basicResume.setDepartment(tZhsqCommunityCadres.getDepartment());
                    basicResume.setStartTime(tZhsqCommunityCadres.getUpdateTime());
                    basicResume.setCreateTime(tZhsqCommunityCadres.getUpdateTime());
                    basicResume.setIsDelete(0);
                    iBasicResumeService.save(basicResume);
                }
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
     * 导出社区干部档案数据
     * @param response
     * @param tZhsqCommunityCadres
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, TZhsqCommunityCadres tZhsqCommunityCadres) {
        Long time = System.currentTimeMillis();
        try {
            tZhsqCommunityCadresService.download(tZhsqCommunityCadres, response);
        } catch (Exception e) {
//			logService.addErrorLog("导出异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
        }
    }


    /**
     * 模糊查询所有社区干部档案数据
     * @param tZhsqCommunityCadres
     * @return
     */
    @GetMapping("/queryCommunityCadresList")
    public Result<Object> queryCommunityCadresList(TZhsqCommunityCadres tZhsqCommunityCadres) {
        Long time = System.currentTimeMillis();
        try {
            return tZhsqCommunityCadresService.queryCommunityCadresList(tZhsqCommunityCadres);
        } catch (Exception e) {
//			logService.addErrorLog("查询异常", this.getClass().getName(), time- System.currentTimeMillis(), e);
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
            return tZhsqCommunityCadresService.importExcel(file);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.error("导入异常" + e.getMessage());
        }
    }
}
