package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.mapper.*;
import me.zhengjie.entity.BasicHousing;
import me.zhengjie.entity.TBuildingArchives;
import me.zhengjie.entity.TZhsqMerchantProfile;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.entity.TZhsqVolunteer;
import me.zhengjie.service.ITZhsqMerchantProfileService;
import me.zhengjie.utils.FileUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.text.SimpleDateFormat;

/**
 * @author
 **/
@Service
@AllArgsConstructor
public class TZhsqMerchantProfileServiceImpl extends ServiceImpl<TZhsqMerchantProfileMapper, TZhsqMerchantProfile> implements ITZhsqMerchantProfileService {
   
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final TZhsqMerchantProfileMapper tZhsqMerchantProfileMapper;

    private final BasicHousingMapper basicHousingMapper;

    private final TBuildingArchivesMapper tBuildingArchivesMapper;

    private final TZhsqVolunteerMapper tZhsqVolunteerMapper;

    private final BasicPersonMapper basicPersonMapper;

    private final TZhsqGridMemberMapper tZhsqGridMemberMapper;

    public Result<Object> getTZhsqMerchantProfileById(String id) {
        TZhsqMerchantProfile tZhsqMerchantProfile = tZhsqMerchantProfileMapper.selectById(id);
        if (tZhsqMerchantProfile != null) {
            return ResultUtil.data(tZhsqMerchantProfile);
        }
        return ResultUtil.error("获取据败，失败原因：查无此数据！");
    }

    @Override
    public Result<Object> queryTZhsqMerchantProfileListByPage(TZhsqMerchantProfile tZhsqMerchantProfile, SearchVo searchVo, PageVo pageVo) {
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
        Page<TZhsqMerchantProfile> pageData = new Page<>(page, limit);
        QueryWrapper<TZhsqMerchantProfile> queryWrapper = new QueryWrapper<>();
        if (tZhsqMerchantProfile != null) {
            queryWrapper = LikeAllFeild(tZhsqMerchantProfile, searchVo, null);
        }
        if(pageVo.getSort()!=null){
            if(pageVo.getSort().equals("asc")){
                queryWrapper.orderByAsc(pageVo.getSort());
            }
            else{
                queryWrapper.orderByDesc(pageVo.getSort());
            }
        }
        else{
            queryWrapper.orderByDesc("create_time");
        }
        IPage<TZhsqMerchantProfile> result = tZhsqMerchantProfileMapper.selectPage(pageData, queryWrapper);
        return ResultUtil.data(result);
    }

    @Override
    public void download(TZhsqMerchantProfile tZhsqMerchantProfile, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<TZhsqMerchantProfile> queryWrapper = new QueryWrapper<>();
        if (tZhsqMerchantProfile != null) {
            queryWrapper = LikeAllFeild(tZhsqMerchantProfile, null, null);
        }
        List<TZhsqMerchantProfile> list = tZhsqMerchantProfileMapper.selectByMyWrapper(queryWrapper);
        for (TZhsqMerchantProfile re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("商户类别", re.getMerchantSort());
            map.put("商户名称", re.getMerchantName());
            map.put("商户地址", re.getMerchantAddress());
            map.put("经营范围", re.getBusinessScope());
            map.put("法人姓名", re.getLegalEntity());
            map.put("法人联系电话", re.getLegalPhone());
            map.put("所属社区", re.getLegalCommunity());
            map.put("所属网格", re.getLegalGrid());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 分页查询商户数据数据
     * @param tZhsqMerchantProfile
     * @param key
     * @param pageVo
     * @return
     */
    @Override
    public Result<Object> queryTZhsqMerchantProfileListByAnyWayWhere(TZhsqMerchantProfile tZhsqMerchantProfile, String key, PageVo pageVo) {
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
        Page<TZhsqMerchantProfile> pageData = new Page<>(page, limit);
        QueryWrapper<TZhsqMerchantProfile> queryWrapper = new QueryWrapper<>();
        if (tZhsqMerchantProfile != null) {
            queryWrapper = LikeAllFeild(tZhsqMerchantProfile, null, key);
        }
        //IPage<TZhsqMerchantProfile> result = tZhsqMerchantProfileMapper.selectPage(pageData, queryWrapper);
        List<TZhsqMerchantProfile> tZhsqMerchantProfiles = tZhsqMerchantProfileMapper.selectList(queryWrapper);
        for (TZhsqMerchantProfile record : tZhsqMerchantProfiles) {
            //查询对应的房屋、匹配坐标position
            if(StringUtils.isNotEmpty(record.getHouseId())){
                BasicHousing basicHousing = basicHousingMapper.selectById(record.getHouseId());
                if(basicHousing != null){
                    TBuildingArchives tBuildingArchives = tBuildingArchivesMapper.selectById(basicHousing.getBuildArchiveId());
                    if(tBuildingArchives != null){
                        record.setLocation(tBuildingArchives.getPosition());
                    }
                }
            }
        }
        return ResultUtil.data(tZhsqMerchantProfiles);
    }

    @Override
    public Map<String, Object> selectByCommunity(String communityId,String gridId) {
        Map<String, Object> map = new HashMap<>();
        //志愿者
        QueryWrapper<TZhsqVolunteer> queryWrapper = new QueryWrapper();
        if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(communityId)){
            queryWrapper.lambda().and(i -> i.eq(TZhsqVolunteer::getCommunityId, communityId));
        }
        if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(gridId)){
            queryWrapper.lambda().and(i -> i.eq(TZhsqVolunteer::getOwnedGrid, gridId));
        }
        int volunteer = tZhsqVolunteerMapper.selectCount(queryWrapper);
        map.put("volunteer",volunteer);
        //网格员
        int  gridMember  = tZhsqGridMemberMapper.selectByCommunityIdAndGridId(communityId,gridId);
        map.put("gridMember",gridMember);
        //人员类型
        List<Map<String, Object>> basicPersonCountByPersonType = basicPersonMapper.getBasicPersonCountByPersonType(communityId, gridId);
        mapList(basicPersonCountByPersonType);
        map.put("personType",basicPersonCountByPersonType);
        //特殊人群
        List<Map<String, Object>> basicPersonCountByPopulation = basicPersonMapper.getBasicPersonCountByPopulation(communityId, gridId);
        mapList(basicPersonCountByPopulation);
        map.put("population",basicPersonCountByPopulation);

        //人群标签
        List<Map<String, Object>> basicPersonCountByTableType = basicPersonMapper.getTableTypeCount(communityId, gridId);
        mapList(basicPersonCountByTableType);
        map.put("tableType",basicPersonCountByTableType);

        //房屋类型
        List<Map<String, Object>> houseType = tZhsqMerchantProfileMapper.getHouseType(communityId, gridId);
        mapList(houseType);
        map.put("houseType",houseType);

        //建筑类型
        List<Map<String, Object>> buildingType = tZhsqMerchantProfileMapper.getBuildingType(communityId, gridId);
        mapList(buildingType);
        map.put("buildingType",buildingType.get(0));

        //商户类型
        List<Map<String, Object>> merchantType = tZhsqMerchantProfileMapper.getMerchantType(communityId, gridId);
        mapList(merchantType);
        map.put("merchantType",merchantType.get(0));
        //社会组织
        List<Map<String, Object>> socialType = tZhsqMerchantProfileMapper.getSocialType(communityId, gridId);
        mapList(socialType);
        map.put("socialType",socialType.get(0));

        //事件紧急程度
        List<Map<String, Object>> urgentType = tZhsqMerchantProfileMapper.getUrgentType(communityId, gridId);
        mapList(urgentType);
        map.put("urgentType",urgentType.get(0));

        return map;
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tZhsqMerchantProfile 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TZhsqMerchantProfile> LikeAllFeild(TZhsqMerchantProfile tZhsqMerchantProfile, SearchVo searchVo, String key) {
        QueryWrapper<TZhsqMerchantProfile> queryWrapper = new QueryWrapper<>();
        if (tZhsqMerchantProfile.getId() != null) {
            queryWrapper.lambda().and(i -> i.eq(TZhsqMerchantProfile::getId, tZhsqMerchantProfile.getId()));
        }
        if (StringUtils.isNotBlank(tZhsqMerchantProfile.getMerchantSort())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqMerchantProfile::getMerchantSort, tZhsqMerchantProfile.getMerchantSort()));
        }
        if (StringUtils.isNotBlank(tZhsqMerchantProfile.getMerchantName())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqMerchantProfile::getMerchantName, tZhsqMerchantProfile.getMerchantName()));
        }
        if (StringUtils.isNotBlank(tZhsqMerchantProfile.getMerchantAddress())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqMerchantProfile::getMerchantAddress, tZhsqMerchantProfile.getMerchantAddress()));
        }
        if (StringUtils.isNotBlank(tZhsqMerchantProfile.getBusinessScope())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqMerchantProfile::getBusinessScope, tZhsqMerchantProfile.getBusinessScope()));
        }
        if (StringUtils.isNotBlank(tZhsqMerchantProfile.getLegalEntity())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqMerchantProfile::getLegalEntity, tZhsqMerchantProfile.getLegalEntity()));
        }
        if (StringUtils.isNotBlank(tZhsqMerchantProfile.getLegalPhone())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqMerchantProfile::getLegalPhone, tZhsqMerchantProfile.getLegalPhone()));
        }
        if (StringUtils.isNotBlank(tZhsqMerchantProfile.getStreet())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqMerchantProfile::getStreet, tZhsqMerchantProfile.getStreet()));
        }
        if (StringUtils.isNotBlank(tZhsqMerchantProfile.getStreetId())) {
            queryWrapper.lambda().and(i -> i.eq(TZhsqMerchantProfile::getStreetId, tZhsqMerchantProfile.getStreetId()));
        }
        if (StringUtils.isNotBlank(tZhsqMerchantProfile.getLegalCommunity())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqMerchantProfile::getLegalCommunity, tZhsqMerchantProfile.getLegalCommunity()));
        }
        if (StringUtils.isNotBlank(tZhsqMerchantProfile.getLegalCommunityId())) {
            queryWrapper.lambda().and(i -> i.eq(TZhsqMerchantProfile::getLegalCommunityId, tZhsqMerchantProfile.getLegalCommunityId()));
        }
        if (StringUtils.isNotBlank(tZhsqMerchantProfile.getLegalGrid())) {
            queryWrapper.lambda().and(i -> i.like(TZhsqMerchantProfile::getLegalGrid, tZhsqMerchantProfile.getLegalGrid()));
        }
        if (StringUtils.isNotBlank(tZhsqMerchantProfile.getGridId())) {
            queryWrapper.lambda().and(i -> i.eq(TZhsqMerchantProfile::getGridId, tZhsqMerchantProfile.getGridId()));
        }
        if (tZhsqMerchantProfile.getCreateId() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqMerchantProfile::getCreateId, tZhsqMerchantProfile.getCreateId()));
        }
        if (tZhsqMerchantProfile.getCreateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqMerchantProfile::getCreateTime, tZhsqMerchantProfile.getCreateTime()));
        }
        if (tZhsqMerchantProfile.getUpdateId() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqMerchantProfile::getUpdateId, tZhsqMerchantProfile.getUpdateId()));
        }
        if (tZhsqMerchantProfile.getUpdateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqMerchantProfile::getUpdateTime, tZhsqMerchantProfile.getUpdateTime()));
        }
        if (tZhsqMerchantProfile.getIsDelete() != null) {
            queryWrapper.lambda().and(i -> i.like(TZhsqMerchantProfile::getIsDelete, tZhsqMerchantProfile.getIsDelete()));
        }

        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.lambda().and(i -> i.between(TZhsqMerchantProfile::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
            //搜索条件
            if (StringUtils.isNotEmpty(searchVo.getSearchInfo())) {
                queryWrapper.lambda().and(i -> i.like(TZhsqMerchantProfile::getLegalEntity, searchVo.getSearchInfo())

                        .or().like(TZhsqMerchantProfile::getMerchantName, searchVo.getSearchInfo())
                        .or().like(TZhsqMerchantProfile::getMerchantAddress, searchVo.getSearchInfo())
                );
            }
        }
        queryWrapper.lambda().and(i -> i.eq(TZhsqMerchantProfile::getIsDelete, 0));


        if(StringUtils.isNotEmpty(key)){
            queryWrapper.and(wrapper -> wrapper.like("merchant_sort", key).or().like("merchant_name", key).or().like("merchant_address", key).or().like("legal_entity", key));
        }
        return queryWrapper;

    }

    /**
     * 遍历map
     * @param maps
     */
    public void mapList(List<Map<String, Object>> maps){
        if(maps!=null && maps.size()>0){
            for (Map<String, Object> map : maps) {
                if(map!=null){
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        if (entry.getKey() != null && entry.getValue() == null) {
                            map.put(entry.getKey(), 0);
                        }
                    }
                }

            }
        }

    }
}
