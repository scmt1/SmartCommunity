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

import me.zhengjie.entity.*;
import me.zhengjie.global.GridTree;
import me.zhengjie.mapper.GridDeptMapper;
import me.zhengjie.util.*;
import me.zhengjie.mapper.BasicGridsMapper;
import me.zhengjie.mapper.BasicHousingEstateMapper;
import me.zhengjie.mapper.TZhsqPropertyManagementMapper;


import javax.servlet.http.HttpServletResponse;

import me.zhengjie.service.IBasicHousingEstateService;

import me.zhengjie.utils.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
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
public class BasicHousingEstateServiceImpl extends ServiceImpl<BasicHousingEstateMapper, BasicHousingEstate> implements IBasicHousingEstateService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final BasicHousingEstateMapper basicHousingEstateMapper;

    private final BasicGridsMapper basicGridsMapper;

    private final TZhsqPropertyManagementMapper propertyManagementMapper;

    private final TZhsqPropertyManagementMapper tZhsqPropertyManagementMapper;

    private final GridDeptMapper deptMapper;

    private final GridTree gridTree;

    @Override
    public BasicHousingEstate getBasicHousingEstateById(String id) {
        BasicHousingEstate basicHousingEstate = basicHousingEstateMapper.selectById(id);
        if (basicHousingEstate != null) {
            TZhsqPropertyManagement tZhsqPropertyManagement = tZhsqPropertyManagementMapper.selectById(basicHousingEstate.getPropertyNameId());
            if (tZhsqPropertyManagement != null) {
                basicHousingEstate.setPropertyName(tZhsqPropertyManagement.getPropertyName());
            }

            return basicHousingEstate;
        }
        return null;
    }

    @Override
    public Result<Object> queryBasicHousingEstateListByPage(BasicHousingEstate basicHousingEstate, SearchVo searchVo, PageVo pageVo) {
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
        Page<BasicHousingEstate> pageData = new Page<>(page, limit);
        QueryWrapper<BasicHousingEstate> queryWrapper = new QueryWrapper<>();
        if (basicHousingEstate != null) {
            queryWrapper = LikeAllFeild(basicHousingEstate, searchVo);
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
        IPage<BasicHousingEstate> result = basicHousingEstateMapper.selectPage(pageData, queryWrapper);
        return ResultUtil.data(result);
    }

    @Override
    public void download(BasicHousingEstate basicHousingEstate, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<BasicHousingEstate> queryWrapper = new QueryWrapper<>();
        if (basicHousingEstate != null) {
            queryWrapper = LikeAllFeild(basicHousingEstate, null);
        }
        List<BasicHousingEstate> list = basicHousingEstateMapper.selectByMyWrapper(queryWrapper);
        for (BasicHousingEstate re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("小区名称", re.getName());
            map.put("小区地址", re.getAddress());
            map.put("物业名称", re.getPropertyName());
            map.put("物业类型", re.getPropertyType());
            map.put("物业负责人", re.getPropertyPerson());
            map.put("物业电话", re.getPropertyPhone());
            map.put("所属社区", re.getCommunity());
            map.put("所属网格", re.getGrid());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param basicHousingEstate 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<BasicHousingEstate> LikeAllFeild(BasicHousingEstate basicHousingEstate, SearchVo searchVo) {
        QueryWrapper<BasicHousingEstate> queryWrapper = new QueryWrapper<>();
        if (basicHousingEstate.getId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getId, basicHousingEstate.getId()));
        }
        if (StringUtils.isNotBlank(basicHousingEstate.getName())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getName, basicHousingEstate.getName()));
        }
        if (StringUtils.isNotBlank(basicHousingEstate.getAddress())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getAddress, basicHousingEstate.getAddress()));
        }
        if (StringUtils.isNotBlank(basicHousingEstate.getPropertyName())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getPropertyName, basicHousingEstate.getPropertyName()));
        }
        if (StringUtils.isNotBlank(basicHousingEstate.getPropertyNameId())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getPropertyNameId, basicHousingEstate.getPropertyNameId()));
        }
        if (StringUtils.isNotBlank(basicHousingEstate.getPropertyType())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getPropertyType, basicHousingEstate.getPropertyType()));
        }
        if (StringUtils.isNotBlank(basicHousingEstate.getPropertyPerson())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getPropertyPerson, basicHousingEstate.getPropertyPerson()));
        }
        if (StringUtils.isNotBlank(basicHousingEstate.getPropertyPhone())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getPropertyPhone, basicHousingEstate.getPropertyPhone()));
        }
        if (StringUtils.isNotBlank(basicHousingEstate.getStreetId())) {
            queryWrapper.lambda().and(i -> i.eq(BasicHousingEstate::getStreetId, basicHousingEstate.getStreetId()));
        }
        if (StringUtils.isNotBlank(basicHousingEstate.getCommunityId())) {
            queryWrapper.lambda().and(i -> i.eq(BasicHousingEstate::getCommunityId, basicHousingEstate.getCommunityId()));
        }
        if (StringUtils.isNotBlank(basicHousingEstate.getGridId())) {
            queryWrapper.lambda().and(i -> i.eq(BasicHousingEstate::getGridId, basicHousingEstate.getGridId()));
        }
        if (StringUtils.isNotBlank(basicHousingEstate.getGrid())) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getGrid, basicHousingEstate.getGrid()));
        }

        if (basicHousingEstate.getLocation() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getLocation, basicHousingEstate.getLocation()));
        }
        if (basicHousingEstate.getIsDelete() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getIsDelete, basicHousingEstate.getIsDelete()));
        }
        if (basicHousingEstate.getStreet() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getStreet, basicHousingEstate.getStreet()));
        }
        if (basicHousingEstate.getStreetNumber() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getStreetNumber, basicHousingEstate.getStreetNumber()));
        }
        if (basicHousingEstate.getBak3() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getBak3, basicHousingEstate.getBak3()));
        }
        if (basicHousingEstate.getBak4() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getBak4, basicHousingEstate.getBak4()));
        }
        if (basicHousingEstate.getBak5() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getBak5, basicHousingEstate.getBak5()));
        }
        if (basicHousingEstate.getRemark() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getRemark, basicHousingEstate.getRemark()));
        }
        if (basicHousingEstate.getCreateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getCreateTime, basicHousingEstate.getCreateTime()));
        }
        if (basicHousingEstate.getCreateId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getCreateId, basicHousingEstate.getCreateId()));
        }
        if (basicHousingEstate.getUpdateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getUpdateTime, basicHousingEstate.getUpdateTime()));
        }
        if (basicHousingEstate.getUpdateId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getUpdateId, basicHousingEstate.getUpdateId()));
        }
        if (basicHousingEstate.getDeleteId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getDeleteId, basicHousingEstate.getDeleteId()));
        }
        if (basicHousingEstate.getDeleteTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getDeleteTime, basicHousingEstate.getDeleteTime()));
        }
        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.lambda().and(i -> i.between(BasicHousingEstate::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
            //搜索条件
            if (StringUtils.isNotEmpty(searchVo.getSearchInfo())) {
                queryWrapper.lambda().and(i -> i.like(BasicHousingEstate::getName, searchVo.getSearchInfo())
                        .or().like(BasicHousingEstate::getAddress, searchVo.getSearchInfo())
                );
            }
        }
        queryWrapper.lambda().and(i -> i.eq(BasicHousingEstate::getIsDelete, 0));
        return queryWrapper;

    }

    @Override
    public List<BasicHousingEstate> queryAllList(BasicHousingEstate basicHousingEstate) {

        QueryWrapper<BasicHousingEstate> queryWrapper = new QueryWrapper<>();
        if (basicHousingEstate != null) {
            queryWrapper = LikeAllFeild(basicHousingEstate, null);
        }
        List<BasicHousingEstate> selectList = basicHousingEstateMapper.selectList(queryWrapper);
        return selectList;
    }

    @Override
    public Result<Object> importExcel(MultipartFile multipartFile) throws Exception {
        File file = FileUtil.toFile(multipartFile);
        InputStream in = new FileInputStream(file);
        Workbook wb = ImportExeclUtil.chooseWorkbook(file.getName(), in);
        //读取一个对象的信息
        BasicHousingEstate tZhsqGridMember = new BasicHousingEstate();
        List<BasicHousingEstate> readData = ImportExeclUtil.readDateListT(wb, tZhsqGridMember, 2, 0);
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Boolean isMatchingData = false;
            isMatchingData = matchingData(readData, result);
            if (isMatchingData) {
                return ResultUtil.data(result);
            }
            for (BasicHousingEstate basicGrids : readData) {
                if(basicGrids==null||BeanUtil.checkObjAllFieldsIsNull(basicGrids)){
                    continue;
                }
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                basicGrids.setIsDelete(0);
                basicGrids.setId(uuid);
                basicGrids.setCreateTime(new Timestamp(System.currentTimeMillis()));
                int insert = basicHousingEstateMapper.insert(basicGrids);
                if (insert != 1) {
                    throw new BusinessErrorException("保存失败，请联系管理员");
                }
                JSONObject jsonObjectRes = new JSONObject();
                jsonObjectRes.put("companyUuid", basicGrids.getPropertyNameId());
                jsonObjectRes.put("gridUid", uuid);
                jsonObjectRes.put("gridId", basicGrids.getGridId());
                jsonObjectRes.put("name", basicGrids.getName());
                jsonObjectRes.put("buildYear", "");
                jsonObjectRes.put("provinceCityDistrict", basicGrids.getGrid());
                jsonObjectRes.put("adress", basicGrids.getAddress());
                jsonObjectRes.put("phone", basicGrids.getPropertyPhone());
                //todo 合并代码后直接调用方法
//                        JSONObject jsonObject = entityClient.synchroProperty(jsonObjectRes);
//                        if (jsonObject.containsKey("code") && jsonObject.get("code").toString().equals("200")) {
//                        } else {
//                            throw new RuntimeException("同步异常");
//                        }
//                        JSONObject dnkParams = new JSONObject();
//                        dnkParams.put("communityName",readData.get(i).getName());
//                        dnkParams.put("thirdPartyId",readData.get(i).getId());
//                        JSONObject resultJson = thirdPartyClient.addOrUpdateCommunity(dnkParams);
//                        if ("200".equals(resultJson.getString("code"))){

//                        } else {
//                            throw new BusinessErrorException("同步失败");
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
    public Boolean matchingData(List<BasicHousingEstate> readData, List<Map<String, Object>> result) {
        if (result == null) {
            result = new ArrayList<>();
        }
        Boolean isMatchingData = false;
        StreetUtil streetUtil = new StreetUtil(deptMapper);
        streetUtil.Init();

        DictionaryUtils dictionaryUtils = new DictionaryUtils(deptMapper);
        dictionaryUtils.init();

        GridUtil gridUtil = new GridUtil(basicGridsMapper);
        gridUtil.init();

        if (readData != null && readData.size() > 0) {
            for (int i = 0; i < readData.size(); i++) {
                BasicHousingEstate basicGrids = readData.get(i);
                if(basicGrids==null||BeanUtil.checkObjAllFieldsIsNull(basicGrids)){
                    continue;
                }
                if (basicGrids != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("number", i + 1);
                    map.put("success", "成功");

                    String msg = "";
                    //小区名称
                    if (StringUtils.isBlank(basicGrids.getName())) {
                        msg += "小区名称为空\n";
                    } else {
                        QueryWrapper<BasicHousingEstate> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().and(item -> item.eq(BasicHousingEstate::getIsDelete, 0));
                        queryWrapper.lambda().and(item -> item.eq(BasicHousingEstate::getName, basicGrids.getName()));
                        queryWrapper.lambda().and(item -> item.eq(BasicHousingEstate::getCommunity, basicGrids.getCommunity()));
                        queryWrapper.lambda().and(item -> item.eq(BasicHousingEstate::getStreet, basicGrids.getStreet()));
                        queryWrapper.lambda().and(item -> item.eq(BasicHousingEstate::getGrid, basicGrids.getGrid()));
                        Integer integer = basicHousingEstateMapper.selectCount(queryWrapper);
                        //数据库重复
                        if (integer > 0) {
                            msg += "小区名称重复\n";
                        }
                        //map 重复
                        else {
                            for (int j = i - 1; j >= 0; j--) {
                                BasicHousingEstate basicGrids1 = readData.get(j);
                                if (basicGrids1 != null && StringUtils.isNotBlank(basicGrids.getName()) &&  StringUtils.isNotBlank(basicGrids.getGrid()) &&  StringUtils.isNotBlank(basicGrids.getCommunity()) &&  StringUtils.isNotBlank(basicGrids.getStreet())) {
                                    if (basicGrids.getName().equals(basicGrids1.getName()) && basicGrids.getCommunity().equals(basicGrids1.getCommunity()) && basicGrids.getStreet().equals(basicGrids1.getStreet())) {
                                        msg += "与第" + (j + 1) + "条数据小区名称重复\n";
                                        break;
                                    }
                                }

                            }
                        }
                    }

                    //所属街道
                    if (StringUtils.isNotBlank(basicGrids.getStreet())) {
                        String s = StreetUtil.matchingStreet(basicGrids.getStreet(), streetUtil.streetDepts);
                        if (StringUtils.isNotBlank(s)) {
                            basicGrids.setStreetId(s);
                        } else {
                            msg += "所属街道未匹配上，请检查所属街道是否入库\n";
                        }
                    } else {
                        msg += "所属街道为空\n";
                    }
                    //所属社区
                    if (StringUtils.isNotBlank(basicGrids.getCommunity())) {
                        String s = StreetUtil.matchingCommunity(basicGrids.getStreet(), basicGrids.getCommunity(), streetUtil.gridDepts);
                        if (StringUtils.isNotBlank(s)) {
                            basicGrids.setCommunityId(s);
                        } else {
                            msg += "所属社区未匹配上，请检查所属社区是否入库\n";
                        }
                    } else {
                        msg += "所属社区为空\n";
                    }

                    //所属网格
                    if (StringUtils.isNotBlank(basicGrids.getGrid())) {
                        String s = GridUtil.matchingGrid(basicGrids.getGrid(), basicGrids.getCommunity(), basicGrids.getStreet(), gridUtil.basicGrids);
                        if (StringUtils.isNotBlank(s)) {
                            basicGrids.setGridId(s);
                        } else {
                            msg += "所属网格未匹配上，请检查当前所属网格是否入库\n";
                        }
                    } else {
                        msg += "所属网格为空\n";
                    }
                    //物业名称
                    if (StringUtils.isNotBlank(basicGrids.getPropertyName())) {
                        QueryWrapper<TZhsqPropertyManagement> queryWrapper = new QueryWrapper();
                        queryWrapper.lambda().and(j -> j.eq(TZhsqPropertyManagement::getPropertyName, basicGrids.getPropertyName()));
                        queryWrapper.lambda().and(j -> j.eq(TZhsqPropertyManagement::getIsDelete, 0));
                        List<TZhsqPropertyManagement> tZhsqPropertyManagements = propertyManagementMapper.selectByMyWrapper(queryWrapper);
                        //数据库重复
                        if (tZhsqPropertyManagements!=null && tZhsqPropertyManagements.size() <= 0) {
                            msg += "当前物业未入库，请先添加物业\n";
                        }
                        else{
                            basicGrids.setPropertyNameId(tZhsqPropertyManagements.get(0).getId());
                        }
                    } else {
                        msg += "物业名称为空\n";
                    }
                    //物业类型
                    if (StringUtils.isNotBlank(basicGrids.getPropertyType())) {
                        String s = dictionaryUtils.propertyType(basicGrids.getPropertyType());
                        if (StringUtils.isNotBlank(s)) {
                            basicGrids.setPropertyType(s);
                        } else {
                            msg += "物业类型未匹配上，请检查当前物业类型（字典）是否入库\n";
                        }
                    } else {
                        msg += "物业类型为空\n";
                    }
                    //物业负责人
                    if (StringUtils.isBlank(basicGrids.getPropertyPerson())) {
                        msg += "物业负责人为空\n";
                    }
                    //物业负责人联系方式
                    if (StringUtils.isNotBlank(basicGrids.getPropertyPhone())) {
                        boolean chinaPhoneLegal = PhoneUtils.isPhoneLegal(basicGrids.getPropertyPhone());
                        if (!chinaPhoneLegal) {
                            msg += "物业负责人联系方式格式不正确，请检查\n";
                        }
                    } else {
                        msg += "物业负责人联系方式为空\n";
                    }

                    //省份（小区地址）
                    if (StringUtils.isBlank(basicGrids.getProvince())) {
                        msg += "省份（小区地址）为空\n";
                    }

                    //城市（小区地址）
                    if (StringUtils.isBlank(basicGrids.getCity())) {
                        msg += "省份（小区地址）为空\n";
                    }
                    //*区县（小区地址）
                    if (StringUtils.isBlank(basicGrids.getCity())) {
                        msg += "省份（小区地址）为空\n";
                    }
                    //详细地址
                    if (StringUtils.isBlank(basicGrids.getAddress())) {
                        msg += "省份（小区地址）为空\n";
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
    public List<Map<String, Object>> loadAllBySelectFromGrid(JSONObject query) {
        QueryWrapper<BasicHousingEstate> queryWrapper = new QueryWrapper<>();
        String address = query.getString("address");
        String streetId = query.getString("streetId");
        String communityId = query.getString("communityId");
        String gridIds = query.getString("gridId");
        if (StringUtils.isNotBlank(address)) {
            queryWrapper.lambda().like(BasicHousingEstate::getAddress, address).or().like(BasicHousingEstate::getName, address);
        }
        if (StringUtils.isNotBlank(streetId)) {
            queryWrapper.lambda().eq(BasicHousingEstate::getStreetId, streetId);
        }
        if (StringUtils.isNotBlank(communityId)) {
            queryWrapper.lambda().eq(BasicHousingEstate::getCommunityId, communityId);
        }
        if (StringUtils.isNotBlank(gridIds)) {
            queryWrapper.lambda().eq(BasicHousingEstate::getGridId, gridIds);
        }
        queryWrapper.lambda().eq(BasicHousingEstate::getIsDelete, 0);
        queryWrapper.lambda().select(BasicHousingEstate::getId, BasicHousingEstate::getName, BasicHousingEstate::getAddress, BasicHousingEstate::getGridId);
        List<Map<String, Object>> maps = basicHousingEstateMapper.selectMaps(queryWrapper);
        for (Map<String, Object> map : maps) {
            String gridId = map.get("grid_id").toString();
            try {
                GridTree.Record gridInfomation = gridTree.getGridInfomation(gridId);
                map.put("gridName", gridInfomation.getName());
                map.put("gridId", gridId);
                map.put("communityName", gridInfomation.getCommunityName());
                map.put("communityId", gridInfomation.getCommunityId());
                map.put("streetName", gridInfomation.getStreetName());
                map.put("streetId", gridInfomation.getStreetId());
            } catch (Exception e) {
                map.put("gridName", "");
                map.put("gridId", "");
                map.put("communityName", "");
                map.put("communityId", "");
                map.put("streetName", "");
                map.put("streetId", "");
            }
        }
        return maps;
    }
}
