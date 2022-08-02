package me.zhengjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.PageVo;
import me.zhengjie.common.vo.Result;
import me.zhengjie.common.vo.SearchVo;
import me.zhengjie.entity.BasicHousingManage;
import me.zhengjie.mapper.BasicHousingManageMapper;
import me.zhengjie.service.IBasicHousingManageService;
import me.zhengjie.utils.FileUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 *@author
 **/
@Service
@AllArgsConstructor
public class BasicHousingManageServiceImpl extends ServiceImpl<BasicHousingManageMapper, BasicHousingManage> implements IBasicHousingManageService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final BasicHousingManageMapper basicHousingManageMapper;

    @Override
    public Result<Object> getBasicHousingManageById(String id){
        BasicHousingManage basicHousingManage = basicHousingManageMapper.selectById(id);
        if(basicHousingManage!=null){
            return  ResultUtil.data(basicHousingManage);
        }
        return  ResultUtil.error("获取据败，失败原因：查无此数据！");
    }

    @Override
    public Result<Object> queryBasicHousingManageListByPage(BasicHousingManage basicHousingManage, SearchVo searchVo, PageVo pageVo){
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
        Page<BasicHousingManage> pageData = new Page<>(page, limit);
        QueryWrapper<BasicHousingManage> queryWrapper = new QueryWrapper<>();
        if (basicHousingManage !=null) {
            queryWrapper = LikeAllFeild(basicHousingManage,searchVo);
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
        IPage<BasicHousingManage> result = basicHousingManageMapper.selectPage(pageData, queryWrapper);
        return  ResultUtil.data(result);
    }
    @Override
    public void download(BasicHousingManage basicHousingManage, HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<BasicHousingManage> queryWrapper = new QueryWrapper<>();
        if (basicHousingManage !=null) {
            queryWrapper = LikeAllFeild(basicHousingManage,null);
        }
        List<BasicHousingManage> list = basicHousingManageMapper.selectList(queryWrapper);
        for (BasicHousingManage re : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("所属街道", re.getStreet());
            map.put("所属社区", re.getCommunity());
            map.put("所属网格", re.getOwnedGrid());
            map.put("小区名称", re.getHouseName());
            map.put("房屋详址", re.getHouseAddress());
            map.put("房主姓名", re.getHouseHost());
            map.put("身份证号", re.getIdCard());
            map.put("租客姓名", re.getCustomerName());
            map.put("租客身份证号", re.getCustomerIdcard());
            map.put("租客电话", re.getCustomerMobile());
            map.put("房屋类型", re.getHouseType());
            map.put("租用状态", re.getRentStatus());
            map.put("楼栋名称",re.getBuildArchivesName());
            map.put("门牌号",re.getDoorNumber());
            mapList.add(map);
        }
        FileUtil.createExcel(mapList, "exel.xlsx", response);
    }

    /**
     * 功能描述：构建模糊查询
     * @param basicHousingManage 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<BasicHousingManage>  LikeAllFeild(BasicHousingManage  basicHousingManage, SearchVo searchVo) {
        QueryWrapper<BasicHousingManage> queryWrapper = new QueryWrapper<>();
        if(basicHousingManage.getId() != null){
            queryWrapper.lambda().and(i -> i.eq(BasicHousingManage::getId, basicHousingManage.getId()));
        }
        if(StringUtils.isNotBlank(basicHousingManage.getStreet())){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getStreet, basicHousingManage.getStreet()));
        }
        if(StringUtils.isNotBlank(basicHousingManage.getCommunity())){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getCommunity, basicHousingManage.getCommunity()));
        }
        if(StringUtils.isNotBlank(basicHousingManage.getOwnedGrid())){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getOwnedGrid, basicHousingManage.getOwnedGrid()));
        }
        if(StringUtils.isNotBlank(basicHousingManage.getHouseName())){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getHouseName, basicHousingManage.getHouseName()));
        }
        if(StringUtils.isNotBlank(basicHousingManage.getHouseAddress())){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getHouseAddress, basicHousingManage.getHouseAddress()));
        }
        if(basicHousingManage.getLocation() != null){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getLocation, basicHousingManage.getLocation()));
        }
        if(StringUtils.isNotBlank(basicHousingManage.getHouseHost())){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getHouseHost, basicHousingManage.getHouseHost()));
        }
        if(StringUtils.isNotBlank(basicHousingManage.getIdCard())){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getIdCard, basicHousingManage.getIdCard()));
        }
        if(StringUtils.isNotBlank(basicHousingManage.getCustomerName())){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getCustomerName, basicHousingManage.getCustomerName()));
        }
        if(StringUtils.isNotBlank(basicHousingManage.getCustomerIdcard())){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getCustomerIdcard, basicHousingManage.getCustomerIdcard()));
        }
        if(StringUtils.isNotBlank(basicHousingManage.getCustomerMobile())){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getCustomerMobile, basicHousingManage.getCustomerMobile()));
        }
        if(StringUtils.isNotBlank(basicHousingManage.getHouseType())){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getHouseType, basicHousingManage.getHouseType()));
        }
        if(StringUtils.isNotBlank(basicHousingManage.getRentStatus())){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getRentStatus, basicHousingManage.getRentStatus()));
        }
        if(basicHousingManage.getHouseNumber() != null){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getHouseNumber, basicHousingManage.getHouseNumber()));
        }
        if(basicHousingManage.getIsBindmap() != null){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getIsBindmap, basicHousingManage.getIsBindmap()));
        }
        if(basicHousingManage.getIsDelete() != null){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getIsDelete, basicHousingManage.getIsDelete()));
        }
        if(basicHousingManage.getCreateId() != null){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getCreateId, basicHousingManage.getCreateId()));
        }
        if(basicHousingManage.getCreateTime() != null){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getCreateTime, basicHousingManage.getCreateTime()));
        }
        if(basicHousingManage.getUpdateId() != null){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getUpdateId, basicHousingManage.getUpdateId()));
        }
        if(basicHousingManage.getUpdateTime() != null){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getUpdateTime, basicHousingManage.getUpdateTime()));
        }
        if(basicHousingManage.getBak1() != null){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getBak1, basicHousingManage.getBak1()));
        }
        if(basicHousingManage.getBak2() != null){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getBak2, basicHousingManage.getBak2()));
        }
        if(basicHousingManage.getBak3() != null){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getBak3, basicHousingManage.getBak3()));
        }
        if(basicHousingManage.getBuildArchivesName() != null){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getBuildArchivesName, basicHousingManage.getBuildArchivesName()));
        }
        if(basicHousingManage.getDoorNumber() != null){
            queryWrapper.lambda().and(i -> i.like(BasicHousingManage::getDoorNumber, basicHousingManage.getDoorNumber()));
        }
        if(searchVo!=null){
            if(searchVo.getStartDate()!=null && searchVo.getEndDate()!=null){
                queryWrapper.lambda().and(i -> i.between(BasicHousingManage::getCreateTime, searchVo.getStartDate(),searchVo.getEndDate()));
            }
        }
        queryWrapper.lambda().and(i -> i.eq(BasicHousingManage::getIsDelete, 0));
        return queryWrapper;

    }

    @Override
    public Result<Object> queryHousingManage(BasicHousingManage basicHousingManage){
        QueryWrapper<BasicHousingManage> queryWrapper = new QueryWrapper<>();
        if (basicHousingManage !=null) {
            queryWrapper = LikeAllFeild(basicHousingManage,null);
        }
        List<BasicHousingManage> result = basicHousingManageMapper.selectList(queryWrapper);
        return  ResultUtil.data(result);
    }
}
