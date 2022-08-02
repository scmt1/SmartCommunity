package me.zhengjie.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.BusinessException;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;

import me.zhengjie.mapper.BasicHousingEstateMapper;
import me.zhengjie.mapper.GridDeptMapper;
import me.zhengjie.mapper.TZhsqPropertyManagementMapper;
import me.zhengjie.entity.BasicHousingEstate;
import me.zhengjie.entity.TZhsqPropertyManagement;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.service.ITZhsqPropertyManagementService;
import lombok.AllArgsConstructor;
import me.zhengjie.system.repository.DictDetailRepository;
import me.zhengjie.util.*;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * @author
 **/
@Service
@AllArgsConstructor
public class TZhsqPropertyManagementServiceImpl extends ServiceImpl<TZhsqPropertyManagementMapper, TZhsqPropertyManagement> implements ITZhsqPropertyManagementService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final TZhsqPropertyManagementMapper tZhsqPropertyManagementMapper;

    private final BasicHousingEstateMapper basicHousingEstateMapper;

    private final DictDetailRepository dictDetailRepository;

    private final GridDeptMapper deptMapper;

    @Override
    public Result<Object> getTZhsqPropertyManagementById(String id) {
        TZhsqPropertyManagement tZhsqPropertyManagement = tZhsqPropertyManagementMapper.selectById(id);
        if (tZhsqPropertyManagement != null) {
            return ResultUtil.data(tZhsqPropertyManagement);
        }
        return ResultUtil.error("获取据败，失败原因：查无此数据！");
    }

    @Override
    public Result<Object> queryTZhsqPropertyManagementListByPage(TZhsqPropertyManagement tZhsqPropertyManagement, SearchVo searchVo, PageVo pageVo) {
        int page = 1;
        int limit = 10;
        if (pageVo != null) {
            if (pageVo.getPageNumber() != 0) {
                page = pageVo.getPageNumber();
            }
            if (pageVo.getPageSize() != 0) {
                limit = pageVo.getPageSize();
            }
        }
        Page<TZhsqPropertyManagement> pageData = new Page<>(page, limit);
        QueryWrapper<TZhsqPropertyManagement> queryWrapper = new QueryWrapper<>();
        if (tZhsqPropertyManagement != null) {
            queryWrapper = LikeAllFeild(tZhsqPropertyManagement, searchVo);
        }
        if (pageVo.getSort() != null) {
            if (pageVo.getSort().equals("asc")) {
                queryWrapper.orderByAsc(pageVo.getSort());
            } else {
                queryWrapper.orderByDesc(pageVo.getSort());
            }
        } else {
            queryWrapper.orderByDesc("create_time");
        }
        IPage<TZhsqPropertyManagement> result = tZhsqPropertyManagementMapper.selectPage(pageData, queryWrapper);
        return ResultUtil.data(result);
    }

    @Override
    public Result<Object> queryTZhsqPropertyManagementListByPageWithGridId(TZhsqPropertyManagement tZhsqPropertyManagement, String gridId, SearchVo searchVo, PageVo pageVo) {
        int page = 1;
        int limit = 10;
        if (pageVo != null) {
            if (pageVo.getPageNumber() != 0) {
                page = pageVo.getPageNumber();
            }
            if (pageVo.getPageSize() != 0) {
                limit = pageVo.getPageSize();
            }
        }
        QueryWrapper<BasicHousingEstate> queryWrapper = new QueryWrapper<>();
        if (tZhsqPropertyManagement != null) {
//            queryWrapper = LikeAllFeild(tZhsqPropertyManagement,searchVo);

            if (StringUtils.isNotBlank(tZhsqPropertyManagement.getPropertyName())) {
                queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getPropertyName, tZhsqPropertyManagement.getPropertyName()));
            }
            if (StringUtils.isNotBlank(tZhsqPropertyManagement.getId())) {
                queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getPropertyNameId, tZhsqPropertyManagement.getId()));
            }
            if (StringUtils.isNotBlank(tZhsqPropertyManagement.getPropertyType())) {
                queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getPropertyType, tZhsqPropertyManagement.getPropertyType()));
            }
            if (StringUtils.isNotBlank(tZhsqPropertyManagement.getPropertyPrincipal())) {
                queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getPropertyPerson, tZhsqPropertyManagement.getPropertyPrincipal()));
            }
            if (StringUtils.isNotBlank(tZhsqPropertyManagement.getPropertyPrincipalPhone())) {
                queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getPropertyPhone, tZhsqPropertyManagement.getPropertyPrincipalPhone()));
            }
            if (StringUtils.isNotBlank(gridId)) {
                queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getGridId, gridId));
            }
        }
        if (pageVo.getSort() != null) {
            if (pageVo.getSort().equals("asc")) {
                queryWrapper.orderByAsc(pageVo.getSort());
            } else {
                queryWrapper.orderByDesc(pageVo.getSort());
            }
        } else {
            queryWrapper.orderByDesc("create_time");
        }
        queryWrapper.groupBy("property_name_id");
        Page<BasicHousingEstate> pageData = new Page<>(page, limit);
        IPage<BasicHousingEstate> result = basicHousingEstateMapper.selectPage(pageData, queryWrapper);
//        IPage<TZhsqPropertyManagement> result = tZhsqPropertyManagementMapper.selectPage(pageData, queryWrapper);
        return ResultUtil.data(result);
    }

    @Override
    public void download(TZhsqPropertyManagement tZhsqPropertyManagement, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<TZhsqPropertyManagement> queryWrapper = new QueryWrapper<>();
        if (tZhsqPropertyManagement != null) {
            queryWrapper = LikeAllFeild(tZhsqPropertyManagement, null);
        }
        List<TZhsqPropertyManagement> list = tZhsqPropertyManagementMapper.selectByMyWrapper(queryWrapper);
//         dictDetailRepository.findAll(DictionaryUtil.communityPriorityData);
        for (TZhsqPropertyManagement re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("物业名称", re.getPropertyName());
//            map.put("物业类型", DictionaryUtil.getNameByNumber(maps,re.getPropertyType()));
            map.put("物业类型", re.getPropertyType());
            map.put("物业负责人", re.getPropertyPrincipal());
            map.put("物业负责人电话", re.getPropertyPrincipalPhone());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 模糊查询所有物业信息
     *
     * @param propertyManagement
     * @return
     */
    @Override
    public Result<Object> queryAllList(TZhsqPropertyManagement propertyManagement) {
        QueryWrapper<TZhsqPropertyManagement> queryWrapper = new QueryWrapper<>();
        if (propertyManagement != null) {
            queryWrapper = LikeAllFeild(propertyManagement, new SearchVo());
        }
        List<TZhsqPropertyManagement> selectList = tZhsqPropertyManagementMapper.selectList(queryWrapper);
        return ResultUtil.data(selectList);
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tZhsqPropertyManagement 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TZhsqPropertyManagement> LikeAllFeild(TZhsqPropertyManagement tZhsqPropertyManagement, SearchVo searchVo) {
        QueryWrapper<TZhsqPropertyManagement> queryWrapper = new QueryWrapper<>();
        if (tZhsqPropertyManagement.getId() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqPropertyManagement::getId, tZhsqPropertyManagement.getId()));
        }
        if (StringUtils.isNotBlank(tZhsqPropertyManagement.getPropertyName())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqPropertyManagement::getPropertyName, tZhsqPropertyManagement.getPropertyName()));
        }
        if (StringUtils.isNotBlank(tZhsqPropertyManagement.getPropertyType())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqPropertyManagement::getPropertyType, tZhsqPropertyManagement.getPropertyType()));
        }
        if (StringUtils.isNotBlank(tZhsqPropertyManagement.getPropertyPrincipal())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqPropertyManagement::getPropertyPrincipal, tZhsqPropertyManagement.getPropertyPrincipal()));
        }
        if (StringUtils.isNotBlank(tZhsqPropertyManagement.getPropertyPrincipalPhone())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqPropertyManagement::getPropertyPrincipalPhone, tZhsqPropertyManagement.getPropertyPrincipalPhone()));
        }
        if (tZhsqPropertyManagement.getCreateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqPropertyManagement::getCreateTime, tZhsqPropertyManagement.getCreateTime()));
        }
        if (tZhsqPropertyManagement.getCreateId() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqPropertyManagement::getCreateId, tZhsqPropertyManagement.getCreateId()));
        }
        if (tZhsqPropertyManagement.getUpdateId() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqPropertyManagement::getUpdateId, tZhsqPropertyManagement.getUpdateId()));
        }
        if (tZhsqPropertyManagement.getUpdateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqPropertyManagement::getUpdateTime, tZhsqPropertyManagement.getUpdateTime()));
        }
        if (tZhsqPropertyManagement.getIsDelete() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqPropertyManagement::getIsDelete, tZhsqPropertyManagement.getIsDelete()));
        }
        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.lambda().and(i -> i.between(TZhsqPropertyManagement::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
            //搜索条件
            if (StringUtils.isNotEmpty(searchVo.getSearchInfo())) {
                queryWrapper.lambda().and(i -> i.like(TZhsqPropertyManagement::getPropertyName, searchVo.getSearchInfo())
                        .or().like(TZhsqPropertyManagement::getPropertyPrincipal, searchVo.getSearchInfo())
                        .or().like(TZhsqPropertyManagement::getPropertyPrincipalPhone, searchVo.getSearchInfo())
                );
            }
        }
        queryWrapper.lambda().and(i -> i.eq(TZhsqPropertyManagement::getIsDelete, 0));
        return queryWrapper;
    }

    @Override
    public Result<Object> importExcel(MultipartFile multipartFile) throws Exception {
        File file = FileUtil.toFile(multipartFile);
        InputStream in = new FileInputStream(file);
        Workbook wb = ImportExeclUtil.chooseWorkbook(file.getName(), in);
        //读取一个对象的信息
        TZhsqPropertyManagement tZhsqGridMember = new TZhsqPropertyManagement();
        List<TZhsqPropertyManagement> readData = ImportExeclUtil.readDateListT(wb, tZhsqGridMember, 2, 0);
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Boolean isMatchingData = false;
            isMatchingData = matchingData(readData, result);
            if (isMatchingData) {
                return ResultUtil.data(result);
            }
            for (TZhsqPropertyManagement basicGrids : readData) {
                if(basicGrids==null||BeanUtil.checkObjAllFieldsIsNull(basicGrids)){
                    continue;
                }
                if (StringUtils.isBlank(basicGrids.getPropertyName()) && StringUtils.isBlank(basicGrids.getPropertyType()) && StringUtils.isBlank(basicGrids.getPropertyPrincipal()) && StringUtils.isBlank(basicGrids.getPropertyPrincipalPhone())) {
                    continue;
                }
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                basicGrids.setIsDelete(0);
                basicGrids.setId(uuid);
                basicGrids.setCreateTime(new Timestamp(System.currentTimeMillis()));
                int insert = tZhsqPropertyManagementMapper.insert(basicGrids);
                if (insert != 1) {
                    throw new BusinessErrorException("保存失败，请联系管理员");
                }
                JSONObject jsonObjectRes = new JSONObject();
                jsonObjectRes.put("uuid", uuid);
                jsonObjectRes.put("name", basicGrids.getPropertyName());
                jsonObjectRes.put("principalName", basicGrids.getPropertyPrincipal());
                jsonObjectRes.put("principalPhone", basicGrids.getPropertyPrincipalPhone());

                //todo 合并代码后直接调用方法
                //httpClient.exchange(PropertyClient.initPropertyCompanyByGrid, HttpMethod.POST,jsonObjectRes ,restTemplate);
//                        JSONObject jsonObject = entityClient.initPropertyCompanyByGrid(jsonObjectRes);
//                        if(jsonObject.containsKey("code") && jsonObject.get("code").toString().equals("200")){
//                        }
//                        else {
//                            throw new RuntimeException("同步异常");
//                        }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.FAILURE);
        }

        return ResultUtil.data(result);
    }

    /**
     * 匹配数据
     */
    public Boolean matchingData(List<TZhsqPropertyManagement> readData, List<Map<String, Object>> result) {
        if (result == null) {
            result = new ArrayList<>();
        }
        Boolean isMatchingData = false;

        DictionaryUtils dictionaryUtils = new DictionaryUtils(deptMapper);
        dictionaryUtils.init();


        if (readData != null && readData.size() > 0) {
            for (int i = 0; i < readData.size(); i++) {
                TZhsqPropertyManagement basicGrids = readData.get(i);
                if (basicGrids != null) {
                    if(BeanUtil.checkObjAllFieldsIsNull(basicGrids)){
                        continue;
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("number", i + 1);
                    map.put("success", "成功");
                    if (StringUtils.isBlank(basicGrids.getPropertyName()) && StringUtils.isBlank(basicGrids.getPropertyType()) && StringUtils.isBlank(basicGrids.getPropertyPrincipal()) && StringUtils.isBlank(basicGrids.getPropertyPrincipalPhone())) {
                         continue;
                    }
                    String msg = "";
                    //物业名称
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(basicGrids.getPropertyName())) {
                        msg += "物业名称为空\n";
                    } else {
                        QueryWrapper<TZhsqPropertyManagement> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().and(item -> item.eq(TZhsqPropertyManagement::getIsDelete, 0));
                        queryWrapper.lambda().and(item -> item.eq(TZhsqPropertyManagement::getPropertyName, basicGrids.getPropertyName()));
                        Integer integer = tZhsqPropertyManagementMapper.selectCount(queryWrapper);
                        //数据库重复
                        if (integer > 0) {
                            msg += "物业名称重复\n";
                        }
                        //map 重复
                        else {
                            for (int j = i - 1; j >= 0; j--) {
                                TZhsqPropertyManagement basicGrids1 = readData.get(j);
                                if (basicGrids1 != null && com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getPropertyName())) {
                                    if (basicGrids.getPropertyName().equals(basicGrids1.getPropertyName())) {
                                        msg += "与第" + (j + 1) + "条数据物业名称重复\n";
                                        break;
                                    }
                                }

                            }
                        }
                    }

                    //物业类型
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getPropertyType())) {
                        String s = dictionaryUtils.propertyType(basicGrids.getPropertyType());
                        if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(s)) {
                            basicGrids.setPropertyType(s);
                        } else {
                            msg += "物业类型未匹配上，请检查当前物业类型（字典）是否入库\n";
                        }
                    } else {
                        msg += "物业类型为空\n";
                    }
                    //物业负责人
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(basicGrids.getPropertyPrincipal())) {
                        msg += "物业负责人为空\n";
                    }
                    //物业负责人联系方式
                    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(basicGrids.getPropertyPrincipalPhone())) {
                        boolean chinaPhoneLegal = PhoneUtils.isPhoneLegal(basicGrids.getPropertyPrincipalPhone());
                        if (!chinaPhoneLegal) {
                            msg += "物业负责人联系方式格式不正确，请检查\n";
                        }
                    } else {
                        msg += "物业负责人联系方式为空\n";
                    }
                    if (msg.length() > 0) {
                        isMatchingData = true;
                        map.put("success", "失败");
                    }
                    map.put("msg", msg);
                    result.add(map);
                }
            }
        }

        return isMatchingData;
    }


    @Override
    public Boolean isSame(String propertyName, String id) {
        QueryWrapper<TZhsqPropertyManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().and(j -> j.eq(TZhsqPropertyManagement::getPropertyName, propertyName));
        if (!id.isEmpty()) {
            queryWrapper.lambda().and(j -> j.ne(TZhsqPropertyManagement::getId, id));
        }
        queryWrapper.lambda().and(j -> j.eq(TZhsqPropertyManagement::getIsDelete, 0));
        List<TZhsqPropertyManagement> tZhsqPropertyManagements = tZhsqPropertyManagementMapper.selectList(queryWrapper);
        if (tZhsqPropertyManagements.size() > 0) {
            return false;//有相同值
        } else {
            return true;//没有相同值
        }

    }

    @Override
    public int insert(TZhsqPropertyManagement propertyManagement) {
        return tZhsqPropertyManagementMapper.insert(propertyManagement);
    }

}
