package me.zhengjie.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;

import me.zhengjie.entity.*;
import me.zhengjie.service.*;
import lombok.AllArgsConstructor;
import me.zhengjie.system.service.UserService;
import me.zhengjie.system.service.dto.UserDto;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 人员档案数据接口
 *
 * @author
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/basicPerson")
public class BasicPersonController {

    private final IBasicPersonService basicPersonService;

    private final IRelaPersonHouseService relaPersonHouseService;

    private final IRelaPersonRelativesService relaPersonRelativesService;

    private final IBasicHousingService basicHousingService;

    private final ITZhsqGridMemberService tZhsqGridMemberService;

    private final ITZhsqCommunityCadresService zhsqCommunityCadresService;

    private final ITZhsqVolunteerService itZhsqVolunteerService;

    private final SecurityUtil securityUtil;


    /**
     * 新增人口档案数据
     *
     * @param basicPerson
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("addBasicPerson")
    public Result<Object> addBasicPerson(@RequestBody BasicPerson basicPerson) {
        Long time = System.currentTimeMillis();
        try {
            //身份证验证重复
            if (StringUtils.isNotBlank(basicPerson.getCardId())) {
                boolean b = checkIdCard(basicPerson.getCardId());
                if (b) {
                    return ResultUtil.error("身份证号码重复!");
                }
            } else {
                return ResultUtil.error("身份证号码为空，请重新录入！！");
            }
            //户主户号重复判断
            if ("1".equals(basicPerson.getPersonType())) {
                if (StringUtils.isNotBlank(basicPerson.getAccNumber()) && StringUtils.isNotBlank(basicPerson.getAccCard())) {
                    boolean b = checkAccNumber(basicPerson.getAccNumber(), basicPerson.getAccCard());
                    if (b) {
                        return ResultUtil.error("户主重复，请重新录入");
                    }
                } else {
                    return ResultUtil.error("参数为空，请联系管理员！！");
                }
            }
            if (StringUtils.isNotBlank(basicPerson.getPhone())) {
                boolean b = checkPhone(basicPerson.getPhone());
                if (b) {
                    return ResultUtil.error("电话号重复，请重新录入");
                }
            } else {
                return ResultUtil.error("电话号参数为空，请联系管理员！！");
            }


            basicPerson.setIsDelete(0);
            basicPerson.setCreateTime(new Timestamp(System.currentTimeMillis()));
            String id = UUID.randomUUID().toString().replaceAll("-", "");
            basicPerson.setId(id);
            boolean res = basicPersonService.save(basicPerson);
            if (res) {
                List<RelaPersonRelatives> relativesList = basicPerson.getRelativesList();
                if (relativesList != null) {
                    for (RelaPersonRelatives item : relativesList) {
                        RelaPersonRelatives relaPersonRelatives = new RelaPersonRelatives();
                        relaPersonRelatives.setPersonId(id);
                        relaPersonRelatives.setName(item.getName());
                        relaPersonRelatives.setPhone(item.getPhone());
                        relaPersonRelatives.setRelationship(item.getRelationship());
                        relaPersonRelatives.setCreateTime(new Timestamp(time));
                        relaPersonRelativesService.save(relaPersonRelatives);
                    }
                }
//                HttpClient httpClient = new HttpClient();
                JSONObject jsonObjectRes = new JSONObject();
                jsonObjectRes.put("idNumber", basicPerson.getCardId());
                jsonObjectRes.put("ownedGridId", basicPerson.getOwnedGridId());
                jsonObjectRes.put("personId", basicPerson.getId());
                jsonObjectRes.put("communityId", basicPerson.getCommunityId());
                //JSONObject jsonObject = baseClient.synchronUser(jsonObjectRes);
//                if (jsonObject.containsKey("code") && jsonObject.get("code").toString().equals("200")) {
//                } else {
//                    throw new RuntimeException("同步异常");
//                }
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

//            logService.addErrorLog("保存异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("保存异常:" + e.getMessage());
        }
    }

    /**
     * 根据主键来删除人口档案
     *
     * @param map
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("deleteBasicPerson")
    public Result<Object> deleteBasicPerson(@RequestBody Map<String, Object> map) {
        if (map == null || map.size() == 0 || !map.containsKey("ids")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            List<String> ids = (List<String>) map.get("ids");
            if (ids == null || ids.size() == 0) {
                return ResultUtil.error("参数为空，请联系管理员！！");
            }
            boolean isDelete = false;
            //todo 查询是否关联用户

            if(isDelete){
                return ResultUtil.error( "删除失败:您删除的用户关联了账号，请先注销账号");
            }
            boolean res = basicPersonService.removeByIds(ids);
            if (res) {
                return ResultUtil.data(res, "删除成功");
            } else {
                return ResultUtil.error( "删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

//            logService.addErrorLog("删除异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("删除异常:" + e.getMessage());
        }
    }

    /**
     * 根据主键来获取人口档案
     *
     * @param id
     * @return
     */
    @GetMapping("getBasicPerson")
    public Result<Object> getBasicPerson(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            BasicPerson res = basicPersonService.getById(id);
            if (res != null && res.getTableType() != null && res.getTableType().indexOf("2") >-1) {
                QueryWrapper<RelaPersonRelatives> relativesQueryWrapper = new QueryWrapper<>();
                relativesQueryWrapper.lambda().and(i -> i.eq(RelaPersonRelatives::getPersonId, res.getId()));
                List<RelaPersonRelatives> list = relaPersonRelativesService.list(relativesQueryWrapper);
                res.setRelativesList(list);

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
     * 分页查询人口档案
     *
     * @param basicPerson
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryBasicPersonList")
    public Result<Object> queryBasicPersonList(BasicPerson basicPerson, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            //用户数据权限
            UserDto user = securityUtil.getCurrUser();
            if (user.getPower() != null){
                if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                    String deptId = user.getPower().getDeptId();
                    Integer attribute = user.getPower().getAttribute();
                    if (attribute == 1){// 街道
                        basicPerson.setStreetId(deptId);
                    }else if (attribute == 2) {// 社区
                        basicPerson.setCommunityId(deptId);
                    }else if (attribute == 3) {// 网格
                        basicPerson.setOwnedGridId(deptId);
                    }
                }
            }
            return basicPersonService.queryBasicPersonListByPage(basicPerson, searchVo, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 分页查询人口档案
     *
     * @param basicPerson
     * @param key
     * @param pageVo
     * @return
     */
    @GetMapping("queryBasicPersonListByAnyWayWhere")
    public Result<Object> queryBasicPersonListByAnyWayWhere(BasicPerson basicPerson, String key, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            return basicPersonService.queryBasicPersonListByAnyWayWhere(basicPerson, key, pageVo);
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 修改电话号码
     *
     * @param basicPerson
     * @param
     * @param
     * @return
     */
    @PostMapping("updatePhone")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> queryBasicPersonListByAnyWayWhere(@RequestBody BasicPerson basicPerson) {
        Long time = System.currentTimeMillis();
        try {
            boolean res = basicPersonService.updateById(basicPerson);
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
     * 更新人口档案
     *
     * @param basicPerson
     * @return
     */
    @PostMapping("updateBasicPerson")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> updateBasicPerson(@RequestBody BasicPerson basicPerson) {
        if (StringUtils.isBlank(basicPerson.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        //身份证验证重复
        if (StringUtils.isNotBlank(basicPerson.getCardId())) {
            //根据id和身份证获取信息
            QueryWrapper<BasicPerson> queryWrapper = new QueryWrapper();
            if (StringUtils.isNotBlank(basicPerson.getCardId())) {
                queryWrapper.lambda().and(j -> j.eq(BasicPerson::getCardId, basicPerson.getCardId()));
                queryWrapper.lambda().and(i -> i.eq(BasicPerson::getIsDelete, 0));
            }
            List<BasicPerson> pList = basicPersonService.list(queryWrapper);
            if (pList != null && pList.size() > 1) {
                return ResultUtil.error("身份证号码重复,请检查");
            }
            if (pList != null && pList.size() == 1) {
                BasicPerson person = pList.get(0);
                if (!person.getId().equals(basicPerson.getId())) {
                    return ResultUtil.error("身份证号码重复,请检查");
                }
            }
        }
        //户主户号重复判断
        if (StringUtils.isNotBlank(basicPerson.getAccNumber()) && StringUtils.isNotBlank(basicPerson.getAccCard())) {
            QueryWrapper<BasicPerson> queryWrapper = new QueryWrapper();
            queryWrapper.lambda().and(j -> j.eq(BasicPerson::getAccNumber, basicPerson.getAccNumber()));
            queryWrapper.lambda().and(j -> j.eq(BasicPerson::getAccCard, basicPerson.getAccCard()));
            queryWrapper.lambda().and(i -> i.eq(BasicPerson::getIsDelete, 0));
            List<BasicPerson> pList = basicPersonService.list(queryWrapper);
            if (pList != null && pList.size() > 1) {
                return ResultUtil.error("户主重复，请重新录入");
            }
            if (pList != null && pList.size() == 1) {
                BasicPerson person = pList.get(0);
                if (!person.getId().equals(basicPerson.getId())) {
                    return ResultUtil.error("户主重复，请重新录入");
                }
            }
        }

        //电话号码重复判断
        if (StringUtils.isNotBlank(basicPerson.getPhone())) {
            QueryWrapper<BasicPerson> queryWrapper = new QueryWrapper();
            queryWrapper.lambda().and(j -> j.eq(BasicPerson::getPhone, basicPerson.getPhone()));
            queryWrapper.lambda().and(j -> j.ne(BasicPerson::getId, basicPerson.getId()));
            queryWrapper.lambda().and(i -> i.eq(BasicPerson::getIsDelete, 0));

            int pList = basicPersonService.count(queryWrapper);
            if(pList>0){
                return ResultUtil.error("联系方式重复，请重新录入");
            }

        }
        Long time = System.currentTimeMillis();
        {
            JSONObject user1 = new JSONObject();
            user1.put("personId", basicPerson.getId());
            user1.put("realName", basicPerson.getName());
            user1.put("account", basicPerson.getPhone());
            user1.put("sex", basicPerson.getSex());
            user1.put("idNumber", basicPerson.getCardId());
            user1.put("birthday", basicPerson.getBirthDate());
            user1.put("gridId", basicPerson.getOwnedGridId());
            user1.put("label", basicPerson.getTableType());
            user1.put("phone", basicPerson.getPhone());
            user1.put("communityId", basicPerson.getCommunityId());
            //JSONObject jsonObject = baseClient.synchronModify(user1);
//            if (jsonObject.getInteger("code") != 200) {
//                throw new BusinessErrorException("同步异常");
//            }

            JSONObject user2 = new JSONObject();
            user2.put("personId", basicPerson.getId());
            user2.put("name", basicPerson.getName());
            user2.put("sex", basicPerson.getSex());
            user2.put("idCard", basicPerson.getCardId());
            user2.put("phone", basicPerson.getPhone());
            //JSONObject jsonObject1 = entityClient.synchronModify(user2);
//            if (jsonObject1.getInteger("code") != 200) {
//                throw new BusinessErrorException("同步异常");
//            }
        }
        try {

            basicPerson.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean res = basicPersonService.updateById(basicPerson);
            if (res) {
                QueryWrapper<RelaPersonRelatives> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().and(i -> i.eq(RelaPersonRelatives::getPersonId, basicPerson.getId()));
                relaPersonRelativesService.remove(queryWrapper);
                List<RelaPersonRelatives> relativesList = basicPerson.getRelativesList();
                if (relativesList != null) {
                    for (RelaPersonRelatives item : relativesList) {
                        RelaPersonRelatives relaPersonRelatives = new RelaPersonRelatives();
                        relaPersonRelatives.setPersonId(basicPerson.getId());
                        relaPersonRelatives.setName(item.getName());
                        relaPersonRelatives.setPhone(item.getPhone());
                        relaPersonRelatives.setRelationship(item.getRelationship());
                        relaPersonRelatives.setCreateTime(new Timestamp(time));
                        relaPersonRelativesService.save(relaPersonRelatives);
                    }
                }
                JSONObject machine = new JSONObject();
                machine.put("thirdPartyId", basicPerson.getId());
                machine.put("householdName",basicPerson.getName());
                machine.put("mobile",basicPerson.getPhone());
                //thirdPartyClient.saveUpdateHousehold(machine);
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
     * 导出人口数据
     *
     * @param response
     * @param basicPerson
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response,BasicPerson basicPerson) {
        Long time = System.currentTimeMillis();
        try {
            basicPersonService.download(basicPerson, response);
        } catch (Exception e) {
            e.printStackTrace();
//            logService.addErrorLog("导出异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }
    /**
     *导出人口数据（动态字段）
     *
     * @param response
     * @param map  包含查询参数 以及 字段集合
     */
    @PostMapping("/downloadDynamics")
    public void downloadDynamics(HttpServletResponse response, @RequestBody Map<String, Object> map) {
        Long time = System.currentTimeMillis();
        try {
            basicPersonService.downloadDynamics(map, response);
        } catch (Exception e) {
            e.printStackTrace();
//            logService.addErrorLog("导出异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }


    /**
     * 导入数据
     *
     * @param file
     * @param request
     */
    @PostMapping("/uploadPerson")
    public void upload(@RequestParam("excel-file") MultipartFile file, HttpServletRequest request) {
        Long time = System.currentTimeMillis();
        System.out.println(file.getName() + "      " + file.getOriginalFilename());
        String originalFilename = file.getOriginalFilename();
        List<BasicPerson> persenList = new ArrayList<>();
        try {
            if (!originalFilename.matches("^.+\\.(?i)(xls)$") && !originalFilename.matches("^.+\\.(?i)(xlsx)$")) {
                throw new Exception("上传文件格式不正确");
            }
        } catch (Exception e) {
//            logService.addErrorLog("文件错误异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
        }
    }

    /**
     * 统计人员分布
     *
     * @return
     */
    @GetMapping("count")
    public Result<Object> count() {
        //用户数据权限
        UserDto user = securityUtil.getCurrUser();
        BasicPerson basicPerson = null;
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                basicPerson = new BasicPerson();
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){// 街道
                    basicPerson.setStreetId(deptId);
                }else if (attribute == 2) {// 社区
                    basicPerson.setCommunityId(deptId);
                }else if (attribute == 3) {// 网格
                    basicPerson.setOwnedGridId(deptId);
                }
            }
        }
        return ResultUtil.data(basicPersonService.getBasicPersonCount(basicPerson), "查询成功");
    }

    /**
     * 统计特殊人群
     *
     * @return
     */
    @GetMapping("getSpecialPersonCount")
    public Result<Object> getSpecialPersonCount() {
        //用户数据权限
        UserDto user = securityUtil.getCurrUser();
        BasicPerson basicPerson = null;
        if (user.getPower() != null){
            if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                basicPerson = new BasicPerson();
                String deptId = user.getPower().getDeptId();
                Integer attribute = user.getPower().getAttribute();
                if (attribute == 1){// 街道
                    basicPerson.setStreetId(deptId);
                }else if (attribute == 2) {// 社区
                    basicPerson.setCommunityId(deptId);
                }else if (attribute == 3) {// 网格
                    basicPerson.setOwnedGridId(deptId);
                }
            }
        }
        return ResultUtil.data(basicPersonService.getSpecialPersonCount(basicPerson), "查询成功");
    }

    /**
     * 根据小区id查询所有楼栋
     *
     * @param estateId
     * @return
     */
    @GetMapping("getBuildArchiveByEstateId")
    public Result<Object> getBuildArchiveByEstateId(String estateId) {
        return ResultUtil.data(basicPersonService.getBuildArchiveByEstateId(estateId), "查询成功");
    }


    /**
     * 新增老人档案数据
     *
     * @param basicPerson
     * @return
     */
    @PostMapping("addBasicPersonByType2")
    public Result<Object> addBasicPersonByType2(@RequestBody BasicPerson basicPerson) {
        Long time = System.currentTimeMillis();
        try {
            String id = UUID.randomUUID().toString().replaceAll("-", "");

//            if (StringUtils.isNotBlank(basicPerson.getImgPath())) {
//                //base64 转文件
//                MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(basicPerson.getImgPath());
//                //文件存储在nginx代理路径下
//                String fileName = UploadFile.uploadFile(imgFile);
//                basicPerson.setImgPath(fileName);
//            }
            basicPerson.setId(id);
            basicPerson.setIsDelete(0);
            basicPerson.setTableType("2");
            basicPerson.setCreateTime(new Timestamp(time));
            int i = basicPersonService.insert1(basicPerson);
            if (i > 0) {
                List<RelaPersonRelatives> relativesList = basicPerson.getRelativesList();
                if (relativesList != null) {
                    for (RelaPersonRelatives item : relativesList) {
                        RelaPersonRelatives relaPersonRelatives = new RelaPersonRelatives();
                        relaPersonRelatives.setPersonId(id);
                        relaPersonRelatives.setName(item.getName());
                        relaPersonRelatives.setPhone(item.getPhone());
                        relaPersonRelatives.setRelationship(item.getRelationship());
                        relaPersonRelatives.setCreateTime(new Timestamp(time));
                        relaPersonRelativesService.save(relaPersonRelatives);
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
     * 更新老人档案
     *
     * @param basicPerson
     * @return
     */
    @PostMapping("updateBasicPersonByType2")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> updateBasicPersonByType2(@RequestBody BasicPerson basicPerson) {
        if (StringUtils.isBlank(basicPerson.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        Long time = System.currentTimeMillis();
        try {
            basicPerson.setUpdateTime(new Timestamp(time));
//            if (StringUtils.isNotBlank(basicPerson.getImgPath()) && basicPerson.getImageIsUpdate()) {
//                //base64 转文件
//                MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(basicPerson.getImgPath());
//                //文件存储在nginx代理路径下
//                String fileName = UploadFile.uploadFile(imgFile);
//                basicPerson.setImgPath(fileName);
//            }
            boolean res = basicPersonService.updateById(basicPerson);
            if (res) {
                QueryWrapper<RelaPersonRelatives> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().and(i -> i.eq(RelaPersonRelatives::getPersonId, basicPerson.getId()));
                relaPersonRelativesService.remove(queryWrapper);

                List<RelaPersonRelatives> relativesList = basicPerson.getRelativesList();
                if (relativesList != null) {
                    for (RelaPersonRelatives item : relativesList) {
                        RelaPersonRelatives relaPersonRelatives = new RelaPersonRelatives();
                        relaPersonRelatives.setPersonId(basicPerson.getId());
                        relaPersonRelatives.setName(item.getName());
                        relaPersonRelatives.setPhone(item.getPhone());
                        relaPersonRelatives.setRelationship(item.getRelationship());
                        relaPersonRelatives.setCreateTime(new Timestamp(time));
                        relaPersonRelativesService.save(relaPersonRelatives);
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
     * 统计各网格人口数据
     *
     * @return
     */
    @GetMapping("statisticsGridPerson")
    public Result<Object> statisticsGridPerson() {
        Long time = System.currentTimeMillis();
        try {
            return basicPersonService.statisticsGridPerson();
        } catch (Exception e) {
//            logService.addErrorLog("统计异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("统计异常:" + e.getMessage());
        }
    }

    /**
     * 统计人口数据
     *
     * @param community
     * @return
     */
    @GetMapping("statisticsGridPerson2")
    public Result<Object> statisticsGridPerson2(String community) {
        Long time = System.currentTimeMillis();
        try {
            return basicPersonService.statisticsGridPerson2(community);
        } catch (Exception e) {
//            logService.addErrorLog("统计异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("统计异常:" + e.getMessage());
        }
    }

    /**
     * 绑定房屋
     *
     * @param basicPerson
     * @return
     */
    @PostMapping("bindHouse")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> bindHouse(BasicPerson basicPerson) {
        Long time = System.currentTimeMillis();
        if (basicPerson==null||StringUtils.isBlank(basicPerson.getId())||StringUtils.isBlank(basicPerson.getOwnedHousing())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        BasicHousing basicHousing = basicHousingService.getById(basicPerson.getOwnedHousing());
        if(basicHousing==null){
            return ResultUtil.error("当前房屋不存在，请联系管理员！");
        }

        if(StringUtils.isNotBlank(basicPerson.getRelationShip()) && basicPerson.getRelationShip().equals("1")){
            BasicPerson basicPersonOne = basicPersonService.getById(basicPerson.getId());
            if(basicPersonOne==null){
                return ResultUtil.error("当前居民数据不存在，请联系管理员！");
            }
            if(StringUtils.isBlank(basicPersonOne.getPhone())){
                return ResultUtil.error("当前居民手机号不存在，请联系管理员！");
            }
            if(StringUtils.isBlank(basicHousing.getHostContact())){
                return ResultUtil.error("实有房屋户主手机号不存在，请联系管理员！");
            }
            if(!basicPersonOne.getPhone().trim().equals(basicHousing.getHostContact())){
                return ResultUtil.error("当前居民手机号与实有房屋户主手机号'"+basicHousing.getHostContact()+"'不匹配！");
            }
        }

        //判断是否重复
        QueryWrapper<RelaPersonHouse> relativesQueryWrapper = new QueryWrapper<>();
        relativesQueryWrapper.lambda().and(i -> i.eq(RelaPersonHouse::getPersonId, basicPerson.getId()));
        relativesQueryWrapper.lambda().and(i -> i.eq(RelaPersonHouse::getHouseId, basicPerson.getOwnedHousing()));
        List<RelaPersonHouse> list = relaPersonHouseService.list(relativesQueryWrapper);
        if (list == null || list.size() >= 1) {
            return ResultUtil.error("该房屋您已经绑定，请勿重复绑定");
        }

        RelaPersonHouse relaPersonHouse = new RelaPersonHouse();
        relaPersonHouse.setCreateTime(new Timestamp(System.currentTimeMillis()));
        relaPersonHouse.setPersonId(basicPerson.getId());
        relaPersonHouse.setHouseId(basicPerson.getOwnedHousing());
        relaPersonHouse.setIsSettle(basicPerson.getIsSettle());

        //亲属关系不为空，保存关系到中间表
        if (StringUtils.isNotBlank(basicPerson.getRelationShip())) {
            relaPersonHouse.setRelationShip(basicPerson.getRelationShip());
        }
        boolean res = relaPersonHouseService.save(relaPersonHouse);
        if (res) {
            //房屋表房屋状态
            if (StringUtils.isNotBlank(basicPerson.getRentStatus())) {
                //租用状态不为空，保存到房屋表
                BasicHousing bh = basicHousingService.getById(basicPerson.getOwnedHousing());
                bh.setRentStatus(basicPerson.getRentStatus());
                bh.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                basicHousingService.updateById(bh);
            }
//                HttpClient httpClient = new HttpClient();
            JSONObject jsonObjectRes = new JSONObject();
            jsonObjectRes.put("houseId", basicHousing.getHouseId());

            jsonObjectRes.put("id", basicPerson.getOwnedHousing());
            jsonObjectRes.put("relationship", basicPerson.getRelationShip());
            jsonObjectRes.put("personId", basicPerson.getId());

            //todo 合并代码后直接调用方法
            //httpClient.exchange(PropertyClient.syncAddHouseCertification, HttpMethod.POST,jsonObjectRes ,restTemplate);
//            JSONObject jsonObject = entityClient.syncAddHouseCertification(jsonObjectRes);
//            if (jsonObject.containsKey("code") && jsonObject.get("code").toString().equals("200")) {
//            } else {
//                throw new RuntimeException("同步异常");
//            }
            //同步门口机关系
            BasicPerson syncUserInfo = basicPersonService.getById(basicPerson.getId());
            JSONObject machine = new JSONObject();
            machine.put("thirdPartyId", basicPerson.getId());
            machine.put("housingUuid", basicPerson.getOwnedHousing());
            machine.put("householdType", convert(basicPerson.getRelationShip()));
            machine.put("householdName",syncUserInfo.getName());
            machine.put("mobile",syncUserInfo.getPhone());
//            JSONObject data = thirdPartyClient.saveUpdateHousehold(machine);
//            if (!data.containsKey("code") || !data.get("code").toString().equals("200")) {
//                throw new BusinessErrorException(data.get("data").toString());
//            }

            //房屋表房屋状态
            if (StringUtils.isNotBlank(basicPerson.getRentStatus())) {
                //租用状态不为空，保存到房屋表
                BasicHousing bh = basicHousingService.getById(basicPerson.getOwnedHousing());
                bh.setRentStatus(basicPerson.getRentStatus());
                bh.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                basicHousingService.updateById(bh);
            }
            //修改人员表状态
            BasicPerson bPerson = basicPersonService.getById(basicPerson.getId());
            bPerson.setIsBind(1);
            bPerson.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean b = basicPersonService.updateById(bPerson);
            if (b) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } else {
            return ResultUtil.error("保存失败");
        }
    }

    private int convert(String relationShip) {
        int type;
        switch (relationShip){
            case "1":
                type = 1;
                break;
            case "2":
            case "6":
                type = 5;
                break;
            case "3":
                type = 7;
                break;
            case "4":
                type = 6;
                break;
            case "5":
                type = 2;
                break;
            default:
                type = 8;
        }
        return type;
    }

    /**
     * 绑定房屋，app同步
     *
     * @param basicPerson
     * @return
     */
    @PostMapping("bindHouseFromApp")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> bindHouseFromApp(@RequestBody BasicPerson basicPerson) {
        Long time = System.currentTimeMillis();

        if (StringUtils.isBlank(basicPerson.getId())) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }

        //判断是否重复
        QueryWrapper<RelaPersonHouse> relativesQueryWrapper = new QueryWrapper<>();
        relativesQueryWrapper.lambda().and(i -> i.eq(RelaPersonHouse::getPersonId, basicPerson.getId()));
        relativesQueryWrapper.lambda().and(i -> i.eq(RelaPersonHouse::getHouseId, basicPerson.getOwnedHousing()));
        List<RelaPersonHouse> list = relaPersonHouseService.list(relativesQueryWrapper);
        if (list == null || list.size() >= 1) {
            return ResultUtil.error("该房屋您已经绑定，请勿重复绑定");
        }

        RelaPersonHouse relaPersonHouse = new RelaPersonHouse();
        relaPersonHouse.setCreateTime(new Timestamp(System.currentTimeMillis()));
        relaPersonHouse.setPersonId(basicPerson.getId());
        relaPersonHouse.setHouseId(basicPerson.getOwnedHousing());
        relaPersonHouse.setIsSettle(basicPerson.getIsSettle());

        //亲属关系不为空，保存关系到中间表
        if (StringUtils.isNotBlank(basicPerson.getRelationShip())) {
            relaPersonHouse.setRelationShip(basicPerson.getRelationShip());
        }
        boolean res = relaPersonHouseService.save(relaPersonHouse);
        if (res) {
            //房屋表房屋状态
            if (StringUtils.isNotBlank(basicPerson.getRentStatus())) {
                //租用状态不为空，保存到房屋表
                BasicHousing bh = basicHousingService.getById(basicPerson.getOwnedHousing());
                bh.setRentStatus(basicPerson.getRentStatus());
                bh.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                basicHousingService.updateById(bh);
            }

            //同步门口机关系
            BasicPerson syncUserInfo = basicPersonService.getById(basicPerson.getId());
            JSONObject machine = new JSONObject();
            machine.put("thirdPartyId", basicPerson.getId());
            machine.put("housingUuid", basicPerson.getOwnedHousing());
            machine.put("householdType", basicPerson.getRelationShip());
            machine.put("householdName",syncUserInfo.getName());
            machine.put("mobile",syncUserInfo.getPhone());
//            JSONObject data = thirdPartyClient.saveUpdateHousehold(machine);
//            if (!data.containsKey("code") || !data.get("code").toString().equals("200")) {
//                throw new BusinessErrorException(data.get("data").toString());
//            }
            //修改人员表状态
            BasicPerson bPerson = basicPersonService.getById(basicPerson.getId());
            bPerson.setIsBind(1);
            bPerson.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            boolean b = basicPersonService.updateById(bPerson);
            if (b) {
                return ResultUtil.data(res, "保存成功");
            } else {
                return ResultUtil.error("保存失败");
            }
        } else {
            return ResultUtil.error("保存失败");
        }

    }


    /**
     * 解除房屋绑定
     *
     * @param map
     * @return
     */
    @PostMapping("outBind")
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> outBind(@RequestBody Map<String, Object> map) {
        Long time = System.currentTimeMillis();
        if (map == null || map.size() == 0 || !map.containsKey("ids") || !map.containsKey("personId")) {
            return ResultUtil.error("参数为空，请联系管理员！！");
        }
        String personId = map.get("personId").toString();

        boolean flag = true;
        List<String> ids = (List<String>) map.get("ids");
        QueryWrapper<RelaPersonHouse> relativesQueryWrapper = new QueryWrapper<>();
        for (String houseId : ids) {
            relativesQueryWrapper = new QueryWrapper<>();
            //人员id
            relativesQueryWrapper.lambda().and(i -> i.eq(RelaPersonHouse::getPersonId, personId));
            //房屋id
            relativesQueryWrapper.lambda().and(i -> i.eq(RelaPersonHouse::getHouseId, houseId));

            //删除绑定
            {
                //同步门口机关系
                JSONObject machine = new JSONObject();
                machine.put("uuid", personId);
                //thirdPartyClient.operateHousehold(machine);
            }

            boolean remove = relaPersonHouseService.remove(relativesQueryWrapper);

            if (!remove) {
                flag = false;
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                break;
            }
            JSONObject jsonObjectRes = new JSONObject();
            jsonObjectRes.put("houseId", houseId);
            jsonObjectRes.put("personId", personId);
//            JSONObject jsonObject = entityClient.syncDelteHouseCertification(jsonObjectRes);
//            if (!jsonObject.containsKey("code") || !jsonObject.get("code").toString().equals("200")) {
//                throw new RuntimeException("同步异常");
//            }

        }
        if (!flag) {
            return ResultUtil.data(flag, "解绑失败");
        }

        //循环删除之后，再看还有没有绑定
        relativesQueryWrapper = new QueryWrapper<>();
        relativesQueryWrapper.lambda().and(i -> i.eq(RelaPersonHouse::getPersonId, personId));
        List<RelaPersonHouse> list = relaPersonHouseService.list(relativesQueryWrapper);
        if (list == null || list.size() < 1) {
            //更新人员
            BasicPerson byId = basicPersonService.getById(personId);
            byId.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            byId.setIsBind(0);
            boolean b = basicPersonService.updateById(byId);
            if (b) {
                return ResultUtil.data(b, "解绑成功");
            } else {
                return ResultUtil.data(b, "解绑失败");
            }
        } else {
            return ResultUtil.data(flag, "解绑成功");
        }
    }

    /**
     * 身份证信息
     *
     * @param cardId
     * @return
     */
    @GetMapping("getPersonByCardId")
    public Result<Object> getPersonByCardId(@RequestParam(name = "cardId") String cardId) {
        Long time = System.currentTimeMillis();
        try {
            List<Map<String, Object>> card = basicPersonService.getPersonByCardId(cardId);
            return ResultUtil.data(card, "保存成功");
        } catch (Exception e) {
//            logService.addErrorLog("统计异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("统计异常:" + e.getMessage());
        }
    }

    /**
     * 身份证信息
     *
     * @return
     */
    @GetMapping("getCardId")
    public Result<Object> getCardId() {
        Long time = System.currentTimeMillis();
        try {
            List<Map<String, Object>> card = basicPersonService.getCardId();
            return ResultUtil.data(card, "保存成功");
        } catch (Exception e) {
//            logService.addErrorLog("统计异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("统计异常:" + e.getMessage());
        }
    }

    /**
     * 房屋id查询人口信息
     *
     * @param houseId
     * @return
     */
    @GetMapping("getPersonByHouseId")
    public Result<Object> getPersonByHouseId(String houseId) {
        Long time = System.currentTimeMillis();
        try {
            List<BasicPerson> card = basicPersonService.getPersonByHouseId(houseId);
            return ResultUtil.data(card, "查询成功");
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 分页查询户籍人口
     *
     * @param basicPerson
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryAccPersonList")
    public Result<Object> queryAccPersonList(BasicPerson basicPerson, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            Page<BasicPerson> pagePerson = basicPersonService.queryAccPerson(basicPerson, searchVo, pageVo);
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
     * 根据户主id查询户籍其他的人
     *
     * @param personId
     * @return
     */
    @GetMapping("queryAccPerson")
    public Result<Object> queryAccPerson(String personId) {
        Long time = System.currentTimeMillis();
        try {
            List<BasicPerson> basicPeople = null;
            BasicPerson basicPerson = basicPersonService.getById(personId);
            if (StringUtils.isNotBlank(basicPerson.getAccNumber())) {
                basicPeople = basicPersonService.queryBasicPersonNoPage(basicPerson.getAccNumber(), personId);
                //查询人口下房屋
                QueryWrapper<RelaPersonHouse> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().and(i -> i.eq(RelaPersonHouse::getPersonId, personId));
                queryWrapper.lambda().and(i -> i.eq(RelaPersonHouse::getIsSettle, "是"));
                List<RelaPersonHouse> list = relaPersonHouseService.list(queryWrapper);
                if (list.size() > 0) {
                    BasicHousing res = basicHousingService.getById(list.get(0).getHouseId());
                    String houseName = res.getOwnedGrid().trim() + res.getHostName().trim() + "小区" + res.getBuildArchiveName().trim() + "号楼" + res.getUnit().trim() + "单元" + res.getFloor().trim() + res.getDoorNumber().trim() + "号";
                    basicPerson.setOwnedHouseid(houseName);
                }
            }

            if (basicPeople != null) {

                basicPerson.setAccBasicPerson(basicPeople);
                return ResultUtil.data(basicPerson, "查询成功");
            } else {
                return ResultUtil.data(basicPerson, "查询失败");
            }
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /**
     * 根据房屋id查询查询户籍
     *
     * @param houseId
     * @return
     */
    @GetMapping("getPersonFromRelaByBasicHouseId")
    public Result<Object> queryAccPersonByHouseId(String houseId) {
        Long time = System.currentTimeMillis();
        try {
            List<BasicPerson> card = basicPersonService.getPersonByHouseId(houseId);
            if (card == null || card.size() == 0) {
                return ResultUtil.data(card, "查询成功,此房屋下还未关联户籍");
            }

            card = card.stream()
                    .filter((BasicPerson s) -> s.getPersonType().contains("户籍"))
                    .collect(Collectors.toList());


            if (card.size() == 0) {

                return ResultUtil.data(card, "查询成功,此房屋下还未关联户籍");
            } else {
                return ResultUtil.data(card, "查询失败");
            }
        } catch (Exception e) {
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }

    /***
     *根据类型查询对应的数据
     * @param type 网格长（gridManagement） 网格员(gridMan) 社区干部(communityOfficer)  街道志愿者(streetVolunteers)
     * @return
     */
    @GetMapping("getPersonDataByType")
    public Result<Object> getPersonDataByType(String type) {
        switch (type) {
            case "gridManagement":
                TZhsqGridMember tZhsqGridMember = new TZhsqGridMember();
                tZhsqGridMember.setPost("网格长");
                tZhsqGridMember.setIsDelete(0);
                return tZhsqGridMemberService.queryGridMemberList(tZhsqGridMember, null);
            case "gridMan":
                tZhsqGridMember = new TZhsqGridMember();
                tZhsqGridMember.setPost("网格员");
                tZhsqGridMember.setIsDelete(0);
                return tZhsqGridMemberService.queryGridMemberList(tZhsqGridMember, null);
            case "communityOfficer":
                TZhsqCommunityCadres tZhsqCommunityCadres = new TZhsqCommunityCadres();
                tZhsqCommunityCadres.setIsDelete(0);
                return zhsqCommunityCadresService.queryCommunityCadresList(tZhsqCommunityCadres);
            case "streetVolunteers":
                QueryWrapper<TZhsqVolunteer> tZhsqVolunteerQueryWrapper = new QueryWrapper<>();
                tZhsqVolunteerQueryWrapper.lambda().and(i -> i.eq(TZhsqVolunteer::getIsDelete, 0));
                List<TZhsqVolunteer> list = itZhsqVolunteerService.list(tZhsqVolunteerQueryWrapper);
                return ResultUtil.data(list);
        }
        return ResultUtil.data(null);
    }

    /**
     * Excel导入人口信息数据
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
            return basicPersonService.importExcel(file);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.error("保存异常" + e.getMessage());
        }
    }

    /**
     * 判断身份证是否重复
     *
     * @param idCard
     * @return
     */
    public boolean checkIdCard(String idCard) {
        boolean isExist = false;
        if (StringUtils.isNotBlank(idCard)) {
            QueryWrapper<BasicPerson> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().and(i -> i.eq(BasicPerson::getCardId, idCard));
            queryWrapper.lambda().and(i -> i.eq(BasicPerson::getIsDelete, 0));
            List<BasicPerson> list = basicPersonService.list(queryWrapper);
            for (BasicPerson p : list) {
                if (idCard.equals(p.getCardId())) {
                    isExist = true;
                    break;
                }
            }
        }
        return isExist;
    }

    /**
     * 判断户号是否重复
     *
     * @param accNumber
     * @return
     */
    public boolean checkAccNumber(String accNumber, String idCard) {
        boolean isExist = false;
        if (StringUtils.isNotBlank(accNumber) && StringUtils.isNotBlank(idCard)) {
            QueryWrapper<BasicPerson> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().and(i -> i.eq(BasicPerson::getAccNumber, accNumber));
            queryWrapper.lambda().and(i -> i.eq(BasicPerson::getAccCard, idCard));
            queryWrapper.lambda().and(i -> i.eq(BasicPerson::getIsDelete, 0));
            List<BasicPerson> list = basicPersonService.list(queryWrapper);
            if (list != null && list.size() > 1) {
                isExist = true;
            }
        }
        return isExist;
    }

    /**
     * 判断电话号码是否重复
     *
     * @param phone
     * @return
     */
    public boolean checkPhone (String phone) {
        boolean isExist = false;
        if (StringUtils.isNotBlank(phone) ) {
            QueryWrapper<BasicPerson> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().and(i -> i.eq(BasicPerson::getPhone, phone));
            queryWrapper.lambda().and(i -> i.eq(BasicPerson::getIsDelete, 0));
            List<BasicPerson> list = basicPersonService.list(queryWrapper);
            if (list != null && list.size() > 1) {
                isExist = true;
            }
        }
        return isExist;
    }

    /**
     * 查询当前社区下男女比例
     *
     * @param communityId
     * @param gridId
     * @return
     */
    @GetMapping("getPersonDataMaleToFemaleratio")
    public Result<Object> getPersonDataMaleToFemaleratio(String communityId, String gridId) {
        Long time = System.currentTimeMillis();
        try {
            Map<String, Object> result = basicPersonService.getPersonDataMaleToFemaleratio(communityId, gridId);
            return ResultUtil.data(result);
        } catch (Exception e) {
            return ResultUtil.error("查询异常" + e.getMessage());
        }
    }


    /**
     * 统计人口类型数量
     *
     * @param communityId
     * @param gridId
     * @return
     */
    @GetMapping("getBasicPersonCountByPersonType")
    public Result<Object> getBasicPersonCountByPersonType(String communityId, String gridId) {
        return ResultUtil.data(basicPersonService.getBasicPersonCountByPersonType(communityId, gridId), "查询成功");
    }

    /**
     * 统计人员类型（老人、精神病人....）
     *
     * @param communityId
     * @param gridId
     * @return
     */
    @GetMapping("getBasicPersonCountByPopulation")
    public Result<Object> getBasicPersonCountByPopulation(String communityId, String gridId) {
        return ResultUtil.data(basicPersonService.getBasicPersonCountByPopulation(communityId, gridId), "查询成功");
    }

    /**
     * 分页查询人口档案
     *
     * @param basicPerson
     * @param searchVo
     * @param pageVo
     * @return
     */
    @GetMapping("queryBasicPersonDynamicList")
    public Result<Object> queryBasicPersonDynamicList(BasicPerson basicPerson, SearchVo searchVo, PageVo pageVo) {
        Long time = System.currentTimeMillis();
        try {
            //用户数据权限
            UserDto user = securityUtil.getCurrUser();
            if (user.getPower() != null){
                if (StringUtils.isNotBlank(user.getPower().getDeptId()) && user.getPower().getAttribute() != null ){
                    String deptId = user.getPower().getDeptId();
                    Integer attribute = user.getPower().getAttribute();
                    if (attribute == 1){// 街道
                        basicPerson.setStreetId(deptId);
                    }else if (attribute == 2) {// 社区
                        basicPerson.setCommunityId(deptId);
                    }else if (attribute == 3) {// 网格
                        basicPerson.setOwnedGridId(deptId);
                    }
                }
            }
            return ResultUtil.data(basicPersonService.queryBasicPersonDynamicList(basicPerson, searchVo, pageVo));
        } catch (Exception e) {
            e.printStackTrace();
//            logService.addErrorLog("查询异常", this.getClass().getName(), time - System.currentTimeMillis(), e);
            return ResultUtil.error("查询异常:" + e.getMessage());
        }
    }
}
