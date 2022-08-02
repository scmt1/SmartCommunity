package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;

import me.zhengjie.entity.BasicPerson;
import me.zhengjie.mapper.BasicHousingMapper;
import me.zhengjie.mapper.BasicSocialOrganizationMapper;
import me.zhengjie.mapper.TBuildingArchivesMapper;
import me.zhengjie.entity.BasicHousing;
import me.zhengjie.entity.BasicSocialOrganization;
import me.zhengjie.entity.TBuildingArchives;
import me.zhengjie.service.IBasicSocialOrganizationService;
import me.zhengjie.utils.FileUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author
 **/
@Service
@AllArgsConstructor
public class BasicSocialOrganizationServiceImpl extends ServiceImpl<BasicSocialOrganizationMapper, BasicSocialOrganization> implements IBasicSocialOrganizationService {
  
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final BasicSocialOrganizationMapper basicSocialOrganizationMapper;

  
    private final BasicHousingMapper basicHousingMapper;

  
    private final TBuildingArchivesMapper tBuildingArchivesMapper;

    @Override
    public Result<Object> getBasicSocialOrganizationById(String id) {
        BasicSocialOrganization basicSocialOrganization = basicSocialOrganizationMapper.selectById(id);
        if (basicSocialOrganization != null) {
            return ResultUtil.data(basicSocialOrganization);
        }
        return ResultUtil.error("获取据败，失败原因：查无此数据！");
    }

    @Override
    public Result<Object> queryBasicSocialOrganizationListByPage(BasicSocialOrganization basicSocialOrganization, SearchVo searchVo, PageVo pageVo) {
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
        Page<BasicSocialOrganization> pageData = new Page<>(page, limit);
        QueryWrapper<BasicSocialOrganization> queryWrapper = new QueryWrapper<>();
        if (basicSocialOrganization != null) {
            queryWrapper = LikeAllFeild(basicSocialOrganization, searchVo,null);
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
        IPage<BasicSocialOrganization> result = basicSocialOrganizationMapper.selectPage(pageData, queryWrapper);
        return ResultUtil.data(result);
    }

    @Override
    public void download(BasicSocialOrganization basicSocialOrganization, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<BasicSocialOrganization> queryWrapper = new QueryWrapper<>();
        if (basicSocialOrganization != null) {
            queryWrapper = LikeAllFeild(basicSocialOrganization, null,null);
        }
        List<BasicSocialOrganization> list = basicSocialOrganizationMapper.selectByMyWrapper(queryWrapper);
        for (BasicSocialOrganization re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("社会组织类别", re.getType());
            map.put("社会组织名称", re.getName());
            map.put("社会组织地址", re.getAddress());
            map.put("法人", re.getLegalPerson());
            map.put("法人联系方式", re.getLegalPhone());
            map.put("所属街道", re.getStreetName());
            map.put("所属社区", re.getCommunityName());
            map.put("所属网格", re.getGridsName());
            map.put("网格长", re.getGridPersonName());
            map.put("统一社会信用代码", re.getOrgCode());
            map.put("治安负责人", re.getPolicePrincipal());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 社会组织按社区统计
     * @return
     */
    @Override
    public List<Map<String, Object>> getCommunityCountData(BasicSocialOrganization basicSocialOrganization) {
        return basicSocialOrganizationMapper.getCommunityCountData(basicSocialOrganization);
    }

    /**
     * 社会组织按网格统计
     * @return
     */
    @Override
    public List<Map<String, Object>> getGridsCountData(BasicSocialOrganization basicSocialOrganization) {
        return basicSocialOrganizationMapper.getGridsCountData(basicSocialOrganization);
    }


    /**
     * 功能描述：实现分页查询
     * 用于一张图显示
     *
     * @param key    查询参数
     * @param pageVo 分页参数
     * @return 返回获取结果
     */
    @Override
    public Result<Object> queryBasicSocialOrganizationListByAnyWayWhere(BasicSocialOrganization basicSocialOrganization, String key, PageVo pageVo) {
        List<BasicSocialOrganization> basicSocialOrganizations =null;
        QueryWrapper<BasicSocialOrganization> queryWrapper = new QueryWrapper<>();
        if (basicSocialOrganization != null) {
            queryWrapper = LikeAllFeild(basicSocialOrganization, null, key);
        }
        IPage<BasicSocialOrganization> result = null;
        boolean isPage=true;
        int page = 1;
        int limit = 10;
        if (pageVo != null && pageVo.getPageNumber() != 0 && pageVo.getPageSize() != 0) {
            if (pageVo.getPageNumber() != 0) {
                page = pageVo.getPageNumber();
            }
            if (pageVo.getPageSize() != 0) {
                limit = pageVo.getPageSize();
            }
            Page<BasicSocialOrganization> pageData = new Page<>(page, limit);


            result = basicSocialOrganizationMapper.selectPage(pageData, queryWrapper);
            basicSocialOrganizations = result.getRecords();
        }
        else {
            isPage = false;
            basicSocialOrganizations = basicSocialOrganizationMapper.selectList(queryWrapper);
        }

        for (BasicSocialOrganization record : basicSocialOrganizations) {
            //查询对应的房屋、匹配坐标position
            if(StringUtils.isNotBlank(record.getAddressId())){
                BasicHousing basicHousing = basicHousingMapper.selectById(record.getAddressId());
                if(basicHousing != null){
                    TBuildingArchives tBuildingArchives = tBuildingArchivesMapper.selectById(basicHousing.getBuildArchiveId());
                    if(tBuildingArchives != null){
                        record.setPosition(tBuildingArchives.getPosition());
                    }
                }
            }
        }
        if(isPage){
            return ResultUtil.data(result);
        }
        return ResultUtil.data(basicSocialOrganizations);
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param basicSocialOrganization 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<BasicSocialOrganization> LikeAllFeild(BasicSocialOrganization basicSocialOrganization, SearchVo searchVo, String key) {
        QueryWrapper<BasicSocialOrganization> queryWrapper = new QueryWrapper<>();
        if (basicSocialOrganization.getId() != null) {
            queryWrapper.lambda().and(i -> i.eq(BasicSocialOrganization::getId, basicSocialOrganization.getId()));
        }

        if (StringUtils.isNotBlank(basicSocialOrganization.getType())) {
            queryWrapper.lambda().and(i -> i.eq(BasicSocialOrganization::getType, basicSocialOrganization.getType()));
        }
        if (StringUtils.isNotBlank(basicSocialOrganization.getName())) {
            queryWrapper.lambda().and(i -> i.like(BasicSocialOrganization::getName, basicSocialOrganization.getName()));
        }
        if (StringUtils.isNotBlank(basicSocialOrganization.getAddress())) {
            queryWrapper.lambda().and(i -> i.like(BasicSocialOrganization::getAddress, basicSocialOrganization.getAddress()));
        }
        if (StringUtils.isNotBlank(basicSocialOrganization.getLegalPerson())) {
            queryWrapper.lambda().and(i -> i.like(BasicSocialOrganization::getLegalPerson, basicSocialOrganization.getLegalPerson()));
        }
        if (StringUtils.isNotBlank(basicSocialOrganization.getLegalPhone())) {
            queryWrapper.lambda().and(i -> i.like(BasicSocialOrganization::getLegalPhone, basicSocialOrganization.getLegalPhone()));
        }
        if (StringUtils.isNotBlank(basicSocialOrganization.getLegalCard())) {
            queryWrapper.lambda().and(i -> i.like(BasicSocialOrganization::getLegalCard, basicSocialOrganization.getLegalCard()));
        }
        if (StringUtils.isNotBlank(basicSocialOrganization.getStreetId())) {
            //queryWrapper.lambda().and(i -> i.eq(BasicSocialOrganization::getStreetId, basicSocialOrganization.getStreetId()));
            String[] streetIds = basicSocialOrganization.getStreetId().split(",");
            queryWrapper.lambda().and(i -> i.in(BasicSocialOrganization::getStreetId, streetIds));
        }
        if (StringUtils.isNotBlank(basicSocialOrganization.getStreetName())) {
            queryWrapper.lambda().and(i -> i.eq(BasicSocialOrganization::getStreetName, basicSocialOrganization.getStreetName()));
        }


        if (StringUtils.isNotBlank(basicSocialOrganization.getCommunityId())) {
            queryWrapper.lambda().and(i -> i.eq(BasicSocialOrganization::getCommunityId, basicSocialOrganization.getCommunityId()));
        }
        if (StringUtils.isNotBlank(basicSocialOrganization.getCommunityName())) {
            queryWrapper.lambda().and(i -> i.eq(BasicSocialOrganization::getCommunityName, basicSocialOrganization.getCommunityName()));
        }

        if (StringUtils.isNotBlank(basicSocialOrganization.getGridsId())) {
            queryWrapper.lambda().and(i -> i.eq(BasicSocialOrganization::getGridsId, basicSocialOrganization.getGridsId()));
        }
        if (StringUtils.isNotBlank(basicSocialOrganization.getGridPersonId())) {
            queryWrapper.lambda().and(i -> i.eq(BasicSocialOrganization::getGridPersonId, basicSocialOrganization.getGridPersonId()));
        }
        if (StringUtils.isNotBlank(basicSocialOrganization.getGridPersonName())) {
            queryWrapper.lambda().and(i -> i.eq(BasicSocialOrganization::getGridPersonName, basicSocialOrganization.getGridPersonName()));
        }
        if (StringUtils.isNotBlank(basicSocialOrganization.getOrgCode())) {
            queryWrapper.lambda().and(i -> i.like(BasicSocialOrganization::getOrgCode, basicSocialOrganization.getOrgCode()));
        }
        if (basicSocialOrganization.getPrincipalName() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicSocialOrganization::getPrincipalName, basicSocialOrganization.getPrincipalName()));
        }
        if (basicSocialOrganization.getPrincipalPhone() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicSocialOrganization::getPrincipalPhone, basicSocialOrganization.getPrincipalPhone()));
        }
        if (StringUtils.isNotBlank(basicSocialOrganization.getPolicePrincipal())) {
            queryWrapper.lambda().and(i -> i.like(BasicSocialOrganization::getPolicePrincipal, basicSocialOrganization.getPolicePrincipal()));
        }
        if (basicSocialOrganization.getIsDelete() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicSocialOrganization::getIsDelete, basicSocialOrganization.getIsDelete()));
        }
        if (basicSocialOrganization.getCreateId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicSocialOrganization::getCreateId, basicSocialOrganization.getCreateId()));
        }
        if (basicSocialOrganization.getCreateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicSocialOrganization::getCreateTime, basicSocialOrganization.getCreateTime()));
        }
        if (basicSocialOrganization.getUpdateId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicSocialOrganization::getUpdateId, basicSocialOrganization.getUpdateId()));
        }
        if (basicSocialOrganization.getUpdateTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicSocialOrganization::getUpdateTime, basicSocialOrganization.getUpdateTime()));
        }
        if (basicSocialOrganization.getDeleteId() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicSocialOrganization::getDeleteId, basicSocialOrganization.getDeleteId()));
        }
        if (basicSocialOrganization.getDeleteTime() != null) {
            queryWrapper.lambda().and(i -> i.like(BasicSocialOrganization::getDeleteTime, basicSocialOrganization.getDeleteTime()));
        }
        queryWrapper.lambda().and(i -> i.eq(BasicSocialOrganization::getIsDelete, 0));


		if (org.apache.commons.lang3.StringUtils.isNotEmpty(key)) {
			queryWrapper.and(wrapper -> wrapper.like("name", key).or().like("type", key).or().like("legal_person", key).or().like("address", key));
		}
        return queryWrapper;

    }
}
